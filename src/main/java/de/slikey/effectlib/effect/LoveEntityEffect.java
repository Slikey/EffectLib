package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class LoveEntityEffect extends EntityEffect {

    /**
     * Particle to display
     */
    public ParticleEffect particle = ParticleEffect.HEART;

    public LoveEntityEffect(EffectManager effectManager, Entity entity) {
        super(effectManager, entity);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 600;
    }

    @Override
    public void onRun() {
        Location location = entity.getLocation();
        location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
        location.add(0, RandomUtils.random.nextFloat() * 2, 0);
        particle.display(location, visibleRange);
    }

}
