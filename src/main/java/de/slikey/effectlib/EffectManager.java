package de.slikey.effectlib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.slikey.effectlib.util.ParticleEffect;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import de.slikey.effectlib.util.Disposable;
import org.bukkit.util.Vector;

/**
 * Dispose the EffectManager if you don't need him anymore.
 * 
 * @author Kevin
 * 
 */
public final class EffectManager implements Disposable {

	private final Plugin owningPlugin;
	private final Map<Effect, BukkitTask> effects;
    private static List<EffectManager> effectManagers;
	private boolean disposed;
	private boolean disposeOnTermination;

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
            for (Iterator<EffectManager> i = effectManagers.iterator(); i.hasNext(); ) {
                EffectManager em = i.next();
                i.remove();
                em.dispose();
            }
        }
    }

	public EffectManager(Plugin owningPlugin) {
		this.owningPlugin = owningPlugin;
		effects = new HashMap<Effect, BukkitTask>();
		disposed = false;
		disposeOnTermination = false;
	}

	public void start(Effect effect) {
		if (disposed)
			throw new IllegalStateException("EffectManager is disposed and not able to accept any effects.");
		if (disposeOnTermination)
			throw new IllegalStateException("EffectManager is awaiting termination to dispose and not able to accept any effects.");

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

    public Effect start(String effectClass, ConfigurationSection parameters, Location origin, Location target, Entity originEntity, Entity targetEntity, Map<String, String> textMap) {
        Class<? extends Effect> effectLibClass;
        try {
            // A shaded manager may provide a fully-qualified path.
            if (!effectClass.contains(".")) {
                effectClass = "de.slikey.effectlib.effect." + effectClass;
            }
            effectLibClass = (Class<? extends Effect>)Class.forName(effectClass);
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
            if (key.equals("class")) continue;

            if (!setField(effect, key, parameters, textMap)) {
                owningPlugin.getLogger().warning("Unable to assign EffectLib property " + key + " of class " + effectLibClass.getName());
            }
        }

        effect.setLocation(origin);
        effect.setTarget(target);
        effect.setTargetEntity(targetEntity);
        effect.setEntity(originEntity);

        effect.start();
        return effect;
    }

    protected boolean setField(Object effect, String key, ConfigurationSection section, Map<String, String> textMap) {
        try {
            Field field = effect.getClass().getField(key);
            if (field.getType().equals(Integer.TYPE)) {
                field.set(effect, section.getInt(key));
            } else if (field.getType().equals(Float.TYPE)) {
                field.set(effect, (float)section.getDouble(key));
            } else if (field.getType().equals(Double.TYPE)) {
                field.set(effect, section.getDouble(key));
            } else if (field.getType().equals(Boolean.TYPE)) {
                field.set(effect, section.getBoolean(key));
            } else if (field.getType().equals(Long.TYPE)) {
                field.set(effect, section.getLong(key));
            } else if (field.getType().isAssignableFrom(String.class)) {
                String value = section.getString(key);
                if (textMap != null) {
                    for (Map.Entry<String, String> replaceEntry : textMap.entrySet()) {
                        value = value.replace(replaceEntry.getKey(), replaceEntry.getValue());
                    }
                }
                field.set(effect, value);
            } else if (field.getType().isAssignableFrom(ParticleEffect.class)) {
                String typeName = section.getString(key);
                ParticleEffect particleType = ParticleEffect.valueOf(typeName.toUpperCase());
                field.set(effect, particleType);
            } else if (field.getType().equals(Sound.class)) {
                String soundName = section.getString(key);
                try {
                    Sound sound = Sound.valueOf(soundName.toUpperCase());
                    field.set(effect, sound);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (field.getType().equals(Color.class)) {
                String hexColor = section.getString(key);
                try {
                    Integer rgb = Integer.parseInt(hexColor, 16);
                    Color color = Color.fromRGB(rgb);
                    field.set(effect, color);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (field.getType().equals(Vector.class)) {
                double x = 0;
                double y = 0;
                double z = 0;
                try {
                    String[] pieces = StringUtils.split(section.getString(key), ',');
                    x = pieces.length > 0 ? Double.parseDouble(pieces[0]) : 0;
                    y = pieces.length > 1 ? Double.parseDouble(pieces[1]) : 0;
                    z = pieces.length > 2 ? Double.parseDouble(pieces[2]) : 0;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                field.set(effect, new Vector(x, y, z));
            } else {
                return false;
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

	public void cancel(boolean callback) {
		for (Map.Entry<Effect, BukkitTask> entry : effects.entrySet())
			entry.getKey().cancel(callback);
	}

	public void done(Effect effect) {
		synchronized (this) {
            BukkitTask existingTask = effects.get(effect);
            if (existingTask != null) {
                existingTask.cancel();
            }
			effects.remove(effect);
		}
		if (effect.callback != null)
			Bukkit.getScheduler().runTask(owningPlugin, effect.callback);
		if (disposeOnTermination && effects.size() == 0)
			dispose();
	}

	public void dispose() {
		if (disposed)
			return;
		disposed = true;
		cancel(false);
        if (effectManagers != null) {
            effectManagers.remove(this);
        }
	}

	public void disposeOnTermination() {
		disposeOnTermination = true;
		if (effects.size() == 0)
			dispose();
	}

}
