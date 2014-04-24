package de.slikey.effectlib.effect;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleType;
import de.slikey.effectlib.util.VectorUtils;

public class VortexLocationEffect extends LocationEffect {

	/**
	 * ParticleType of spawned particle
	 */
	public ParticleType particle = ParticleType.FLAME;

	/**
	 * Radius of vortex (2)
	 */
	public float radius = 2;

	/**
	 * Growing per iteration (0.05)
	 */
	public float grow = .05f;

	/**
	 * Radials per iteration (PI / 16)
	 */
	public double radials = Math.PI / 16;

	/**
	 * Helix-circles per interation (3)
	 */
	public int circles = 3;

	/**
	 * Amount of helixes (4)
	 */
	public int helixes = 4;

	/**
	 * Current step. Works as counter
	 */
	protected int step = 0;

	public VortexLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 200;
	}

	@Override
	public void onRun() {
		for (int x = 0; x < circles; x++) {
			for (int i = 0; i < helixes; i++) {
				double angle = step * radials + (2 * Math.PI * i / helixes);
				Vector v = new Vector(Math.cos(angle) * radius, step * grow, Math.sin(angle) * radius);
				VectorUtils.rotateAroundAxisX(v, (location.getPitch() + 90) * MathUtils.degreesToRadians);
				VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
				
				location.add(v);
				PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 0);
				sendPacket(packet, location, visibleRadiusSquared);
				location.subtract(v);
			}
			step++;
		}
	}

}
