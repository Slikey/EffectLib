package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;

public class CubeLocationEffect extends LocationEffect {

	/**
	 * Particle of the cube
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	/**
	 * Lenght of the edges
	 */
	public float edgeLenght = 3;

	/**
	 * Turns the cube by this angle each iteration around the x-axis
	 */
	public double angularVelocityX = Math.PI / 200;

	/**
	 * Turns the cube by this angle each iteration around the y-axis
	 */
	public double angularVelocityY = Math.PI / 170;

	/**
	 * Turns the cube by this angle each iteration around the z-axis
	 */
	public double angularVelocityZ = Math.PI / 155;

	/**
	 * Particles in each row
	 */
	public int particles = 8;

	/**
	 * True if rotation is enable
	 */
	public boolean enableRotation = true;

	/**
	 * Current step. Works as counter
	 */
	protected int step = 0;

	public CubeLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 5;
		iterations = 200;
	}

	@Override
	public void onRun() {
		double xRotation = 0, yRotation = 0, zRotation = 0;
		if (enableRotation) {
			xRotation = step * angularVelocityX;
			yRotation = step * angularVelocityY;
			zRotation = step * angularVelocityZ;
		}
		float a = edgeLenght / 2;
		for (int x = 0; x <= particles; x++) {
			float posX = edgeLenght * ((float) x / particles) - a;
			for (int y = 0; y <= particles; y++) {
				float posY = edgeLenght * ((float) y / particles) - a;
				for (int z = 0; z <= particles; z++) {
					if (x != 0 && x != particles && y != 0 && y != particles && z != 0 && z != particles)
						continue;
					float posZ = edgeLenght * ((float) z / particles) - a;
					Vector v = new Vector(posX, posY, posZ);
					if (enableRotation)
						VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
					particle.display(location.add(v), visibleRange);
					location.subtract(v);
				}
			}
		}
		step++;
	}
}
