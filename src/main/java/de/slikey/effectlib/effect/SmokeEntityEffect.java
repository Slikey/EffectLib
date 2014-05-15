package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;

public class SmokeEntityEffect extends EntityEffect {

	public SmokeEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 300;
	}

	@Override
	public void onRun() {
		for (int i = 0; i < 20; i++) {
			Location location = entity.getLocation();
			location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
			location.add(0, RandomUtils.random.nextFloat() * 2, 0);
			ParticleEffect.SMOKE.display(location, visibleRange);
		}
	}

}
