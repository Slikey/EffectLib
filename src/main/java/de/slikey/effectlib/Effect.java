package de.slikey.effectlib;


import de.slikey.effectlib.util.ParticleEffect;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.lang.ref.WeakReference;

public abstract class Effect implements Runnable {

	/**
	 * Handles the type, the effect is played.
	 * 
	 * @see {@link de.slikey.effectlib.EffectType}
	 */
	public EffectType type = EffectType.INSTANT;

    /**
     * Can be used to colorize certain particles. As of 1.8, those
     * include SPELL_MOB_AMBIENT, SPELL_MOB and REDSTONE.
     */
    public Color color = null;

    /** This is only used when colorizing certain particles,
     * since the speed can't be 0 for the particle to look colored.
     */
    public float speed = 1;

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
    public int locationUpdateInterval = 100;

    /**
     * If true, and a "target" Location or Entity is set, the two Locations
     * will orient to face one another.
     */
    public boolean autoOrient = true;

    /**
     * If set, will offset all locations
     */
    public Vector offset = null;

    /**
     * If set, will offset the target location
     */
    public Vector targetOffset = null;

    private Location location = null;
    private WeakReference<Entity> entity = new WeakReference<Entity>(null);
    private Location target = null;
    private WeakReference<Entity> targetEntity = new WeakReference<Entity>(null);
    private long lastLocationUpdate = 0;
    private long lastTargetUpdate = 0;

	private boolean done = false;
	protected final EffectManager effectManager;

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
        if (!validate()) {
            cancel();
            return;
        }
		if (done)
			return;
        try {
            onRun();
        } catch (Exception ex) {
            done();
            effectManager.onError(ex);
        }
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

    protected final boolean validate() {
        // Check for a valid Location
        Location location = getLocation();
        if (location == null) return false;
        if (autoOrient) {
            Location target = getTarget();
            if (target != null) {
                Vector direction = target.toVector().subtract(location.toVector());
                location.setDirection(direction);
                target.setDirection(direction.multiply(-1));
            }
        }

        return true;
    }

	public final void start() {
		effectManager.start(this);
	}

	public final void infinite() {
		type = EffectType.REPEATING;
		iterations = -1;
	}

    /**
     * Extending Effect classes can use this to determine the Entity this
     * Effect is centered upon.
     *
     * This may return null, even for an Effect that was set with an Entity,
     * if the Entity gets GC'd.
     */
    public Entity getEntity()
    {
        return this.entity.get();
    }

    /**
     * Extending Effect classes can use this to determine the Entity this
     * Effect is targeted upon. This is probably a very rare case, such as
     * an Effect that "links" two Entities together somehow. (Idea!)
     *
     * This may return null, even for an Effect that was set with a target Entity,
     * if the Entity gets GC'd.
     */
    public Entity getTargetEntity()
    {
        return this.targetEntity.get();
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "root" Location of the effect.
     *
     * This method will not return null when called from onRun. Effects
     * with invalid locations will be cancelled.
     */
    public final Location getLocation()
    {
        Entity entityReference = entity.get();
        if (entityReference != null) {
            long now = System.currentTimeMillis();
            if (locationUpdateInterval == 0 || lastLocationUpdate == 0 || lastLocationUpdate + locationUpdateInterval > now) {
                if (entityReference instanceof LivingEntity) {
                    setLocation(((LivingEntity)entityReference).getEyeLocation());
                } else {
                    setLocation(entityReference.getLocation());
                }
            }
        }

        return location;
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "target" Location of the effect.
     *
     * Unlike getLocation, this may return null.
     */
    public final Location getTarget()
    {
        Entity entityReference = targetEntity.get();
        if (entityReference != null) {
            long now = System.currentTimeMillis();
            if (locationUpdateInterval == 0 || lastTargetUpdate == 0 || lastTargetUpdate + locationUpdateInterval > now) {
                if (entityReference instanceof LivingEntity) {
                    setTarget(((LivingEntity)entityReference).getEyeLocation());
                } else {
                    setTarget(entityReference.getLocation());
                }
            }
        }

        return target;
    }

    /**
     * Set the Entity this Effect is centered on.
     */
    public void setEntity(Entity entity) {
        this.entity = new WeakReference<Entity>(entity);
    }

    /**
     * Set the Entity this Effect is targeting.
     */
    public void setTargetEntity(Entity entity) {
        this.targetEntity = new WeakReference<Entity>(entity);
    }

    /**
     * Set the Location this Effect is centered on.
     */
    public void setLocation(Location location) {
        Validate.notNull(location, "Location cannot be null!");
        this.location = location == null ? null : location.clone();
        if (offset != null && this.location != null) {
            this.location = this.location.add(offset);
            lastLocationUpdate = System.currentTimeMillis();
        }
    }

    /**
     * Set the Location this Effect is targeting.
     */
    public void setTarget(Location location) {
        this.target = location == null ? null : location.clone();
        if (targetOffset != null && this.target != null) {
            this.target = this.target.add(targetOffset);
            lastTargetUpdate = System.currentTimeMillis();
        }
    }

    protected void display(ParticleEffect effect, Location location)
    {
        display(effect, location, this.color);
    }

    protected void display(ParticleEffect particle, Location location, Color color)
    {
        display(particle, location, color, 0, 1);
    }

    protected void display(ParticleEffect particle, Location location, float speed, int amount)
    {
        display(particle, location, this.color, speed, amount);
    }

    protected void display(ParticleEffect particle, Location location, Color color, float speed, int amount)
    {
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;

        // Colorizeable!
        if (color != null && (particle == ParticleEffect.REDSTONE || particle == ParticleEffect.SPELL_MOB || particle == ParticleEffect.SPELL_MOB_AMBIENT))
        {
            amount = 0;
            // See note at the top about this, colored particles can't have a speed of 0.
            speed = this.speed;
            offsetX = (float)color.getRed() / 255;
            offsetY = (float)color.getGreen() / 255;
            offsetZ = (float)color.getBlue() / 255;

            // The redstone particle reverts to red if R is 0!
            if (offsetX < Float.MIN_NORMAL) {
                offsetX = Float.MIN_NORMAL;
            }
        }

        particle.display(offsetX, offsetY, offsetZ, speed, amount, location, visibleRange);
    }
}

