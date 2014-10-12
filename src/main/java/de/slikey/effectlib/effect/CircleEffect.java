package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.VectorUtils;

public class CircleEffect extends Effect{
	

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.HAPPY_VILLAGER;

    /**
     * Rotation of the torus.
     */
    public double xRotation, yRotation, zRotation = 0;
    
    /**
     * Radials to spawn next note.
     */
    public double radialsPerStep = Math.PI / 8;

    /**
     * Radius of circle above head
     */
    public float radius = .4f;

    /**
     * Current step. Works as a counter
     */
    protected float step = 0;
    
    /**
     * Subtracts from location if needed
     */
    public double xSubtract, ySubtract, zSubtract;

    public CircleEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 10;
        iterations = 20;
    }
    
    @Override
    public void onRun(){
    	Location location = getLocation();
    	location.subtract(xSubtract, ySubtract, zSubtract);
    	Vector v = new Vector();
    	v.setX(Math.cos(radialsPerStep * step) * radius);
    	v.setZ(Math.sin(radialsPerStep * step) * radius);
    	VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
    	particle.display(0, 0, 0, .5f, 1, location.add(v), visibleRange);
    	step++;
    }

}
