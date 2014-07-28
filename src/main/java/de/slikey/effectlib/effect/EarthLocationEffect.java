package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class EarthLocationEffect extends LocationEffect {

    public int precision = 1000;

    public int particles = 500;

    public float radius = 1;

    public float mountainHeight = .5f;

    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    protected boolean firstStep = true;

    protected final Set<Vector> cacheGreen, cacheBlue;

    public EarthLocationEffect(EffectManager effectManager, Location location) {
        super(effectManager, location);
        type = EffectType.REPEATING;
        period = 5;
        iterations = 200;
        cacheGreen = new HashSet<Vector>();
        cacheBlue = new HashSet<Vector>();
    }

    public void invalidate() {
        firstStep = false;
        cacheGreen.clear();
        cacheBlue.clear();

        Set<Vector> cache = new HashSet<Vector>();
        for (int i = 0; i < particles; i++)
            cache.add(RandomUtils.getRandomVector().multiply(radius));

        float increase = mountainHeight / precision;
        for (int i = 0; i < precision; i++) {
            double r1 = RandomUtils.getRandomAngle(), r2 = RandomUtils.getRandomAngle(), r3 = RandomUtils.getRandomAngle();
            for (Vector v : cache) {
                if (v.getY() > 0) {
                    v.setY(v.getY() + increase);
                } else {
                    v.setY(v.getY() - increase);
                }
                if (i != precision - 1)
                    VectorUtils.rotateVector(v, r1, r2, r3);
            }
        }

        float minSquared = Float.POSITIVE_INFINITY, maxSquared = Float.NEGATIVE_INFINITY;
        for (Vector current : cache) {
            float lengthSquared = (float) current.lengthSquared();
            if (minSquared > lengthSquared)
                minSquared = lengthSquared;
            if (maxSquared < lengthSquared)
                maxSquared = lengthSquared;
        }

        // COLOR PARTICLES
        float average = (minSquared + maxSquared) / 2;
        for (Vector v : cache) {
            float lengthSquared = (float) v.lengthSquared();
            if (lengthSquared >= average) cacheGreen.add(v);
            else cacheBlue.add(v);
        }
    }

    @Override
    public void onRun() {
        if (firstStep) invalidate();
        for (Vector v : cacheGreen) {
            ParticleEffect.HAPPY_VILLAGER.display(location.add(v), visibleRange, 0, 0, 0, 0, 3);
            location.subtract(v);
        }
        for (Vector v : cacheBlue) {
            ParticleEffect.DRIP_WATER.display(location.add(v), visibleRange, 0, 0, 0, 0, 1);
            location.subtract(v);
        }
    }
}