package de.slikey.effectlib.effect;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleType;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class AtomLocationEffect extends LocationEffect {

	/**
	 * ParticleType of the nucleus
	 */
	public ParticleType particleNucleus = ParticleType.DROP_WATER;
	
	/**
	 * ParticleType of orbitals
	 */
	public ParticleType particleOrbital = ParticleType.DROP_LAVA;
	
	/**
	 * Radius of the atom
	 */
	public int radius = 3;
	
	/**
	 * Radius of the nucleus as a fraction of the atom-radius
	 */
	public float radiusNucleus = .2f;
	
	/**
	 * Particles to be spawned in the nucleus per iteration
	 */
	public int particlesNucleus = 10;
	
	/**
	 * Particles to be spawned per orbital per iteration
	 */
	public int particlesOrbital = 10;
	
	/**
	 * Orbitals around the nucleus
	 */
	public int orbitals = 3;
	
	/**
	 * Rotation around the Y-axis
	 */
	public double rotation = 0;
	
	/**
	 * Velocity of the orbitals
	 */
	public double angularVelocity = Math.PI / 80d;
	
	protected int step = 0;
	
	public AtomLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 2;
		iterations = 200;
	}

	@Override
	public void onRun() {
		for (int i = 0; i < particlesNucleus; i++) {
			Vector v = RandomUtils.getRandomVector().multiply(radius * radiusNucleus);
			location.add(v);
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particleNucleus.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 0);
			sendPacket(packet, location, visibleRadiusSquared);
			location.subtract(v);
		}
		for (int i = 0; i < particlesOrbital; i++) {
			double angle = step * angularVelocity;
			for (int j = 0; j < orbitals; j++) {
				double xRotation = (Math.PI / orbitals) * j;
				Vector v = new Vector(Math.cos(angle), Math.sin(angle), 0).multiply(radius);
				VectorUtils.rotateAroundAxisX(v, xRotation);
				VectorUtils.rotateAroundAxisY(v, rotation);
				location.add(v);
				PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particleOrbital.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 0);
				sendPacket(packet, location, visibleRadiusSquared);
				location.subtract(v);
			}
			step++;
		}
	}

}
