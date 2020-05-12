package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;

public class TurnEffect extends Effect {

    /**
     * Angular movement per iteration
     */
    public float step = 11.25f;

    public TurnEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = (int) (360 * 5 / step);
        asynchronous = false;
    }

    @Override
    public void onRun() {
        Entity entity = getEntity();
        if (entity == null) {
            cancel();
            return;
        }
        Location loc = entity.getLocation();
        loc.setYaw(loc.getYaw() + step);
        entity.teleport(loc);
    }

}
