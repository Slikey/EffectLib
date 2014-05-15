package de.slikey.effectlib;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import de.slikey.effectlib.util.Disposable;

/**
 * Dispose the EffectManager if you don't need him anymore. {@link
 * EffectManager.dispose()}
 * 
 * @author Kevin
 * 
 */
public final class EffectManager implements Disposable {

	private final EffectLib effectLib;
	private final Map<Effect, BukkitTask> effects;
	private boolean disposed;
	private boolean disposeOnTermination;

	public EffectManager(EffectLib effectLib) {
		this.effectLib = effectLib;
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
				task = s.runTask(effectLib, effect);
				break;
			case DELAYED:
				task = s.runTaskLater(effectLib, effect, effect.delay);
				break;
			case REPEATING:
				task = s.runTaskTimer(effectLib, effect, effect.delay, effect.period);
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
			Bukkit.getScheduler().runTask(effectLib, effect.callback);
		if (disposeOnTermination && effects.size() == 0)
			dispose();
	}

	public void dispose() {
		if (disposed)
			return;
		disposed = true;
		cancel(false);
		effectLib.getEffectManagers().remove(this);
	}

	public void disposeOnTermination() {
		disposeOnTermination = true;
		if (effects.size() == 0)
			dispose();
	}

}
