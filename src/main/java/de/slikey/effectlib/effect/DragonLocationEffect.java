package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class DragonLocationEffect extends LocationEffect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Pitch of the dragon arc
     */
    public float pitch = .2f;

    /**
     * Arcs to build the breath
     */
    public int arcs = 10;

    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    public DragonLocationEffect(EffectManager effectManager, Location location) {
        super(effectManager, location);
        type = EffectType.REPEATING;
        period = 5;
        iterations = 200;
    }

    @Override
    public void onRun() {
        for (int i = 0; i < arcs; i++) {
            float pitch = 1 + RandomUtils.random.nextFloat() * 2 * this.pitch - this.pitch;
            float x = step;
            float y = (float) (pitch * Math.pow(step, 2));
            Vector v = new Vector(x, y, 0);
            VectorUtils.rotateAroundAxisX(v, location.getPitch() * MathUtils.degreesToRadians);
            VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
            particle.display(location.add(v), visibleRange, 0, 0, 0, 0, 1);
            location.subtract(v);
        }
        step++;
    }
}
