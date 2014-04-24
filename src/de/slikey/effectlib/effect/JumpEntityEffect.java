package de.slikey.effectlib.effect;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;

public class JumpEntityEffect extends EntityEffect {
	/**
	 * Power of jump. (0.5f)
	 */
	public float power = .5f;
	
	public JumpEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.INSTANT;
		period = 20;
		iterations = 1;
	}
	
	@Override
	public void onRun() {
		Vector v = entity.getVelocity();
		v.setY(v.getY() + power);
		entity.setVelocity(v);
	}

}
