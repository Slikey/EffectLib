package de.slikey.effectlib.effect;

import org.bukkit.entity.Player;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;

public abstract class PlayerEffect extends Effect {
	protected final Player player;

	public PlayerEffect(EffectManager effectManager, Player player) {
		super(effectManager);
		this.player = player;
	}

}
