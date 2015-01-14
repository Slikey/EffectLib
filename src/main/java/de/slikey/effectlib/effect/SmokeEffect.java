package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SmokeEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.SMOKE_NORMAL;

    public SmokeEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 300;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int i = 0; i < 20; i++) {
            location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
            location.add(0, RandomUtils.random.nextFloat() * 2, 0);
            display(particle, location);
        }
    }

}
