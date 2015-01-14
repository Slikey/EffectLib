package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class LoveEffect extends Effect {

    /**
     * Particle to display
     */
    public ParticleEffect particle = ParticleEffect.HEART;

    public LoveEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 600;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
        location.add(0, RandomUtils.random.nextFloat() * 2, 0);
        display(particle, location);
    }

}
