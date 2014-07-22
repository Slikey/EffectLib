package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import org.bukkit.entity.Entity;

public class SkyRocketEntityEffect extends JumpEntityEffect {

    public SkyRocketEntityEffect(EffectManager effectManager, Entity entity) {
        super(effectManager, entity);
        power = 10;
    }

}
