package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import org.bukkit.entity.Entity;

public class SkyRocketEffect extends JumpEffect {

    public SkyRocketEffect(EffectManager effectManager, Entity entity) {
        super(effectManager, entity);
        power = 10;
    }

}
