package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@Deprecated
public class CylinderLocationEffect extends LocationEffect {

	/**
	 * Particle of the cube
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	/**
	 * Radius of cylinder
	 */
	public float radius = 1;

    /**
     * Height of Cylinder
     */
    public float height = 3;

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
     * Rotation of the cylinder
     */
    public double rotationX, rotationY, rotationZ;

	/**
	 * Particles in each row
	 */
	public int particles = 50;

	/**
	 * True if rotation is enable
	 */
	public boolean enableRotation = true;

	/**
	 * Current step. Works as counter
	 */
	protected int step = 0;

    protected float sideRatio = 0;

	public CylinderLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 5;
		iterations = 200;
	}

	@Override
	public void onRun() {
        if (sideRatio == 0) calculateSideRatio();
        for (int i = 0; i < particles; i++) {

        }
        particle.display(location, visibleRange);
		step++;
	}

    protected void calculateSideRatio() {

    }
}
