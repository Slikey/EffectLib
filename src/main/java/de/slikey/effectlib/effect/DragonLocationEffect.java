package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

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
     * Particles per arc
     */
    public int particles = 30;

    /**
     * Length in blocks
     */
    public float length = 4;

    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    protected final List<Float> rndF, rndAngle;

    public DragonLocationEffect(EffectManager effectManager, Location location) {
        super(effectManager, location);
        type = EffectType.REPEATING;
        period = 5;
        iterations = 200;
        rndF = new ArrayList<Float>(arcs);
        rndAngle = new ArrayList<Float>(arcs);
    }

    @Override
    public void onRun() {
        while (rndF.size() < arcs) rndF.add(RandomUtils.random.nextFloat());
        while (rndAngle.size() < arcs) rndAngle.add(RandomUtils.random.nextFloat());
        for (int i = 0; i < arcs; i++) {
            float pitch = 1 + rndF.get(i) * 2 * this.pitch - this.pitch;
            float x = step % particles;
            float y = (float) (pitch * Math.pow(step % particles, 2));
            Vector v = new Vector(x * length / particles, y, 0);
            VectorUtils.rotateAroundAxisX(v, rndAngle.get(i));
            VectorUtils.rotateAroundAxisZ(v, location.getPitch() * MathUtils.degreesToRadians);
            VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
            particle.display(location.add(v), visibleRange, 0, 0, 0, 0, 1);
            location.subtract(v);
        }
        step++;
    }
}
