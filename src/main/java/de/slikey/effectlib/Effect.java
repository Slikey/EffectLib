package de.slikey.effectlib;

import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

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

    /**
     * This can be used to give particles a set speed when spawned.
     * This will not work with colored particles.
     */
    public float speed = 0;

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
     * Amount of repetitions to do.
     * Set this to -1 for an infinite effect
     *
     * @see {@link de.slikey.effectlib.EffectType}
     */
    public int iterations = 0;

    /**
     * Total duration of this effect in milliseconds.
     *
     * If set, this will adjust iterations to match
     * the defined delay such that the effect lasts
     * a specific duration.
     */
    public Integer duration = null;

    /**
     * Callback to run, after effect is done.
     *
     * @see {@link java.lang.Runnable}
     */
    public Runnable callback = null;

    /**
     * Display particles to players within this radius.
     */
    public float visibleRange = 32;

    /**
     * If true, and a "target" Location or Entity is set, the two Locations
     * will orient to face one another.
     */
    public boolean autoOrient = false;

    /**
     * If set, will offset the origin location
     */
    public Vector offset = null;

    /**
     * If set, will offset the origin location, relative to the origin direction
     */
    public Vector relativeOffset = null;

    /**
     * If set, will offset the target location
     */
    public Vector targetOffset = null;

    /**
     * These are used to modify the direction of the origin Location.
     */
    public float yawOffset = 0;
    public float pitchOffset = 0;

    /**
     * If set to false, Entity-bound locations will not update during the Effect
     */
    public boolean updateLocations = true;

    /**
     * If set to false, Entity-bound directions will not update during the Effect
     */
    public boolean updateDirections = true;

    /**
     * The Material and data to use for block and item break particles
     */
    public Material material;
    public Byte materialData;

    /**
     * These can be used to spawn multiple particles per packet.
     * It will not work with colored particles, however.
     */
    public int particleCount = 1;

    /**
     * These can be used to apply an offset to spawned particles, particularly
     * useful when spawning multiples.
     */
    public int particleOffsetX = 0;
    public int particleOffsetY = 0;
    public int particleOffsetZ = 0;

    /**
     * If set, will run asynchronously.
     * Some effects don't support this (TurnEffect, JumpEffect)
     *
     * Generally this shouldn't be changed, unless you want to
     * make an async effect synchronous.
     */
    public boolean asynchronous = true;
    protected final EffectManager effectManager;
    protected Runnable asyncRunnableTask;

    private DynamicLocation origin = null;
    private DynamicLocation target = null;

    private boolean done = false;

    public Effect(EffectManager effectManager) {
        if (effectManager == null) {
            throw new IllegalArgumentException("EffectManager cannot be null!");
        }
        this.effectManager = effectManager;
        this.visibleRange = effectManager.getParticleRange();
    }

    public final void cancel() {
        cancel(true);
    }

    public final void cancel(boolean callback) {
        if (callback) {
            done();
        } else {
            done = true;
        }
    }

    public final boolean isDone() {
        return done;
    }

    public abstract void onRun();

    /**
     * Called when this effect is done playing (when {@link #done()} is called).
     */
    public void onDone() {
    }

    @Override
    public final void run() {
        if (!validate()) {
            cancel();
            return;
        }
        if (done) {
            return;
        }
        if (asynchronous) {
            if (asyncRunnableTask == null) {
                final Effect effect = this;
                asyncRunnableTask = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            effect.onRun();
                        } catch (Exception ex) {
                            effectManager.onError(ex);
                            Bukkit.getScheduler().runTask(effectManager.getOwningPlugin(), new Runnable() {
                                @Override
                                public void run() {
                                    effect.done();
                                }
                            });
                        }
                    }
                };
            }
            Bukkit.getScheduler().runTaskAsynchronously(effectManager.getOwningPlugin(), asyncRunnableTask);
        } else {
            try {
                onRun();
            } catch (Exception ex) {
                done();
                effectManager.onError(ex);
            }
        }
        if (type == EffectType.REPEATING) {
            if (iterations == -1) {
                return;
            }
            iterations--;
            if (iterations < 1) {
                done();
            }
        } else {
            done();
        }
    }

    public final void start() {
        updateDuration();
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
    public Entity getEntity() {
        return origin == null ? null : origin.getEntity();
    }

    /**
     * Extending Effect classes can use this to determine the Entity this
     * Effect is targeted upon. This is probably a very rare case, such as
     * an Effect that "links" two Entities together somehow. (Idea!)
     *
     * This may return null, even for an Effect that was set with a target Entity,
     * if the Entity gets GC'd.
     */
    public Entity getTargetEntity() {
        return target == null ? null : target.getEntity();
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "root" Location of the effect.
     *
     * This method will not return null when called from onRun. Effects
     * with invalid locations will be cancelled.
     */
    public final Location getLocation() {
        return origin == null ? null : origin.getLocation();
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "target" Location of the effect.
     *
     * Unlike getLocation, this may return null.
     */
    public final Location getTarget() {
        return target == null ? null : target.getLocation();
    }

    /**
     * Set the Location this Effect is centered on.
     */
    public void setDynamicOrigin(DynamicLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Origin Location cannot be null!");
        }
        origin = location;
        if (origin == null) return;

        if (offset != null) {
            origin.addOffset(offset);
        }
        if (relativeOffset != null) {
            origin.addRelativeOffset(relativeOffset);
        }
        origin.setDirectionOffset(yawOffset, pitchOffset);
        origin.setUpdateLocation(updateLocations);
        origin.setUpdateDirection(updateDirections);
    }

    /**
     * Set the Location this Effect is targeting.
     */
    public void setDynamicTarget(DynamicLocation location) {
        target = location;
        if (target != null && targetOffset != null) {
            target.addOffset(targetOffset);
        }
        if (target != null) {
            target.setUpdateLocation(updateLocations);
            target.setUpdateDirection(updateDirections);
        }
    }

    protected final boolean validate() {
        // Check for a valid Location
        updateLocation();
        updateTarget();
        Location location = getLocation();
        if (location == null) {
            return false;
        }
        if (autoOrient) {
            Location targetLocation = target == null ? null : target.getLocation();
            if (targetLocation != null) {
                Vector direction = targetLocation.toVector().subtract(location.toVector());
                location.setDirection(direction);
                targetLocation.setDirection(direction.multiply(-1));
            }
        }

        return true;
    }

    protected void updateDuration() {
        if (duration != null) {
            if (period < 1) {
                period = 1;
            }
            iterations = duration / period / 50;
        }
    }

    protected void updateLocation() {
        if (origin != null) {
            origin.update();
        }
    }

    protected void updateTarget() {
        if (target != null) {
            target.update();
        }
    }

    protected void display(ParticleEffect effect, Location location) {
        display(effect, location, this.color);
    }

    protected void display(ParticleEffect particle, Location location, Color color) {
        display(particle, location, color, speed, particleCount);
    }

    protected void display(ParticleEffect particle, Location location, float speed, int amount) {
        display(particle, location, this.color, speed, amount);
    }

    protected void display(ParticleEffect particle, Location location, Color color, float speed, int amount) {
        particle.display(particle.getData(material, materialData), location, color, visibleRange, particleOffsetX, particleOffsetY, particleOffsetZ, speed, amount);
    }

    private void done() {
        done = true;
        effectManager.done(this);
        onDone();
    }
}
