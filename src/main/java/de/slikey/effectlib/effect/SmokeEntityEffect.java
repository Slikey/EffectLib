package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class SmokeEntityEffect extends EntityEffect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.SMOKE;

    public SmokeEntityEffect(EffectManager effectManager, Entity entity) {
        super(effectManager, entity);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 300;
    }

    @Override
    public void onRun() {
        for (int i = 0; i < 20; i++) {
            Location location = entity.getLocation();
            location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
            location.add(0, RandomUtils.random.nextFloat() * 2, 0);
            particle.display(location, visibleRange);
        }
    }

}
