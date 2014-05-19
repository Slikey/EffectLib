package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class WarpEntityEffect extends EntityEffect {
	public float radius = 1;
	public int particles = 20;
	public ParticleEffect particle = ParticleEffect.FIREWORKS_SPARK;
	public float grow = .2f;
	public int rings = 12;

	protected int step = 0;

	public WarpEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		period = 2;
		iterations = rings;
	}

	@Override
	public void onRun() {
		Location location = entity.getLocation();
		if (step > rings)
			step = 0;
		double x, y, z;
		y = step * grow;
		location.add(0, y, 0);
		for (int i = 0; i < particles; i++) {
			double angle = (double) 2 * Math.PI * i / particles;
			x = Math.cos(angle) * radius;
			z = Math.sin(angle) * radius;
			location.add(x, 0, z);
			particle.display(location, visibleRange);
			location.subtract(x, 0, z);
		}
		location.subtract(0, y, 0);
		step++;
	}

}
