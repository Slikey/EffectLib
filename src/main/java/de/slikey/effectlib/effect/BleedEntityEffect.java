package de.slikey.effectlib.effect;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.RandomUtils;

public class BleedEntityEffect extends EntityEffect {

    public boolean hurt = true;

	/**
	 * Duration in ticks, the blood-particles take to despawn.
	 */
	public int duration = 10;

	/**
	 * Color of blood. Default is red (152)
	 */
	public int color = 152;

	public BleedEntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager, entity);
		type = EffectType.REPEATING;
		period = 4;
		iterations = 25;
	}

	@Override
	public void onRun() {
		// Location to spawn the blood-item.
		Location spawn = entity.getLocation();
		spawn.add(0, RandomUtils.random.nextFloat() * 1.75f, 0);
		spawn.getWorld().playEffect(spawn, Effect.STEP_SOUND, 152);
        if (hurt) {
            entity.playEffect(org.bukkit.EntityEffect.HURT);
        }
	}
}
