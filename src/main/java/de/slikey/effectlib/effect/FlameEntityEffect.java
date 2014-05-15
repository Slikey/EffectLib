package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;

public class FlameEntityEffect extends EntityEffect {

	public FlameEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 600;
	}

	@Override
	public void onRun() {
		for (int i = 0; i < 10; i++) {
			Location location = entity.getLocation();
			Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d);
			location.add(v);
			ParticleEffect.FLAME.display(location, visibleRange, 0, 0, 0, 0, 0);
			location.subtract(v);
		}
	}

}
