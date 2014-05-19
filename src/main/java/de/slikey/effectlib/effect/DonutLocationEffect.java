package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;

/**
 * Taken from http://en.wikipedia.org/wiki/Torus
 * 
 * @author Kevin
 * 
 */
public class DonutLocationEffect extends LocationEffect {

	/**
	 * ParticleType of spawned particle
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	public int particlesCircle = 10;
	public int circles = 36;
	public float radiusDonut = 2;
	public float radiusTube = .5f;
	
	public double xRotation, yRotation, zRotation = 0;

	public DonutLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 10;
		iterations = 20;
	}

	@Override
	public void onRun() {
		Vector v = new Vector();
		for (int i = 0; i < circles; i++) {
			double theta = 2 * Math.PI * i / circles;
			for (int j = 0; j < particlesCircle; j++) {
				double phi = 2 * Math.PI * j / particlesCircle;
				double cosPhi = Math.cos(phi);
				v.setX((radiusDonut + radiusTube * cosPhi) * Math.cos(theta));
				v.setY((radiusDonut + radiusTube * cosPhi) * Math.sin(theta));
				v.setZ(radiusTube * Math.sin(phi));
				
				VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
				
				particle.display(location.add(v), visibleRange, 0, 0, 0, 0, 0);
				location.subtract(v);
			}
		}
	}
}
