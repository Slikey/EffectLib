package de.slikey.effectlib.effect;

import org.bukkit.Location;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class ExplodeLocationEffect extends LocationEffect {

	/**
	 * Amount of spawned smoke-sparks
	 */
	public int amount = 25;

	/**
	 * Movement speed of smoke-sparks. Should be increases with force.
	 */
	public float speed = .5f;

	public ExplodeLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.INSTANT;
	}

	@Override
	public void onRun() {
		ParticleEffect.EXPLODE.display(location, visibleRange, 0, 0, 0, speed, amount);
		ParticleEffect.HUGE_EXPLOSION.display(location, visibleRange, 0, 0, 0, 0, amount);
	}

}
