package de.slikey.effectlib.effect;

import org.bukkit.Location;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;

public abstract class LocationEffect extends Effect {
	/**
	 * Location of effect.
	 */
	protected final Location location;

	public LocationEffect(EffectManager effectManager, Location location) {
		super(effectManager);
		this.location = location;
	}

}
