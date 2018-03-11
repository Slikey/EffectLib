package de.slikey.effectlib.math;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.ConfigUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transforms {
    private static final String TRANSFORM_BUILTIN_CLASSPATH = "de.slikey.effectlib.math";
    private static Map<String, Class<?>> transformClasses = new HashMap<String, Class<?>>();
    private static EffectManager effectManager;

    public static Transform loadTransform(ConfigurationSection base, String value) {
        if (base.isConfigurationSection(value)) {
            return loadTransform(ConfigUtils.getConfigurationSection(base, value));
        }
        if (base.isDouble(value) || base.isInt(value)) {
            return new ConstantTransform(base.getDouble(value));
        }
        if (base.isString(value)) {
            String equation = base.getString(value);
            if (equation.equalsIgnoreCase("t") || equation.equalsIgnoreCase("time")) {
                return new EchoTransform();
            }
            EquationTransform transform = EquationStore.getInstance().getTransform(equation, "t");
            Exception ex = transform.getException();
            if (ex != null && effectManager != null) {
                effectManager.onError("Error parsing equation: " + equation, ex);
            }
            return transform;
        }
        return new ConstantTransform(0);
    }

    public static Collection<Transform> loadTransformList(ConfigurationSection base, String value)
    {
        Collection<ConfigurationSection> transformConfigs = ConfigUtils.getNodeList(base, value);
        List<Transform> transforms = new ArrayList<Transform>();
        if (transformConfigs != null)
        {
            for (ConfigurationSection transformConfig : transformConfigs)
            {
                transforms.add(loadTransform(transformConfig));
            }
        }

        return transforms;
    }

    public static Transform loadTransform(ConfigurationSection parameters) {
        Transform transform = null;
        if (parameters != null && parameters.contains("class"))
        {
            String className = parameters.getString("class");
            try
            {
                if (!className.contains("."))
                {
                    className = TRANSFORM_BUILTIN_CLASSPATH + "." + className;
                }
                Class<?> genericClass = transformClasses.get(className);
                if (genericClass == null) {
                    try {
                        genericClass = Class.forName(className + "Transform");
                    } catch (Exception ex) {
                        genericClass = Class.forName(className);
                    }

                    if (!Transform.class.isAssignableFrom(genericClass)) {
                        throw new Exception("Must extend Transform");
                    }
                    transformClasses.put(className, genericClass);
                }

                @SuppressWarnings("unchecked")
                Class<? extends Transform> transformClass = (Class<? extends Transform>)genericClass;
                transform = transformClass.newInstance();
                parameters.set("class", null);
                transform.load(parameters);
            } catch (Exception ex) {
                if (effectManager != null) {
                    effectManager.onError("Error loading class " + className, ex);
                }
            }
        }

        return transform == null ? new ConstantTransform(0) : transform;
    }

    public static void setEffectManager(EffectManager manager) {
        effectManager = manager;
    }
}
