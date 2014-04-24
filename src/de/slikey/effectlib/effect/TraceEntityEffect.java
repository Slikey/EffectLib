package de.slikey.effectlib.effect;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleType;

public class TraceEntityEffect extends EntityEffect {

	/**
	 * Particle to spawn
	 */
	public ParticleType particle = ParticleType.FLAME;
	
	/**
	 * Interations to wait before refreshing particles
	 */
	public int refresh = 20;
	
	/**
	 * World of the trace
	 */
	private World world;

	/**
	 * Waypoints of the trace
	 */
	protected List<Vector> waypoints;

	protected int step = 0;

	public TraceEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 600;
		waypoints = new ArrayList<Vector>();
	}

	@Override
	public void onRun() {
		if (world == null) {
			world = entity.getWorld();
		} else if (entity.getWorld() != world) {
			cancel(true);
			return;
		}
		waypoints.add(entity.getLocation().toVector());
		step++;
		if (step % refresh != 0)
			return;
		for (int i = 0; i < waypoints.size(); i++) {
			Vector position = waypoints.get(i);
			Location location = new Location(world, position.getX(), position.getY(), position.getZ());
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 0);
			sendPacket(packet, location, visibleRadiusSquared);
		}
	}

}
