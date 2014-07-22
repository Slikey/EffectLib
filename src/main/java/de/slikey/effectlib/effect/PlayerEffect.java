package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import org.bukkit.entity.Player;

public abstract class PlayerEffect extends Effect {
    /**
     * Player affected by the Effect
     */
    protected final Player player;

    public PlayerEffect(EffectManager effectManager, Player player) {
        super(effectManager);
        this.player = player;
    }

}
