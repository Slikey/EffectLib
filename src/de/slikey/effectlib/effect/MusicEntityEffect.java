package de.slikey.effectlib.effect;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleType;

public class MusicEntityEffect extends EntityEffect {
	
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

	public MusicEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		iterations = 400;
		period = 1;
	}

	@Override
	public void onRun() {
		Location location = entity.getLocation();
		location.add(0, 1.9f, 0);
		location.add(Math.cos(radialsPerStep * step) * radius, 0, Math.sin(radialsPerStep * step) * radius);
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(ParticleType.NOTE.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, .5f, 1);
		sendPacket(packet, location, visibleRadiusSquared);
		step++;
	}

}
