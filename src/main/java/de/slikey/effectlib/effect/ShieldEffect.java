package de.slikey.effectlib.effect;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.RandomUtils;

public class ShieldEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.FLAME;

    /**
     * Radius of the shield
     */
    public double radius = 3;

    /**
     * Particles to display
     */
    public int particles = 50;

    /**
     * Set to false for a half-sphere and true for a complete sphere
     */
    public boolean sphere = false;

    // Set to true to reverse the direction of the shield (works only if sphere is set to false)
    public boolean reverse = false;

    public ShieldEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = 500;
        period = 1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int i = 0; i < particles; i++) {
            Vector vector = RandomUtils.getRandomVector().multiply(radius);
            if (!sphere) {
                if (reverse) vector.setY(Math.abs(vector.getY()) * -1);
                else vector.setY(Math.abs(vector.getY()));
            }
            location.add(vector);
            display(particle, location);
            location.subtract(vector);
        }
    }

}
