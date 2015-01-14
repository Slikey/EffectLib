package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class IconEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.VILLAGER_ANGRY;

    public int yOffset = 2;

    public IconEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 4;
        iterations = 25;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(0, yOffset, 0);
        display(particle, location);
    }
}
