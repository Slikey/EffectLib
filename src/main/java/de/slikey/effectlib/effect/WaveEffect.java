package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;

public class WaveEffect extends Effect {
    public ParticleEffect particle = ParticleEffect.DRIP_WATER;
    public ParticleEffect cloudParticle = ParticleEffect.CLOUD;
    public Color cloudColor = null;

    /**
     * Velocity of the wave
     * Call velocity.zero() if the wave should be stationary
     */
    public Vector velocity = new Vector();

    /**
     * Caches the Vectors used to build the wave
     */
    protected final Collection<Vector> waterCache, cloudCache;

    /**
     * Amount of particles forming the tube
     */
    public int particlesFront = 10;

    /**
     * Amount of particles forming the back
     */
    public int particlesBack = 10;

    /**
     * Rows to build the wave in the width
     */
    public int rows = 20;

    /**
     * The distance from the origin location to the first point of the wave
     */
    public float lengthFront = 1.5f;

    /**
     * The distance from the origin location to the last point of the wave
     */
    public float lengthBack = 3;

    /**
     * Depth of the parabola tube
     */
    public float depthFront = 1;

    /**
     * Height of the parabola arc forming the back
     */
    public float heightBack = .5f;

    /**
     * Height of the wave in blocks
     */
    public float height = 2;

    /**
     * Width of the wave in blocks
     */
    public float width = 5;

    /**
     * Do not mess with the following attributes. They build a cache to gain performance.
     */
    protected boolean firstStep = true;

    public WaveEffect(EffectManager effectManager) {
        super(effectManager);

        type = EffectType.REPEATING;
        period = 5;
        iterations = 50;
        waterCache = new HashSet<Vector>();
        cloudCache = new HashSet<Vector>();
    }

    /**
     * Call this method when you change anything related to the creation of the wave
     */
    public void invalidate(Location location) {
        firstStep = false;
        waterCache.clear();
        cloudCache.clear();

        Vector s1 = new Vector(-lengthFront, 0, 0);
        Vector s2 = new Vector(lengthBack, 0, 0);
        Vector h = new Vector(-0.5 * lengthFront, height, 0);

        Vector n1, n2, n_s1ToH, n_s2ToH, c1, c2, s1ToH, s2ToH;
        float len_s1ToH, len_s2ToH, yaw;

        s1ToH = h.clone().subtract(s1);
        c1 = s1.clone().add(s1ToH.clone().multiply(0.5));
        len_s1ToH = (float) s1ToH.length();
        n_s1ToH = s1ToH.clone().multiply(1f / len_s1ToH);
        n1 = new Vector(s1ToH.getY(), -s1ToH.getX(), 0).normalize();
        if (n1.getX() < 0) n1.multiply(-1);

        s2ToH = h.clone().subtract(s2);
        c2 = s2.clone().add(s2ToH.clone().multiply(0.5));
        len_s2ToH = (float) s2ToH.length();
        n_s2ToH = s2ToH.clone().multiply(1f / len_s2ToH);
        n2 = new Vector(s2ToH.getY(), -s2ToH.getX(), 0).normalize();
        if (n2.getX() < 0) n2.multiply(-1);

        yaw = (-location.getYaw() + 90) * MathUtils.degreesToRadians;

        for (int i = 0; i < particlesFront; i++) {
            float ratio = (float) i / particlesFront;
            float x = (ratio - .5f) * len_s1ToH;
            float y = (float) (-depthFront / Math.pow((len_s1ToH / 2), 2) * Math.pow(x, 2) + depthFront);
            Vector v = c1.clone();
            v.add(n_s1ToH.clone().multiply(x));
            v.add(n1.clone().multiply(y));
            for (int j = 0; j < rows; j++) {
                float z = ((float) j / rows - .5f) * width;
                Vector vec = v.clone().setZ(v.getZ() + z);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                if (i == 0 || i == particlesFront - 1) cloudCache.add(vec);
                else waterCache.add(vec);
            }
        }
        for (int i = 0; i < particlesBack; i++) {
            float ratio = (float) i / particlesBack;
            float x = (ratio - .5f) * len_s2ToH;
            float y = (float) (-heightBack / Math.pow((len_s2ToH / 2), 2) * Math.pow(x, 2) + heightBack);
            Vector v = c2.clone();
            v.add(n_s2ToH.clone().multiply(x));
            v.add(n2.clone().multiply(y));
            for (int j = 0; j < rows; j++) {
                float z = ((float) j / rows - .5f) * width;
                Vector vec = v.clone().setZ(v.getZ() + z);
                VectorUtils.rotateAroundAxisY(vec, yaw);
                if (i == particlesFront - 1) cloudCache.add(vec);
                else waterCache.add(vec);
            }
        }
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        if (firstStep) {
            velocity.copy(location.getDirection().setY(0).normalize().multiply(0.2));
            invalidate(location);
        }
        location.add(velocity);

        for (Vector v : cloudCache) {
            location.add(v);
            display(cloudParticle, location, cloudColor, 0, 1);
            location.subtract(v);
        }
        for (Vector v : waterCache) {
            location.add(v);
            display(particle, location);
            location.subtract(v);
        }
    }
}
