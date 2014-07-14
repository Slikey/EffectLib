package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class SphereEntityEffect extends EntityEffect {
	/**
	 * ParticleType of spawned particle
	 */
	public ParticleEffect particle = ParticleEffect.MOB_SPELL;

	/**
	 * Radius of the sphere
	 */
	public double radius = 0.6;

    /**
     * Y-Offset of the sphere
     */
    public double yOffset = 1.5;

	/**
	 * Particles to display
	 */
	public int particles = 50;

	public SphereEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		iterations = 500;
		period = 1;
	}

	@Override
	public void onRun() {
		for (int i = 0; i < particles; i++) {
            Location location = entity.getLocation();
            location.add(0, yOffset, 0);
            location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * radius));
            particle.display(location, visibleRange);
		}
	}

}
