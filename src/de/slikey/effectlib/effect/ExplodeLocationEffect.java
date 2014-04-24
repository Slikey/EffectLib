package de.slikey.effectlib.effect;

import java.util.ArrayList;

import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleType;

public class ExplodeLocationEffect extends LocationEffect {
	
	/**
	 * Amount of spawned smoke-sparks
	 */
	public int amount = 25;
	
	/**
	 * Movement speed of smoke-sparks. Should be increases with force.
	 */
	public float speed = .5f;
	
	/**
	 * Force of the explosion
	 */
	public float force = 2;

	public ExplodeLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.INSTANT;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onRun() {
		Packet packet = new PacketPlayOutWorldParticles(ParticleType.EXPLODE.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, speed, amount);
		sendPacket(packet, location, visibleRadiusSquared);
		packet = new PacketPlayOutExplosion((float) location.getX(), (float) location.getY(), (float) location.getZ(), force, new ArrayList(), null);
		sendPacket(packet, location, visibleRadiusSquared);
	}

}
