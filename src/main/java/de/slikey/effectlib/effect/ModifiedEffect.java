package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.math.EquationStore;
import de.slikey.effectlib.math.EquationTransform;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModifiedEffect extends Effect {
    private final static String[] _variables = {"t", "i"};
    private final static Set<String> variables = new HashSet<String>(Arrays.asList(_variables));

    /**
     * The base configuration of the inner effect.
     */
    public ConfigurationSection effect;

    /**
     * The class name of the effect to modify.
     *
     * Can be left blank if class is set in effect configuration.
     */
    public String effectClass;

    /**
     * Effect parameters to modify each tick, paired with an equation used to modify them.
     */
    public Map<String, String> parameters = new HashMap<String, String>();

    public ModifiedEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 100;
    }

    private boolean initialized = false;
    private Effect innerEffect;
    private Map<Field, EquationTransform> parameterTransforms = new HashMap<Field, EquationTransform>();
    private int step = 0;

    @Override
    public void reset() {
        this.step = 0;
        if (innerEffect != null) {
            innerEffect.prepare();
        }
    }

    @Override
    public void onDone() {
        if (innerEffect != null) {
            innerEffect.onDone();
        }
    }

    @Override
    public void onRun() {
        if (!initialized) {
            initialized = true;
            if (effect == null) {
                effectManager.onError("ModifiedEffect missing inner effect configuration");
                cancel();
                return;
            }

            if (effectClass == null) {
                effectClass = effect.getString("class");
            }
            if (effectClass == null) {
                effectManager.onError("ModifiedEffect missing inner effect class property");
                cancel();
                return;
            }

            innerEffect = effectManager.getEffect(effectClass, effect, origin, target, null, targetPlayer);
            if (innerEffect == null) {
                cancel();
                return;
            }
            innerEffect.materialData = materialData;
            innerEffect.material = material;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String equation = entry.getValue();
                String fieldName = entry.getKey();
                EquationTransform transform = EquationStore.getInstance().getTransform(equation, variables);
                Exception ex = transform.getException();
                if (ex != null) {
                    effectManager.onError("Error parsing equation: " + equation, ex);
                    continue;
                }
                try {
                    Field field = innerEffect.getClass().getField(fieldName);
                    parameterTransforms.put(field, transform);
                } catch (Exception ex2) {
                    effectManager.onError("Error binding to field: " + fieldName + " of effect class " + effectClass, ex2);
                    continue;
                }
            }
            innerEffect.prepare();
        }
        if (innerEffect == null) {
            cancel();
            return;
        }

        for (Map.Entry<Field, EquationTransform> entry : parameterTransforms.entrySet()) {
            double value = entry.getValue().get(step, maxIterations);
            try {
                Field field = entry.getKey();
                if (field.getType().equals(Double.class) || field.getType().equals(Double.TYPE)) {
                    entry.getKey().set(innerEffect,value);
                } else if (field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
                    entry.getKey().set(innerEffect, (int)value);
                } else if (field.getType().equals(Float.class) || field.getType().equals(Float.TYPE)) {
                    entry.getKey().set(innerEffect, (float)value);
                } else if (field.getType().equals(Short.class) || field.getType().equals(Short.TYPE)) {
                    entry.getKey().set(innerEffect, (short)value);
                } else if (field.getType().equals(Byte.class) || field.getType().equals(Byte.TYPE)) {
                    entry.getKey().set(innerEffect, (byte)value);
                } else {
                    effectManager.onError("Can't assign property " + entry.getKey().getName() + " of effect class " + effectClass + " of type " + field.getType().getName());
                    cancel();
                    return;
                }
            } catch (Exception ex) {
                effectManager.onError("Error assigning to : " + entry.getKey().getName() + " of effect class " + effectClass, ex);
                cancel();
                return;
            }
        }

        try {
            innerEffect.onRun();
        } catch (Exception ex) {
            innerEffect.onDone();
            effectManager.onError(ex);
        }
        step++;
    }
}
