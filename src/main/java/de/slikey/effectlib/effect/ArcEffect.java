package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ArcEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Height of the arc in blocks
     */
    public float height = 2;

    /**
     * Particles per arc
     */
    public int particles = 100;
    
    public Vector relativeOffsetFromOriginToTarget = null;
    public Vector absoluteOffsetFromOriginToTarget = null;

    protected DynamicLocation targetOffseter = null;
    
    /**
     * Internal counter
     */
    protected int step = 0;

    public ArcEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 200;
    }

    @Override
    public void reset() {
        this.step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        
        if (step == 0 && (relativeOffsetFromOriginToTarget != null || absoluteOffsetFromOriginToTarget != null)) {
            targetOffseter = new DynamicLocation(location);
            if (relativeOffsetFromOriginToTarget != null) {
                targetOffseter.addRelativeOffset(relativeOffsetFromOriginToTarget);
            }
            if (absoluteOffsetFromOriginToTarget != null) {
                targetOffseter.addOffset(absoluteOffsetFromOriginToTarget);
            }
        }
        
        Location target = getTarget();
        if (target == null) {
            if (targetOffseter == null) {
                cancel();
                return;
            }
            targetOffseter.updateFrom(location);
            target = targetOffseter.getLocation();
        }
        
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        float pitch = (float) (4 * height / Math.pow(length, 2));
        for (int i = 0; i < particles; i++) {
            Vector v = link.clone().normalize().multiply((float) length * i / particles);
            float x = ((float) i / particles) * length - length / 2;
            float y = (float) (-pitch * Math.pow(x, 2) + height);
            location.add(v).add(0, y, 0);
            display(particle, location);
            location.subtract(0, y, 0).subtract(v);

            step++;
        }
    }

}
