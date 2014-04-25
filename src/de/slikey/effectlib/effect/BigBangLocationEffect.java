package de.slikey.effectlib.effect;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.FireworkMeta;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;

public class BigBangLocationEffect extends LocationEffect {

	public BigBangLocationEffect(EffectManager effectManager, Location location) {
		super(effectManager, location);
		type = EffectType.REPEATING;
		period = 40;
		iterations = 20;
	}

	@Override
	public void onRun() {
		Builder b = FireworkEffect.builder();
		b.withColor(Color.RED).withColor(Color.ORANGE).withColor(Color.BLACK);
		b.withFade(Color.BLACK);
		b.trail(true);
		FireworkEffect fe = b.build();

		final Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		fw.playEffect(org.bukkit.EntityEffect.WOLF_HEARTS);
		FireworkMeta meta = fw.getFireworkMeta();
		meta.setPower(10);
		meta.addEffect(fe);
		fw.setFireworkMeta(meta);
		
		fw.detonate();
	}

}
