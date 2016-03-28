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
     * How many steps to take per iteration
     */
    public int particles = 1;

    /**
     * Whether or not to orient the effect in the direction
     * of the source Location
     * 
     * If this is set to true, the X axis will represent "forward".
     */
    public boolean orient = true;

    private EquationTransform xTransform;
    private EquationTransform yTransform;
    private EquationTransform zTransform;
    
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
        if (step == 0) {
            xTransform = new EquationTransform(xEquation);
            yTransform = new EquationTransform(yEquation);
            zTransform = new EquationTransform(zEquation);
        }
        Location location = getLocation();

        for (int i = 0; i < particles; i++) {
            Double xValue = xTransform.get(step);
            Double yValue = yTransform.get(step);
            Double zValue = zTransform.get(step);
            
            Vector result = new Vector(xValue, yValue, zValue);
            if (orient) {
                result = VectorUtils.rotateVector(result, location);
            }

            Location targetLocation = location.clone();
            targetLocation.add(result);
            display(particle, targetLocation);
            step++;
        }
    }
}
