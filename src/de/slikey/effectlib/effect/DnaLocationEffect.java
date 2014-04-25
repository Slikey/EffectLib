package de.slikey.effectlib.effect;

import org.bukkit.Location;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class DnaLocationEffect extends LocationEffect {

	/**
	 * ParticleType of spawned particle
	 */
	public ParticleEffect particleHelix = ParticleEffect.FLAME;

	public ParticleEffect particleBase1 = ParticleEffect.SLIME;
	
	public ParticleEffect particleBase2 = ParticleEffect.SPELL;
	
	

	/**
	 * Current step. Works as counter
	 */
	protected int step = 0;

	public DnaLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 200;
	}

	@Override
	public void onRun() {

	}

}
