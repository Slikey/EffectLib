package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.math.EquationTransform;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class EquationEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.REDSTONE;

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
     * Set this to true to have the effect repeat from
     * t = 0 at each iteration.
     */
    public boolean cycle = false;

    private EquationTransform xTransform;
    private EquationTransform yTransform;
    private EquationTransform zTransform;

    private EquationTransform x2Transform;
    private EquationTransform y2Transform;
    private EquationTransform z2Transform;
    
    private int step;
    
    public EquationEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 100;
        step = 0;
    }

    @Override
    public void onRun() {
        if (xTransform == null) {
            xTransform = new EquationTransform(xEquation, variable, "p", "p2");
            yTransform = new EquationTransform(yEquation, variable, "p", "p2");
            zTransform = new EquationTransform(zEquation, variable, "p", "p2");
            
            if (x2Equation != null && y2Equation != null && z2Equation != null && particles2 > 0) {
                x2Transform = new EquationTransform(x2Equation, variable, variable2, "p", "p2");
                y2Transform = new EquationTransform(y2Equation, variable, variable2, "p", "p2");
                z2Transform = new EquationTransform(z2Equation, variable, variable2, "p", "p2");
            }
        }
        Location location = getLocation();

        boolean hasInnerEquation = (x2Transform != null && y2Transform != null && z2Transform != null);
        for (int i = 0; i < particles; i++) {
            Double xValue = xTransform.get(step, particles);
            Double yValue = yTransform.get(step, particles);
            Double zValue = zTransform.get(step, particles);
            
            Vector result = new Vector(xValue, yValue, zValue);
            if (orient) {
                result = VectorUtils.rotateVector(result, location);
            }

            Location targetLocation = location.clone();
            targetLocation.add(result);
            if (!hasInnerEquation) {
                display(particle, targetLocation);
            } else {
                for (int j = 0; j < particles2; j++) {
                    Double x2Value = x2Transform.get(step, j, particles, particles2);
                    Double y2Value = y2Transform.get(step, j, particles, particles2);
                    Double z2Value = z2Transform.get(step, j, particles, particles2);

                    Location target2Location = targetLocation.clone();
                    target2Location.setX(target2Location.getX() + x2Value);
                    target2Location.setY(target2Location.getY() + y2Value);
                    target2Location.setZ(target2Location.getZ() + z2Value);
                    display(particle, target2Location);
                }
            }
            
            step++;
        }
        
        if (cycle) {
            step = 0;
        }
    }
}
