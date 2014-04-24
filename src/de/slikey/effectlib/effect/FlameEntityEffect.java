package de.slikey.effectlib.effect;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleType;
import de.slikey.effectlib.util.RandomUtils;

public class FlameEntityEffect extends EntityEffect {

	public FlameEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 600;
	}

	@Override
	public void onRun() {
		for (int i = 0; i < 10; i++) {
			Location location = entity.getLocation();
			location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
			location.add(0, RandomUtils.random.nextFloat() * 2, 0);
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(ParticleType.FLAME.getParticleName(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 0);
			sendPacket(packet, location, visibleRadiusSquared);
		}
	}

}
