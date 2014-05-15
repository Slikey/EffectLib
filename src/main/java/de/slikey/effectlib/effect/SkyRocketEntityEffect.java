package de.slikey.effectlib.effect;

import org.bukkit.entity.Entity;

import de.slikey.effectlib.EffectManager;

public class SkyRocketEntityEffect extends JumpEntityEffect {

	public SkyRocketEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		power = 10;
	}

}
