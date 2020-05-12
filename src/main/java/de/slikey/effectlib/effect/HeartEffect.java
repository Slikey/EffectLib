package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.VectorUtils;

/**
 * Creates a 2D Heart in 3D space. Thanks to the author for sharing it!
 *
 * @author <a href="http://forums.bukkit.org/members/qukie.90952701/">Qukie</a>
 */
public class HeartEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.CRIT_MAGIC;

    /**
     * Heart-particles per interation (100)
     */
    public int particles = 50;

    /**
     * Rotation of the heart.
     */
    public double xRotation, yRotation, zRotation = 0;

    /**
     * Stretch/Compress factor along the x or y axis (1, 1)
     */
    public double yFactor = 1, xFactor = 1;

    /**
     * Defines the size of the that inner sting (0.8) \/
     */
    public double factorInnerSpike = 0.8;

    /**
     * Compresses the heart along the y axis. (2)
     */
    public double compressYFactorTotal = 2;

    /**
     * Compilation of the heart. (2)
     */
    public float compilation = 2F;

    public HeartEffect(EffectManager effectManager) {
        super(effectManager);
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Vector vector = new Vector();
        for (int i = 0; i < particles; i++) {
            float alpha = ((MathUtils.PI / compilation) / particles) * i;
            double phi = Math.pow(Math.abs(MathUtils.sin(2 * compilation * alpha)) + factorInnerSpike * Math.abs(MathUtils.sin(compilation * alpha)), 1 / compressYFactorTotal);

            vector.setY(phi * (MathUtils.sin(alpha) + MathUtils.cos(alpha)) * yFactor);
            vector.setZ(phi * (MathUtils.cos(alpha) - MathUtils.sin(alpha)) * xFactor);

            VectorUtils.rotateVector(vector, xRotation, yRotation, zRotation);

            display(particle, location.add(vector));
            location.subtract(vector);
        }
    }

}
