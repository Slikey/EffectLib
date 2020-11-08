package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import org.bukkit.Particle;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GridEffect extends Effect {

    /**
     * ParticleType of the nucleus
     */
    public Particle particle = Particle.FLAME;

    /**
     * Rows of the grid
     */
    public int rows = 5;

    /**
     * Columns of the grid
     */
    public int columns = 10;

    /**
     * Width per cell in blocks
     */
    public float widthCell = 1;

    /**
     * Height per cell in blocks
     */
    public float heightCell = 1;

    /**
     * Particles to be spawned on the horizontal borders of the cell
     */
    public int particlesWidth = 4;

    /**
     * Particles to be spawned on the vertical borders of the cell
     */
    public int particlesHeight = 3;

    /**
     * Rotation around the Y-axis
     */
    public double rotation = 0;

    /**
     * Rotation around the X-axis
     */
    public double rotationX = 0;

    /**
     * Rotation around the Z-axis
     */
    public double rotationZ = 0;

    /**
     * To center the grid on the location
     */
    public boolean center = false;

    public GridEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        period = 5;
        iterations = 50;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        Vector v = new Vector();
        // Draw rows
        for (int i = 0; i <= (rows + 1); i++) {
            for (int j = 0; j < particlesWidth * (columns + 1); j++) {
                v.setY(i * heightCell);
                v.setX(j * widthCell / particlesWidth);
                addParticle(location, v);
            }
        }
        // Draw columns
        for (int i = 0; i <= (columns + 1); i++) {
            for (int j = 0; j < particlesHeight * (rows + 1); j++) {
                v.setX(i * widthCell);
                v.setY(j * heightCell / particlesHeight);
                addParticle(location, v);
            }
        }
    }

    protected void addParticle(Location location, Vector v) {
        v.setZ(0);
        if (center) {
            v.setY(v.getY() + heightCell * -(rows + 1) / 2);
            v.setX(v.getX() + widthCell * -(columns + 1) / 2);
        }
        VectorUtils.rotateAroundAxisY(v, rotation);
        if (rotationX != 0) {
            VectorUtils.rotateAroundAxisX(v, rotationX);
        }
        if (rotationZ != 0) {
            VectorUtils.rotateAroundAxisZ(v, rotationZ);
        }
        location.add(v);
        display(particle, location);
        location.subtract(v);
    }

}
