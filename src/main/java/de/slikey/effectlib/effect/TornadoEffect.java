package de.slikey.effectlib.effect;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.RandomUtils;

public class TornadoEffect extends Effect {

    /*
     * Tornado particle
     */
    public Particle tornadoParticle = Particle.FLAME;
    public Color tornadoColor = null;

    /*
     * Particle of the cloud
     */
    public Particle cloudParticle = Particle.CLOUD;
    public Color cloudColor = null;
    public float cloudSpeed = 0;

    /*
     * Size of the cloud
     */
    public float cloudSize = 2.5f;

    /*
     * Y-Offset from location
     */
    public double yOffset = .8;

    /*
     * Height of the Tornado
     */
    public float tornadoHeight = 5f;

    /*
     * Max radius of the Tornado
     */
    public float maxTornadoRadius = 5f;

    /*
     * Should the cloud appear?
     */
    public boolean showCloud = true;

    /*
     * Should the tornado appear?
     */
    public boolean showTornado = true;

    /*
     * Distance between each row
     */
    public double distance = .375d;

    /*
     * Number of particles per circle
     */
    public int circleParticles = 64;

    /*
     * Number of particles in the cloud
     */
    public int cloudParticles = 100;

    /*
     * Amount of y-jitter between circle particles
     */
    public double circleHeight = 0;

    /*
     * Internal counter
     */
    protected int step = 0;

    public TornadoEffect(EffectManager manager) {
        super(manager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = 20;
    }

    @Override
    public void reset() {
        this.step = 0;
    }

    @Override
    public void onRun() {
        Location l = getLocation().add(0, yOffset, 0);
        for (int i = 0; i < (cloudParticles * cloudSize); i++) {
            Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * cloudSize);
            if (showCloud) {
                display(cloudParticle, l.add(v), cloudColor, cloudSpeed, 1);
                l.subtract(v);
            }
        }
        Location t = l.clone().add(0, .2, 0);
        double r = .45 * (maxTornadoRadius * (2.35 / tornadoHeight));
        for (double y = 0; y < tornadoHeight; y += distance) {
            double fr = r * y;
            if (fr > maxTornadoRadius) {
                fr = maxTornadoRadius;
            }
            for (Vector v : createCircle(y, fr)) {
                if (showTornado) {
                    if (circleHeight > 0) v.setY(v.getY() + RandomUtils.random.nextDouble() * circleHeight / 2 - circleHeight / 2);
                    display(tornadoParticle, t.add(v), tornadoColor);
                    t.subtract(v);
                    step++;
                }
            }
        }
        l.subtract(0, yOffset, 0);
    }

    public List<Vector> createCircle(double y, double radius) {
        double amount = radius * circleParticles;
        double inc = (2 * Math.PI) / amount;
        List<Vector> vecs = new ArrayList<Vector>();
        for (int i = 0; i < amount; i++) {
            double angle = i * inc;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vector v = new Vector(x, y, z);
            vecs.add(v);
        }
        return vecs;
    }

}
