package de.slikey.effectlib.effect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import com.google.common.base.CaseFormat;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.math.EquationStore;
import de.slikey.effectlib.math.EquationTransform;
import de.slikey.effectlib.util.VectorUtils;

public class ModifiedEffect extends Effect {
    private final static String[] _variables = {"t", "i", "a", "b"};
    private final static List<String> variables = Arrays.asList(_variables);

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
     * Move the entire effect's x location around
     */
    public String xEquation = null;

    /**
     * Move the entire effect's y location around
     */
    public String yEquation = null;

    /**
     * Move the entire effect's z location around
     */
    public String zEquation = null;

    /**
     * The starting value of variable a
     */
    public double variableA;

    /**
     * The starting value of variable b
     */
    public double variableB;

    /**
     * Whether or not to orient the effect in the direction
     * of the source Location
     *
     * If this is set to true, the X axis will represent "forward".
     *
     * This is only used if setting an x, y, z equation.
     */
    public boolean orient = true;

    /**
     * Similar to orient, however this is specific to pitch.
     */
    public boolean orientPitch = false;

    /**
     * Effect parameters to modify each tick, paired with an equation used to modify them.
     */
    public Map<String, String> parameters = new HashMap<String, String>();

    private boolean initialized = false;
    private Effect innerEffect;
    private Map<Field, EquationTransform> parameterTransforms = new HashMap<Field, EquationTransform>();
    private int step = 0;

    private EquationTransform xTransform;
    private EquationTransform yTransform;
    private EquationTransform zTransform;
    private Vector previousOffset;

    public ModifiedEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 100;
    }

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
            innerEffect.material = material;
            innerEffect.materialData = materialData;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String equation = entry.getValue();
                String fieldName = entry.getKey();

                // Allow underscore_style and dash_style parameters
                if (fieldName.contains("-")) {
                    fieldName = fieldName.replace("-", "_");
                }
                if (fieldName.contains("_")) {
                    fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName);
                }

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

            if (xEquation != null) xTransform = EquationStore.getInstance().getTransform(xEquation, _variables);
            if (yEquation != null) yTransform = EquationStore.getInstance().getTransform(yEquation, _variables);
            if (zEquation != null) zTransform = EquationStore.getInstance().getTransform(zEquation, _variables);
        }
        if (innerEffect == null) {
            cancel();
            return;
        }

        if (origin != null && xTransform != null || yTransform != null || zTransform != null) {
            Vector offset = new Vector(
                xTransform == null ? 0 : xTransform.get(step, maxIterations, variableA, variableB),
                yTransform == null ? 0 : yTransform.get(step, maxIterations, variableA, variableB),
                zTransform == null ? 0 : zTransform.get(step, maxIterations, variableA, variableB)
            );

            if (previousOffset != null) {
                offset.subtract(previousOffset);
            } else {
                previousOffset = new Vector();
            }

            Location location = getLocation();
            if (orient && orientPitch) {
                offset = VectorUtils.rotateVector(offset, location);
            } else if (orient) {
                offset = VectorUtils.rotateVector(offset, location.getYaw(), 0);
            }

            origin.addOffset(offset);
            previousOffset.add(offset);
        }

        for (Map.Entry<Field, EquationTransform> entry : parameterTransforms.entrySet()) {
            double value = entry.getValue().get(step, maxIterations, variableA, variableB);
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
