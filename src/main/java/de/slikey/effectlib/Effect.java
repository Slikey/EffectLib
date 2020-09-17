package de.slikey.effectlib;

import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.RandomUtils;

import org.bukkit.Particle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

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
    @Deprecated
    public float speed = 0;

    /**
     * This can be used to give particles a set speed when spawned.
     * This will not work with colored particles.
     *
     * This is a replacement for "speed"
     */
    public float particleData = 0;

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
     * Probability that this effect will play on each iteration
     */
    public double probability = 1;

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
    public Float yaw = null;
    public Float pitch = null;

    /**
     * If set to false, Entity-bound locations will not update during the Effect
     */
    public boolean updateLocations = true;

    /**
     * If set to false, Entity-bound directions will not update during the Effect
     */
    public boolean updateDirections = true;

    /**
     * A specific player who should see this effect.
     */
    public Player targetPlayer;

    /**
     * A group of players who should see this effect.
     */
    public List<Player> targetPlayers;

    /**
     * The Material and data to use for block and item break particles
     */
    public Material material;
    public byte materialData;

    /**
     * These can be used to spawn multiple particles per packet.
     * It will not work with colored particles, however.
     */
    public int particleCount = 1;

    /**
     * These can be used to apply an offset to spawned particles, particularly
     * useful when spawning multiples.
     */
    public float particleOffsetX = 0;
    public float particleOffsetY = 0;
    public float particleOffsetZ = 0;

    /**
     * This can be used to scale up or down a particle's size.
     *
     * This currently only works with the redstone particle in 1.13 and up.
     */
    public float particleSize = 1;

    /**
     * If set, will run asynchronously.
     * Some effects don't support this (TurnEffect, JumpEffect)
     *
     * Generally this shouldn't be changed, unless you want to
     * make an async effect synchronous.
     */
    public boolean asynchronous = true;
    protected final EffectManager effectManager;

    protected DynamicLocation origin = null;
    protected DynamicLocation target = null;

    /**
     * This will store the base number of iterations
     */
    protected int maxIterations;

    /**
     * Should this effect stop playing if the origin entity becomes invalid?
     */
    public boolean disappearWithOriginEntity = false;
    
    /**
     * Should this effect stop playing if the target entity becomes invalid?
     */
    public boolean disappearWithTargetEntity = false;

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
        try {
            if (RandomUtils.checkProbability(probability)) {
                onRun();
            }
        } catch (Exception ex) {
            done();
            effectManager.onError(ex);
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

    /**
     * Effects should override this if they want to be reusable, this is called prior to starting so
     * state can be reset.
     */
    protected void reset() {
        this.done = false;
    }

    public void prepare() {
        reset();
        updateDuration();
    }

    public final void start() {
        prepare();
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

        if (offset != null) {
            origin.addOffset(offset);
        }
        if (relativeOffset != null) {
            origin.addRelativeOffset(relativeOffset);
        }
        origin.setDirectionOffset(yawOffset, pitchOffset);
        origin.setYaw(yaw);
        origin.setPitch(pitch);
        origin.setUpdateLocation(updateLocations);
        origin.setUpdateDirection(updateDirections);
        origin.updateDirection();
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
        // Check if the origin and target entities are present
        if (disappearWithOriginEntity && (origin != null && !origin.hasValidEntity())) {
            return false;
        }
        
        if (disappearWithTargetEntity && (target != null && !target.hasValidEntity())) {
            return false;
        }
        
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
        maxIterations = iterations;
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

    protected void display(Particle effect, Location location) {
        display(effect, location, this.color);
    }

    protected void display(Particle particle, Location location, Color color) {
        display(particle, location, color, particleData != 0 ? particleData : speed, particleCount);
    }

    protected void display(Particle particle, Location location, float speed, int amount) {
        display(particle, location, this.color, speed, amount);
    }

    protected void display(Particle particle, Location location, Color color, float speed, int amount) {
        if (targetPlayers == null && targetPlayer != null) {
            targetPlayers = new ArrayList<Player>();
            targetPlayers.add(targetPlayer);
        }
        effectManager.display(particle, location, particleOffsetX, particleOffsetY, particleOffsetZ, speed, amount,
                particleSize, color, material, materialData, visibleRange, targetPlayers);
    }

    private void done() {
        done = true;
        effectManager.done(this);
        onDone();
    }

    public EffectType getType() {
        return type;
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public int getDelay() {
        return delay;
    }

    public int getPeriod() {
        return period;
    }
    
    public void setEntity(Entity entity) {
        setDynamicOrigin(new DynamicLocation(entity));
    }

    public void setLocation(Location location) {
        setDynamicOrigin(new DynamicLocation(location));
    }

    public void setTargetEntity(Entity entity) {
        target = new DynamicLocation(entity);
    }

    public void setTargetLocation(Location location) {
        target = new DynamicLocation(location);
    }

    public Player getTargetPlayer() {return this.targetPlayer; }
    public void setTargetPlayer(Player p){ this.targetPlayer = p; }
}
