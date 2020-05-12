package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.VectorUtils;
import de.slikey.effectlib.math.EquationStore;
import de.slikey.effectlib.math.EquationTransform;

public class EquationEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.REDSTONE;

    /**
     * Equations defining the X,Y,Z coordinates over
     * iteration t
     * 
     * These equations can make use of most common math functions,
     * including randomized numbers. Some examples:
     * 
     * 4*sin(t)
     * cos(t * rand(-4,5) + 32
     * tan(t)
     */
    public String xEquation = "t";
    public String yEquation = "0";
    public String zEquation = "0";

    /**
     * The variable name used in equations to represent major ticks
     */
    public String variable = "t";

    /**
     * How many steps to take per iteration
     */
    public int particles = 1;
    
    /**
     * A set of equations that, if set, will be performed in a sub-iteration
     * for each major iteration.
     */
    public String x2Equation = null;
    public String y2Equation = null;
    public String z2Equation = null;
    
    /**
     * The variable name used in sub-equations to represent minor ticks
     */
    public String variable2 = "t2";

    /**
     * How many steps to take per sub-iteration
     */
    public int particles2 = 0;

    /**
     * Whether or not to orient the effect in the direction
     * of the source Location
     * 
     * If this is set to true, the X axis will represent "forward".
     */
    public boolean orient = true;
    
    /**
     * Similar to orient, however this is specific to pitch.
     */
    public boolean orientPitch = true;

    /**
     * These is the limit for the steps until it starts over.
     */
    public int maxSteps = 0;
    
    /**
     * If this is true, after cycling the inner equation, it'll be set to 0.
     * Set this to false if you want the miniStep to be saved between major steps.
     */
    public boolean cycleMiniStep = true;

    private EquationTransform xTransform;
    private EquationTransform yTransform;
    private EquationTransform zTransform;

    private EquationTransform x2Transform;
    private EquationTransform y2Transform;
    private EquationTransform z2Transform;
    
    private int step = 0;
    private int miniStep = 0;
    
    public EquationEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 100;
    }

    @Override
    public void reset() {
        step = 0;
        miniStep = 0;
    }

    @Override
    public void onRun() {
        if (xTransform == null) {
            xTransform = EquationStore.getInstance().getTransform(xEquation, variable);
            yTransform = EquationStore.getInstance().getTransform(yEquation, variable);
            zTransform = EquationStore.getInstance().getTransform(zEquation, variable);
            
            if (x2Equation != null && y2Equation != null && z2Equation != null && particles2 > 0) {
                x2Transform = EquationStore.getInstance().getTransform(x2Equation, variable, variable2);
                y2Transform = EquationStore.getInstance().getTransform(y2Equation, variable, variable2);
                z2Transform = EquationStore.getInstance().getTransform(z2Equation, variable, variable2);
            }
        }
        Location location = getLocation();

        boolean hasInnerEquation = (x2Transform != null && y2Transform != null && z2Transform != null);
        for (int i = 0; i < particles; i++) {
            double xValue = xTransform.get(step);
            double yValue = yTransform.get(step);
            double zValue = zTransform.get(step);
            
            Vector result = new Vector(xValue, yValue, zValue);
            if (orient && orientPitch) result = VectorUtils.rotateVector(result, location);
            else if (orient) result = VectorUtils.rotateVector(result, location.getYaw(), 0);

            Location targetLocation = location.clone();
            targetLocation.add(result);

            if (hasInnerEquation) {
                for (int j = 0; j < particles2; j++) {
                    double x2Value = x2Transform.get(step, miniStep);
                    double y2Value = y2Transform.get(step, miniStep);
                    double z2Value = z2Transform.get(step, miniStep);

                    Vector result2 = new Vector(x2Value, y2Value, z2Value);
                    if (orient && orientPitch) result2 = VectorUtils.rotateVector(result2, location);
                    else if (orient) result2 = VectorUtils.rotateVector(result2, location.getYaw(), 0);

                    Location target2Location = targetLocation.clone().add(result2);
                    display(particle, target2Location);

                    miniStep++;
                }

                if (cycleMiniStep) miniStep = 0;
            } else {
                display(particle, targetLocation);
            }

            if (maxSteps != 0 && step > maxSteps) {
                step = 0;
                break;
            } else {
                step++;
            }
        }
    }

}
