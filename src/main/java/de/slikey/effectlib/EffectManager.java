package de.slikey.effectlib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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

	public void cancel(boolean callback) {
		for (Map.Entry<Effect, BukkitTask> entry : effects.entrySet())
			entry.getKey().cancel(callback);
	}

	public void done(Effect effect) {
		synchronized (this) {
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
