package de.slikey.effectlib.effect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

public class TraceEntityEffect extends EntityEffect {

	/**
	 * Particle to spawn
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	/**
	 * Interations to wait before refreshing particles
	 */
	public int refresh = 5;

    /**
     * Maximum amount of way points
     */
    public int maxWayPoints = 30;

	/**
	 * World of the trace
	 */
	private World world;

	/**
	 * Waypoints of the trace
	 */
	protected List<Vector> wayPoints;

	protected int step = 0;

	public TraceEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 600;
        wayPoints = new ArrayList<Vector>();
	}

	@Override
	public void onRun() {
		if (world == null) {
			world = entity.getWorld();
		} else if (entity.getWorld() != world) {
			cancel(true);
			return;
		}
        if (wayPoints.size() >= maxWayPoints)
            wayPoints.remove(0);
        wayPoints.add(entity.getLocation().toVector());
		step++;
		if (step % refresh != 0)
			return;
		for (int i = 0; i < wayPoints.size(); i++) {
			Vector position = wayPoints.get(i);
			Location location = new Location(world, position.getX(), position.getY(), position.getZ());
			particle.display(location, visibleRange);
		}
	}

}
