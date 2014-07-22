package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TurnPlayerEffect extends PlayerEffect {

    /**
     * Angular movement per iteration
     */
    public float step = 11.25f;

    public TurnPlayerEffect(EffectManager effectManager, Player player) {
        super(effectManager, player);
        type = EffectType.REPEATING;
        period = 1;
        iterations = (int) (360 * 5 / step);
    }

    @Override
    public void onRun() {
        Location loc = player.getLocation();
        loc.setYaw(loc.getYaw() + step);
        player.teleport(loc);
    }

}
