package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class StarLocationEffect extends LocationEffect {

	public ParticleEffect particle = ParticleEffect.FLAME;
	public int particles = 100;
	public float spitzenHeight = 4;

	public StarLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 4;
		iterations = 50;
	}

	@Override
	public void onRun() {
		for (int j = 0; j < 2; j++) {
			double yRotation = j * (Math.PI / 2);
			for (int k = 0; k < 2; k++) {
				for (int i = 0; i < 3; i++) {
					double xRotation = i * 2 * Math.PI / 3 + j * Math.PI + k * Math.PI;
					float innerRadius = 1;
					float radius = 3 * innerRadius / 1.73205f;
					for (int x = 0; x < particles; x++) {
						double angle = 2 * Math.PI * x / particles;
						float height = RandomUtils.random.nextFloat() * spitzenHeight;
						Vector v = new Vector(Math.cos(angle), 0, Math.sin(angle)).multiply((spitzenHeight - height) * radius / spitzenHeight);
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

}
