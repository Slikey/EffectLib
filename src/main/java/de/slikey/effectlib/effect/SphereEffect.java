package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SphereEffect extends Effect {
    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.SPELL_MOB;

    /**
     * Radius of the sphere
     */
    public double radius = 0.6;

    /**
     * Y-Offset of the sphere
     */
    public double yOffset = 0;

    /**
     * Particles to display
     */
    public int particles = 50;

    public SphereEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        iterations = 500;
        period = 1;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, yOffset, 0);
        for (int i = 0; i < particles; i++) {
            Vector vector = RandomUtils.getRandomVector().multiply(radius);
            location.add(vector);
            display(particle, location);
            location.subtract(vector);
        }
    }

}