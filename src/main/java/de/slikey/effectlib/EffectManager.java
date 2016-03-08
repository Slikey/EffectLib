package de.slikey.effectlib;

import de.slikey.effectlib.util.Disposable;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

/**
 * Dispose the EffectManager if you don't need him anymore.
 *
 * @author Kevin
 *
 */
public final class EffectManager implements Disposable {

    private static List<EffectManager> effectManagers;
    private static Map<String, Class<? extends Effect>> effectClasses = new HashMap<String, Class<? extends Effect>>();
    private final Plugin owningPlugin;
    private final Map<Effect, BukkitTask> effects;
    private boolean disposed;
    private boolean disposeOnTermination;
    private boolean debug = false;
    private int visibleRange = 32;

    public EffectManager(Plugin owningPlugin) {
        ParticleEffect.ParticlePacket.initialize();
        this.owningPlugin = owningPlugin;
        effects = new HashMap<Effect, BukkitTask>();
        disposed = false;
        disposeOnTermination = false;
    }

    public void start(Effect effect) {
        if (disposed) {
            throw new IllegalStateException("EffectManager is disposed and not able to accept any effects.");
        }
        if (disposeOnTermination) {
            throw new IllegalStateException("EffectManager is awaiting termination to dispose and not able to accept any effects.");
        }

        if (effects.containsKey(effect)) {
            effect.cancel(false);
        }

        BukkitScheduler s = Bukkit.getScheduler();
        BukkitTask task = null;
        switch (effect.type) {
            case INSTANT:
                task = s.runTask(owningPlugin, effect);
                break;
            case DELAYED:
                task = s.runTaskLater(owningPlugin, effect, effect.delay);
                break;
            case REPEATING:
                task = s.runTaskTimer(owningPlugin, effect, effect.delay, effect.period);
                break;
        }
        synchronized (this) {
            effects.put(effect, task);
        }
    }

    public Effect start(String effectClass, ConfigurationSection parameters, Location origin, Entity originEntity) {
        return start(effectClass, parameters, origin, null, originEntity, null, null);
    }

    public Effect start(String effectClass, ConfigurationSection parameters, Entity originEntity) {
        return start(effectClass, parameters, originEntity == null ? null : originEntity.getLocation(), null, originEntity, null, null);
    }

    public Effect start(String effectClass, ConfigurationSection parameters, Location origin) {
        return start(effectClass, parameters, origin, null, null, null, null);
    }

    /**
     * Start an Effect from a Configuration map of parameters.
     *
     * @param effectClass The name of the Effect class to instantiate. If unqualified, defaults to the de.slikey.effectlib.effect namespace.
     * @param parameters A Configuration-driven map of key/value parameters. Each of these will be applied directly to the corresponding field in the Effect instance.
     * @param origin The origin Location
     * @param target The target Location, only used in some Effects (like LineEffect)
     * @param originEntity The origin Entity, the effect will attach to the Entity's Location
     * @param targetEntity The target Entity, only used in some Effects
     * @param parameterMap A map of parameter values to replace. These must start with the "$" character, values in the parameters map that contain a $key will be replaced with the value in this parameterMap.
     * @return
     */
    public Effect start(String effectClass, ConfigurationSection parameters, Location origin, Location target, Entity originEntity, Entity targetEntity, Map<String, String> parameterMap) {
        return start(effectClass, parameters, new DynamicLocation(origin, originEntity), new DynamicLocation(target, targetEntity), parameterMap);
    }

    /**
     * Start an Effect from a Configuration map of parameters.
     *
     * @param effectClass The name of the Effect class to instantiate. If unqualified, defaults to the de.slikey.effectlib.effect namespace.
     * @param parameters A Configuration-driven map of key/value parameters. Each of these will be applied directly to the corresponding field in the Effect instance.
     * @param origin The origin Location
     * @param target The target Location, only used in some Effects (like LineEffect)
     * @param parameterMap A map of parameter values to replace. These must start with the "$" character, values in the parameters map that contain a $key will be replaced with the value in this parameterMap.
     * @return
     */
    public Effect start(String effectClass, ConfigurationSection parameters, DynamicLocation origin, DynamicLocation target, Map<String, String> parameterMap) {
        Class<? extends Effect> effectLibClass;
        try {
            // A shaded manager may provide a fully-qualified path.
            if (!effectClass.contains(".")) {
                effectClass = "de.slikey.effectlib.effect." + effectClass;
                if (!effectClass.endsWith("Effect")) {
                    effectClass = effectClass + "Effect";
                }
            }
            effectLibClass = effectClasses.get(effectClass);
            if (effectLibClass == null) {
                effectLibClass = (Class<? extends Effect>) Class.forName(effectClass);
                effectClasses.put(effectClass, effectLibClass);
            }
        } catch (Throwable ex) {
            owningPlugin.getLogger().info("Error loading EffectLib class: " + effectClass + ": " + ex.getMessage());
            return null;
        }

        Effect effect = null;
        try {
            Constructor constructor = effectLibClass.getConstructor(EffectManager.class);
            effect = (Effect) constructor.newInstance(this);
        } catch (Exception ex) {
            owningPlugin.getLogger().warning("Error creating Effect class: " + effectClass);
        }
        if (effect == null) {
            return null;
        }

        Collection<String> keys = parameters.getKeys(false);
        for (String key : keys) {
            if (key.equals("class")) {
                continue;
            }

            if (!setField(effect, key, parameters, parameterMap) && debug) {
                owningPlugin.getLogger().warning("Unable to assign EffectLib property " + key + " of class " + effectLibClass.getName());
            }
        }

        effect.setDynamicOrigin(origin);
        effect.setDynamicTarget(target);

        effect.start();
        return effect;
    }
    
    public void cancel(boolean callback) {
        for (Map.Entry<Effect, BukkitTask> entry : effects.entrySet()) {
            entry.getKey().cancel(callback);
        }
    }
    
    public void done(Effect effect) {
        synchronized (this) {
            BukkitTask existingTask = effects.get(effect);
            if (existingTask != null) {
                existingTask.cancel();
            }
            effects.remove(effect);
        }
        if (effect.callback != null) {
            Bukkit.getScheduler().runTask(owningPlugin, effect.callback);
        }
        if (disposeOnTermination && effects.isEmpty()) {
            dispose();
        }
    }
    
    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        cancel(false);
        if (effectManagers != null) {
            effectManagers.remove(this);
        }
    }
    
    public void disposeOnTermination() {
        disposeOnTermination = true;
        if (effects.isEmpty()) {
            dispose();
        }
    }

    public void enableDebug(boolean enable) {
        debug = enable;
    }

    public boolean isDebugEnabled() {
        return debug;
    }
    
    public void onError(Throwable ex) {
        if (debug) {
            owningPlugin.getLogger().log(Level.WARNING, "Particle Effect error", ex);
        }
    }

    public int getParticleRange() {
        return visibleRange;
    }
    
    public void setParticleRange(int range) {
        visibleRange = range;
    }

    public Plugin getOwningPlugin() {
        return owningPlugin;
    }

    protected boolean setField(Object effect, String key, ConfigurationSection section, Map<String, String> parameterMap) {
        try {
            String value = section.getString(key);
            if (parameterMap != null && !parameterMap.isEmpty() && value.startsWith("$")) {
                String parameterValue = parameterMap.get(value);
                value = parameterValue == null ? value : parameterValue;
            }
            Field field = effect.getClass().getField(key);
            if (field.getType().equals(Integer.TYPE) || field.getType().equals(Integer.class)) {
                field.set(effect, NumberConversions.toInt(value));
            } else if (field.getType().equals(Float.TYPE) || field.getType().equals(Float.class)) {
                field.set(effect, NumberConversions.toFloat(value));
            } else if (field.getType().equals(Double.TYPE) || field.getType().equals(Double.class)) {
                field.set(effect, NumberConversions.toDouble(value));
            } else if (field.getType().equals(Boolean.TYPE) || field.getType().equals(Boolean.class)) {
                field.set(effect, value.equalsIgnoreCase("true"));
            } else if (field.getType().equals(Long.TYPE) || field.getType().equals(Long.class)) {
                field.set(effect, NumberConversions.toLong(value));
            } else if (field.getType().equals(Short.TYPE) || field.getType().equals(Short.class)) {
                field.set(effect, NumberConversions.toShort(value));
            } else if (field.getType().equals(Byte.TYPE) || field.getType().equals(Byte.class)) {
                field.set(effect, NumberConversions.toByte(value));
            } else if (field.getType().isAssignableFrom(String.class)) {
                field.set(effect, value);
            } else if (field.getType().isAssignableFrom(ParticleEffect.class)) {
                ParticleEffect particleType = ParticleEffect.valueOf(value.toUpperCase());
                field.set(effect, particleType);
            } else if (field.getType().isAssignableFrom(EffectType.class)) {
                EffectType effectType = EffectType.valueOf(value.toUpperCase());
                field.set(effect, effectType);
            } else if (field.getType().equals(Sound.class)) {
                try {
                    Sound sound = Sound.valueOf(value.toUpperCase());
                    field.set(effect, sound);
                } catch (Exception ex) {
                    onError(ex);
                }
            } else if (field.getType().equals(Material.class)) {
                try {
                    Material material = Material.valueOf(value.toUpperCase());
                    field.set(effect, material);
                } catch (Exception ex) {
                    onError(ex);
                }
            } else if (field.getType().equals(Color.class)) {
                try {
                    Integer rgb = Integer.parseInt(value, 16);
                    Color color = Color.fromRGB(rgb);
                    field.set(effect, color);
                } catch (Exception ex) {
                    onError(ex);
                }
            } else if (field.getType().equals(Vector.class)) {
                double x = 0;
                double y = 0;
                double z = 0;
                try {
                    String[] pieces = value.split(",");
                    x = pieces.length > 0 ? Double.parseDouble(pieces[0]) : 0;
                    y = pieces.length > 1 ? Double.parseDouble(pieces[1]) : 0;
                    z = pieces.length > 2 ? Double.parseDouble(pieces[2]) : 0;
                } catch (Exception ex) {
                    onError(ex);
                }
                field.set(effect, new Vector(x, y, z));
            } else {
                return false;
            }

            return true;
        } catch (Exception ex) {
            this.onError(ex);
        }

        return false;
    }
    
    public static void initialize() {
        effectManagers = new ArrayList<EffectManager>();
    }
    
    public static List<EffectManager> getManagers() {
        if (effectManagers == null) {
            initialize();
        }
        return effectManagers;
    }
    
    public static void disposeAll() {
        if (effectManagers != null) {
            for (Iterator<EffectManager> i = effectManagers.iterator(); i.hasNext();) {
                EffectManager em = i.next();
                i.remove();
                em.dispose();
            }
        }
    }
}
