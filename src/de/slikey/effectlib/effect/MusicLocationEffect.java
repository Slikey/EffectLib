package de.slikey.effectlib.effect;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleType;

public class MusicLocationEffect extends LocationEffect {
	
	/**
	 * Radials to spawn next note.
	 */
	public double radialsPerStep = Math.PI / 8;
	
	/**
	 * Radius of circle above head
	 */
	public float radius = .4f;
	
	/**
	 * Current step. Works as a counter
	 */
	protected float step = 0;

	public MusicLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		iterations = 500;
		period = 4;
	}

	@Override
	public void onRun() {
		Location location = this.location.clone();
		location.add(Math.cos(radialsPerStep * step) * radius, .2f, Math.sin(radialsPerStep * step) * radius);
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(ParticleType.NOTE.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, .5f, 1);
		sendPacket(packet, location, visibleRadiusSquared);
		step++;
	}

}
