package de.slikey.effectlib.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ConfigUtils {
    public static Collection<ConfigurationSection> getNodeList(ConfigurationSection node, String path) {
        Collection<ConfigurationSection> results = new ArrayList<ConfigurationSection>();
        List<Map<?, ?>> mapList = node.getMapList(path);
        for (Map<?, ?> map : mapList) {
            results.add(toConfigurationSection(map));
        }

        return results;
    }

    @Deprecated
    public static ConfigurationSection toNodeList(Map<?, ?> nodeMap) {
        return toConfigurationSection(nodeMap);
    }

    public static ConfigurationSection toConfigurationSection(Map<?, ?> nodeMap) {
        ConfigurationSection newSection = new MemoryConfiguration();
        for (Map.Entry<?, ?> entry : nodeMap.entrySet()) {
            set(newSection, entry.getKey().toString(), entry.getValue());
        }

        return newSection;
    }

    public static void set(ConfigurationSection node, String path, Object value)
    {
        if (value == null) {
            node.set(path, value);
            return;
        }
        // This is a bunch of hackery... I suppose I ought to change the NBT
        // types to match and make this smarter?
        boolean isTrue = value.equals("true");
        boolean isFalse = value.equals("false");
        if (isTrue || isFalse) {
            node.set(path, isTrue);
        } else {
            try {
                Double d = (value instanceof Double) ? (Double)value : (
                        (value instanceof Float) ? (Float)value :
                                Double.parseDouble(value.toString())
                );
                node.set(path, d);
            } catch (Exception ex) {
                try {
                    Integer i = (value instanceof Integer) ? (Integer)value : Integer.parseInt(value.toString());
                    node.set(path, i);
                } catch (Exception ex2) {
                    node.set(path, value);
                }
            }
        }
    }

    public static ConfigurationSection getConfigurationSection(ConfigurationSection base, String key)
    {
        Object value = base.get(key);
        if (value == null) return null;

        if (value instanceof ConfigurationSection)
        {
            return (ConfigurationSection)value;
        }

        if (value instanceof Map)
        {
            ConfigurationSection newChild = base.createSection(key);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>)value;
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                newChild.set(entry.getKey(), entry.getValue());
            }
            base.set(key, newChild);
            return newChild;
        }

        return null;
    }
}
