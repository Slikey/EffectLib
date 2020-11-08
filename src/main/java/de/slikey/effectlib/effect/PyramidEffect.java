package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;

public class PyramidEffect extends Effect {

    /**
     * Particle of the cube
     */
    public Particle particle = Particle.FLAME;

    /**
     * Particles in each row
     */
    public int particles = 8;

    /**
     * Center to edge distance
     */
    public double radius = 0;

    /**
     * Use corners of blocks
     */
    public boolean blockSnap = false;

    public PyramidEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 5;
        iterations = 200;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        drawOutline(location);
    }

    private void drawOutline(Location location) {
        Vector v = new Vector();
        for (int i = 0; i < particles; i++) {
            // X base
            drawEdge(location, v, i, 0, 0, -1);
            drawEdge(location, v, i, 0, 0, 1);

            // Z base
            drawEdge(location, v, i, -1, 0, 0);
            drawEdge(location, v, i, 1, 0, 0);

            // diagonals
            drawEdge(location, v, i, -1, 1, -1);
            drawEdge(location, v, i, -1, 1, 1);
            drawEdge(location, v, i, 1, 1, -1);
            drawEdge(location, v, i, 1, 1, 1);
        }
    }

    private void drawEdge(Location center, Vector v, int i, int dx, int dy, int dz) {
        // Y goes from 0 to 1
        // X and Z go from -1 to 1
        double ratio = (double)i / particles;
        if (dy == 1) {
            v.setY(ratio);
            if (dx < 0) {
                v.setX(ratio - 1);
            } else {
                v.setX(1 - ratio);
            }
            if (dz < 0) {
                v.setZ(ratio - 1);
            } else {
                v.setZ(1 - ratio);
            }
        } else {
            v.setY(0);

            if (dx == 0) {
                v.setX(ratio * 2 - 1);
            } else {
                v.setX(dx);
            }
            if (dz == 0) {
                v.setZ(ratio * 2 - 1);
            } else {
                v.setZ(dz);
            }
        }
        display(particle, center.add(v.multiply(radius)));
        center.subtract(v);
    }
}
