package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class LineLocationEffect extends LocationEffect {

	/**
	 * ParticleType of spawned particle
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	/**
	 * Particles per arc
	 */
	public int particles = 100;

	protected final Vector link;
	protected final float lenght;

	public LineLocationEffect(EffectManager effectManager, Location start, Location stop) {
		super(effectManager, start);
		link = stop.toVector().subtract(start.toVector());
		lenght = (float) link.length();
		link.normalize();

		type = EffectType.INSTANT;
		period = 5;
		iterations = 200;
	}

	@Override
	public void onRun() {
		for (int i = 0; i < particles; i++) {
			float ratio = (float) i * lenght / particles;
			Vector v = link.clone().multiply(ratio);
			location.add(v);
			particle.display(location, visibleRange);
			location.subtract(v);
		}
	}

}
