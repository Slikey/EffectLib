package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ArcLocationEffect extends LocationEffect {

    /**
     * Links the two Locations
     */
    protected final Vector link;

    /**
     * Length of the link between two Locations
     */
    protected final float length;

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Height of the arc in blocks
     */
    public float height = 2;

    /**
     * Particles per arc
     */
    public int particles = 100;

    /**
     * Internal counter
     */
    protected int step = 0;

    public ArcLocationEffect(EffectManager effectManager, Location start, Location stop) {
        super(effectManager, start);
        link = stop.toVector().subtract(start.toVector());
        length = (float) link.length();

        type = EffectType.REPEATING;
        period = 1;
        iterations = 200;
    }

    @Override
    public void onRun() {
        float pitch = (float) (4 * height / Math.pow(length, 2));
        for (int i = 0; i < particles; i++) {
            Vector v = link.clone().normalize().multiply((float) length * i / particles);
            float x = ((float) i / particles) * length - length / 2;
            float y = (float) (-pitch * Math.pow(x, 2) + height);
            location.add(v).add(0, y, 0);
            particle.display(location, visibleRange);
            location.subtract(0, y, 0).subtract(v);

            step++;
        }
    }

}
