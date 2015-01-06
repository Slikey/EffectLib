package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class FlameEffect extends Effect {

    public FlameEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 600;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int i = 0; i < 10; i++) {
            Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d);
            v.setY(RandomUtils.random.nextFloat() * 1.8);
            location.add(v);
            ParticleEffect.FLAME.display(location, visibleRange);
            location.subtract(v);
        }
    }

}
