package de.slikey.effectlib.effect;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.RandomUtils;

public class BleedEffect extends de.slikey.effectlib.Effect {

    /**
     * Play the Hurt Effect for the Player
     */
    public boolean hurt = true;

    /**
     * Height of the blood spurt
     */
    public double height = 1.75;

    /**
     * Color of blood. Default is red (152)
     */
    public Material material = Material.REDSTONE_BLOCK;

    public BleedEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 4;
        iterations = 25;
    }

    @Override
    public void onRun() {
        // Location to spawn the blood-item.
        Location location = getLocation();
        location.add(0, RandomUtils.random.nextFloat() * height, 0);
        location.getWorld().playEffect(location, Effect.STEP_SOUND, material);

        Entity entity = getEntity();
        if (hurt && entity != null) entity.playEffect(org.bukkit.EntityEffect.HURT);
    }
}
