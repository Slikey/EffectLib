package de.slikey.effectlib.effect;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.RandomUtils;

public class SquareEffect extends Effect {

	// Type of the spawned particles
	public Particle particle = Particle.SPELL_MOB;

	// Radius of the square
	public double radius = 0.6;

	// y-offset of the square
	public double yOffset = 0;

	// Amount of particles to display
	public int particles = 50;

	// Amount to increase the radius per tick
	public double radiusIncrease = 0;

	// Amount to increase the particles per tick
	public int particleIncrease = 0;

	public SquareEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.REPEATING;
		iterations = 500;
		period = 1;
	}

	@Override
	public void onRun() {
		if (radiusIncrease != 0) radius += radiusIncrease;
		if (particleIncrease != 0) particles += particleIncrease;

		Location location = getLocation();
		location.add(0, yOffset, 0);
		for (int i = 0; i < particles; i++) {
			Vector vector = RandomUtils.getRandomFlatVector().multiply(radius);
			location.add(vector);
			display(particle, location);
			location.subtract(vector);
		}
		location.subtract(0, yOffset, 0);
	}

}
