package de.slikey.effectlib.effect;

import java.util.Set;
import java.util.HashSet;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class EarthEffect extends Effect {

    public Particle particleLand = Particle.VILLAGER_HAPPY;
    public Particle particleOcean = Particle.DRIP_WATER;

    public Color colorLand = null;
    public Color colorOcean = null;

    public int particlesLand = 3;
    public int particlesOcean = 1;

    public float speedLand = 0F;
    public float speedOcean = 0F;

    /**
     * Precision of generation. Higher numbers have better results, but increase the time of generation. Don't pick Number above 10.000
     */
    public int precision = 100;

    /**
     * Amount of Particles to form the World
     */
    public int particles = 500;

    /**
     * Radius of the World
     */
    public float radius = 1;

    /**
     * Height of the mountains.
     */
    public float mountainHeight = .5f;

    /**
     * Triggers invalidation on first run
     */
    protected boolean firstStep = true;

    /**
     * Caches vectors to increase performance
     */
    protected final Set<Vector> cacheGreen, cacheBlue;

    public EarthEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = 200;
        cacheGreen = new HashSet<>();
        cacheBlue = new HashSet<>();
    }

    @Override
    public void reset() {
        firstStep = true;
    }

    public void invalidate() {
        firstStep = false;
        cacheGreen.clear();
        cacheBlue.clear();

        Set<Vector> cache = new HashSet<>();
        int sqrtParticles = (int) Math.sqrt(particles);
        float theta = 0, phi, thetaStep = MathUtils.PI / sqrtParticles, phiStep = MathUtils.PI2 / sqrtParticles;
        for (int i = 0; i < sqrtParticles; i++) {
            theta += thetaStep;
            phi = 0;
            for (int j = 0; j < sqrtParticles; j++) {
                phi += phiStep;
                float x = radius * MathUtils.sin(theta) * MathUtils.cos(phi);
                float y = radius * MathUtils.sin(theta) * MathUtils.sin(phi);
                float z = radius * MathUtils.cos(theta);
                cache.add(new Vector(x, y, z));
            }
        }

        float increase = mountainHeight / precision;
        for (int i = 0; i < precision; i++) {
            double r1 = RandomUtils.getRandomAngle(), r2 = RandomUtils.getRandomAngle(), r3 = RandomUtils.getRandomAngle();
            for (Vector v : cache) {
                if (v.getY() > 0) {
                    v.setY(v.getY() + increase);
                } else {
                    v.setY(v.getY() - increase);
                }
                if (i != precision - 1) {
                    VectorUtils.rotateVector(v, r1, r2, r3);
                }
            }
        }

        float minSquared = Float.POSITIVE_INFINITY, maxSquared = Float.NEGATIVE_INFINITY;
        for (Vector current : cache) {
            float lengthSquared = (float) current.lengthSquared();

            if (minSquared > lengthSquared) minSquared = lengthSquared;
            if (maxSquared < lengthSquared) maxSquared = lengthSquared;
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
        Location location = getLocation();
        if (firstStep) invalidate();

        for (Vector v : cacheGreen) {
            display(particleLand, location.add(v), colorLand, speedLand, particlesLand);
            location.subtract(v);
        }
        for (Vector v : cacheBlue) {
            display(particleOcean, location.add(v), colorOcean, speedOcean, particlesOcean);
            location.subtract(v);
        }
    }

}
