package de.slikey.effectlib;


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
	public float visibleRange = 16;

	private boolean done = false;
	private final EffectManager effectManager;

	public Effect(EffectManager effectManager) {
		this.effectManager = effectManager;
	}

	public final void cancel(boolean callback) {
		if (callback)
			done();
		else
			done = true;
	}

	private final void done() {
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
		iterations = -1;
	}

}
