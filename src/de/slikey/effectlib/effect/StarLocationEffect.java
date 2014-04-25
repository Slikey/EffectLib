package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class StarLocationEffect extends LocationEffect {

	public ParticleEffect particle = ParticleEffect.FLAME;
	public int particles = 100;
	public float spikeHeight = 4;
	public int spikesHalf = 3;
	public float innerRadius = 1;

	public StarLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 4;
		iterations = 50;
	}

	@Override
	public void onRun() {
		float radius = 3 * innerRadius / MathUtils.SQRT_3;
		for (int j = 0; j < 2; j++) {
			double yRotation = j * Math.PI / 2;
			for (int i = 0; i < spikesHalf * 2; i++) {
				double xRotation = i * Math.PI / spikesHalf + j * Math.PI;
				for (int x = 0; x < particles; x++) {
					double angle = 2 * Math.PI * x / particles;
					float height = RandomUtils.random.nextFloat() * spikeHeight;
					Vector v = new Vector(Math.cos(angle), 0, Math.sin(angle));
					v.multiply((spikeHeight - height) * radius / spikeHeight);
					v.setY(innerRadius + height);
					VectorUtils.rotateAroundAxisX(v, xRotation);
					VectorUtils.rotateAroundAxisY(v, yRotation);
					location.add(v);
					particle.display(location, visibleRange);
					location.subtract(v);
				}
			}

		}
	}

}
