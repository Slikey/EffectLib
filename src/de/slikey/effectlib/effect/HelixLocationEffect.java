package de.slikey.effectlib.effect;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleType;

public class HelixLocationEffect extends LocationEffect {
	/**
	 * Particle to form the helix
	 */
	public ParticleType particle = ParticleType.FLAME;

	/**
	 * Amount of strands
	 */
	public int strands = 8;

	/**
	 * Particles per strand
	 */
	public int particles = 80;

	/**
	 * Radius of helix
	 */
	public float radius = 10;

	/**
	 * Factor for the curves. Negative values reverse rotation.
	 */
	public float curve = 10;
	
	/**
	 * Rotation of the helix (Fraction of PI)
	 */
	public double rotation = Math.PI / 4;

	public HelixLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 10;
		iterations = 8;
	}

	@Override
	public void onRun() {
		for (int i = 1; i <= strands; i++) {
			for (int j = 1; j <= particles; j++) {
				float ratio = (float) j / particles;
				double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
				double x = Math.cos(angle) * ratio * radius;
				double z = Math.sin(angle) * ratio * radius;
				location.add(x, 0, z);

				PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 0);
				sendPacket(packet, location, visibleRadiusSquared);
				
				// Subtracting x and z to get to the first location again.
				location.subtract(x, 0, z);
			}
		}
	}

}
