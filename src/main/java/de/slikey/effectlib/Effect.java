package de.slikey.effectlib;


import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.lang.ref.WeakReference;

public abstract class Effect implements Runnable {

	/**
	 * Handles the type, the effect is played.
	 * 
	 * @see {@link de.slikey.effectlib.EffectType}
	 */
	public EffectType type = EffectType.INSTANT;

	/**
	 * Delay to wait for delayed effects.
	 * 
	 * @see {@link de.slikey.effectlib.EffectType}
	 */
	public int delay = 0;

	/**
	 * Interval to wait for repeating effects.
	 * 
	 * @see {@link de.slikey.effectlib.EffectType}
	 */
	public int period = 1;

	/**
	 * Amount of repititions to do.
	 * Set this to -1 for an infinite effect
	 * 
	 * @see {@link de.slikey.effectlib.EffectType}
	 */
	public int iterations = 0;

	/**
	 * Callback to run, after effect is done.
	 * 
	 * @see {@link java.lang.Runnable}
	 */
	public Runnable callback = null;

	/**
	 * Display particles to players within this radius. Squared radius for
	 * performance reasons.
	 */
	public float visibleRange = 32;

    /**
     * The interval at which we will update the cached Entity Location.
     * This value is specified in milliseconds.
     *
     * A value of 0 indicates no caching should be done- this may be
     * expensive.
     */
    public int locationUpdateInterval = 250;

    private Location location = null;
    private WeakReference<Entity> entity = new WeakReference<Entity>(null);
    private long lastLocationUpdate = 0;

	private boolean done = false;
	private final EffectManager effectManager;

	public Effect(EffectManager effectManager) {
        Validate.notNull(effectManager, "EffectManager cannot be null!");
		this.effectManager = effectManager;
	}

    public final void cancel() {
        cancel(true);
    }

	public final void cancel(boolean callback) {
		if (callback)
			done();
		else
			done = true;
	}

	private void done() {
		done = true;
		effectManager.done(this);
	}

	public final boolean isDone() {
		return done;
	}

	public abstract void onRun();

	@Override
	public final void run() {
		if (done)
			return;
		onRun();

		if (type == EffectType.REPEATING) {
			if (iterations == -1)
				return;
			iterations--;
			if (iterations < 1)
				done();
		} else {
			done();
		}
	}

	public final void start() {
		effectManager.start(this);
	}

	public final void infinite() {
		type = EffectType.REPEATING;
		iterations = -1;
	}

    /**
     * Extending Effect classes should use this method to obtain the
     * current "root" Location of the effect.
     */
    public Location getLocation()
    {
        Entity entityReference = entity.get();
        if (entityReference != null) {
            long now = System.currentTimeMillis();
            if (locationUpdateInterval == 0 || lastLocationUpdate == 0 || lastLocationUpdate + locationUpdateInterval > now) {
                location = entityReference.getLocation();
            }
        }

        return location;
    }
}

