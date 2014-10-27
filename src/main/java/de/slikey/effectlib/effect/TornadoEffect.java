package de.slikey.effectlib.effect;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class TornadoEffect extends Effect{
	
	/*
	 * Tornado particle
	 */
	public ParticleEffect tornadoParticle = ParticleEffect.FLAME;
	
	/*
	 * Particle of the cloud
	 */
	public ParticleEffect cloudParticle = ParticleEffect.CLOUD;
	
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
	 * Should it rotate?
	 */
	public boolean enableRotation = true;
	
	/*
     * Turns the cube by this angle each iteration around the y-axis
     */
    public double angularVelocityY = Math.PI / 170;
    
    /*
     * Distance between each row
     */
    public double distance = .375d;
    
    /*
     * Internal counter
     */
    protected int step = 0;
	
	public TornadoEffect(EffectManager manager){
		super(manager);
		type = EffectType.REPEATING;
		period = 5;
		iterations = 20;
	}
	
	@Override
	public void onRun(){
		Location l = getLocation().add(0, yOffset, 0);
		for(int i = 0; i < (100 * cloudSize); i++){
			Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * cloudSize);
			if(showCloud){
				cloudParticle.display(l.add(v), visibleRange, 0, 0, 0, 0, 7);
				l.subtract(v);
			}
		}
		Location t = l.clone().add(0, .2, 0);
		double r = .45 * (maxTornadoRadius*(2.35/tornadoHeight));
		for(double y = 0; y < tornadoHeight; y+=distance){
			double fr = r * y;
			if(fr > maxTornadoRadius)
				fr = maxTornadoRadius;
			for(Vector v : createCircle(y, fr)){
				if(showTornado){
					if(enableRotation)
						VectorUtils.rotateVector(v, 0, angularVelocityY * step, 0);
					tornadoParticle.display(t.add(v), visibleRange);
					t.subtract(v);
					step++;
				}
			}
		}
		l.subtract(0, yOffset, 0);
	}
	
	public ArrayList<Vector> createCircle(double y, double radius){
		double amount = radius * 64;
		double inc = (2*Math.PI)/amount;
		ArrayList<Vector> vecs = new ArrayList<Vector>();
		for(int i = 0; i < amount; i++){
			double angle = i * inc;
			double x = radius * Math.cos(angle);
			double z = radius * Math.sin(angle);
			Vector v = new Vector(x, y, z);
			vecs.add(v);
		}
		return vecs;
	}

}
