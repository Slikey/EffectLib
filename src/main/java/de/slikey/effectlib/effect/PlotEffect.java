package de.slikey.effectlib.effect;

import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Particle;
import org.bukkit.Location;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.math.EquationStore;
import de.slikey.effectlib.math.EquationTransform;

public class PlotEffect extends Effect {

    private final static String[] _variables = {"t", "i"};
    private final static Set<String> variables = new HashSet<>(Arrays.asList(_variables));

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.REDSTONE;

    /**
     * The equation to use for x-values. If not set, the iteration count will be used.
     */
    public String xEquation;

    /**
     * The equation to use for y-values. If not set, the iteration count will be used.
     */
    public String yEquation;

    /**
     * The equation to use for y-values. If not set, 0 will be used.
     */
    public String zEquation;

    /**
     * This is a shortcut to quickly scaling the x value.
     */
    public double xScale = 1.0;

    /**
     * This is a shortcut to quickly scaling the y value.
     */
    public double yScale = 1.0;

    /**
     * This is a shortcut to quickly scaling the z value.
     */
    public double zScale = 1.0;

    /**
     * This will re-spawn particles as the plot moves to make a solid line.
     */
    public boolean persistent = true;

    private int step = 0;

    public PlotEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 100;
    }

    @Override
    public void onRun() {
        int base = persistent ? 0 : step;
        for (int i = base; i <= step; i++) {
            Location location = getLocation().clone();
            double xOffset = step;
            double yOffset = step;
            double zOffset = 0;

            if (xEquation != null && !xEquation.isEmpty()) {
                EquationTransform xTransform = EquationStore.getInstance().getTransform(xEquation, variables);
                xOffset = xTransform.get(i, maxIterations);
            }

            if (yEquation != null && !yEquation.isEmpty()) {
                EquationTransform yTransform = EquationStore.getInstance().getTransform(yEquation, variables);
                yOffset = yTransform.get(i, maxIterations);
            }

            if (zEquation != null && !zEquation.isEmpty()) {
                EquationTransform zTransform = EquationStore.getInstance().getTransform(zEquation, variables);
                zOffset = zTransform.get(i, maxIterations);
            }

            location.add(xOffset * xScale, yOffset * yScale, zOffset * zScale);
            display(particle, location);
        }

        step++;
    }

}
