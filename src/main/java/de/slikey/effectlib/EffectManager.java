package de.slikey.effectlib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import de.slikey.effectlib.util.Disposable;

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

    public Effect[] start(String effectClass, ConfigurationSection parameters, Location origin, Location target) {
        return start(effectClass, parameters, origin, target, null, null);
    }

    public Effect[] start(String effectClass, ConfigurationSection parameters, Location origin, Location target, Entity originEntity, Entity targetEntity) {
        return start(effectClass, parameters, origin, target, originEntity, targetEntity);
    }

    public Effect[] start(String effectClass, ConfigurationSection parameters, Location origin, Location target, Entity originEntity, Entity targetEntity, Map<String, String> textMap) {
        Class<? extends Effect> effectLibClass = null;
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

        Effect[] effects = tryPointConstructor(effectLibClass, origin, target);
        if (effects == null) {
            effects = tryEntityConstructor(effectLibClass, originEntity, targetEntity);
            if (effects == null) {
                effects = tryLineConstructor(effectLibClass, origin, target);
            }
        }

        if (effects == null) {
            owningPlugin.getLogger().info("Failed to construct EffectLib class: " + effectLibClass.getName());
            return null;
        }

        Collection<String> keys = parameters.getKeys(false);
        for (Effect effect : effects) {
            for (String key : keys) {
                if (key.equals("class")) continue;

                if (!setField(effect, key, parameters, textMap)) {
                    owningPlugin.getLogger().warning("Unable to assign EffectLib property " + key + " of class " + effectLibClass.getName());
                }
            }

            try {
                Method startMethod = effectLibClass.getMethod("start");
                startMethod.invoke(effect);
            } catch (Throwable ex) {

            }
        }

        return effects;
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
            } else {
                return false;
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    protected Effect[] tryPointConstructor(Class<? extends Effect> effectLibClass, Location origin, Location target) {
        if (origin == null && target == null) return null;

        Effect[] players = null;
        try {
            Constructor constructor = effectLibClass.getConstructor(EffectManager.class, Location.class);
            if (target != null && origin != null) {
                players = new Effect[2];
                players[0] = (Effect)constructor.newInstance(this, target);
                players[1] = (Effect)constructor.newInstance(this, origin);
            } else if (target != null) {
                players = new Effect[1];
                players[0] = (Effect)constructor.newInstance(this, target);
            } else if (origin != null) {
                players = new Effect[1];
                players[0] = (Effect)constructor.newInstance(this, origin);
            }
        } catch (Exception ex) {
            players = null;
        }
        return players;
    }

    protected Effect[] tryEntityConstructor(Class<? extends Effect> effectLibClass, Entity origin, Entity target) {
        if (target == null && origin == null) return null;

        Effect[] players = null;
        try {
            Constructor constructor = effectLibClass.getConstructor(EffectManager.class, Entity.class);
            if (target != null && origin != null) {
                players = new Effect[2];
                players[0] = (Effect)constructor.newInstance(this, target);
                players[1] = (Effect)constructor.newInstance(this, origin);
            } else if (target != null) {
                players = new Effect[1];
                players[0] = (Effect)constructor.newInstance(this, target);
            } else if (origin != null) {
                players = new Effect[1];
                players[0] = (Effect)constructor.newInstance(this, origin);
            }
        } catch (Exception ex) {
            players = null;
        }
        return players;
    }

    protected Effect[] tryLineConstructor(Class<? extends Effect> effectLibClass, Location origin, Location target) {
        if (origin == null || target == null) return null;

        Effect[] players = null;
        try {
            Constructor constructor = effectLibClass.getConstructor(EffectManager.class, Location.class, Location.class);
            players = new Effect[1];
            players[0] = (Effect)constructor.newInstance(this, origin, target);
        } catch (Exception ex) {
            players = null;
        }
        return players;
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
