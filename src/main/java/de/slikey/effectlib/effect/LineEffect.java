package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LineEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Particles per arc
     */
    public int particles = 100;

    public LineEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        period = 5;
        iterations = 200;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Location target = getTarget();
        if (target == null) {
            cancel();
            return;
        }
        Vector link = target.toVector().subtract(location.toVector());
        float length = (float) link.length();
        link.normalize();

        float ratio = length / particles;
        Vector v = link.multiply(ratio);
        Location loc = location.clone().subtract(v);
        for (int i = 0; i < particles; i++) {
            loc.add(v);
            particle.display(loc, visibleRange);
        }
    }

}
