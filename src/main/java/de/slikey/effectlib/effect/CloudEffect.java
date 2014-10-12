package de.slikey.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.RandomUtils;

public class CloudEffect extends Effect{
	
	/*
	 * Particle of the cloud
	 */
	public ParticleEffect cloudParticle = ParticleEffect.CLOUD;
	
	/*
	 * Particle of the rain/snow
	 */
	public ParticleEffect mainParticle = ParticleEffect.DRIP_WATER;
	
	/*
	 * Size of the cloud
	 */
	public float cloudSize = .7f;
	
	/*
	 * Radius of the rain/snow
	 */
	public float particleRadius = cloudSize-.1f;
	
	/*
	 * Y-Offset from location
	 */
	public double yOffset = .8;
	
	public CloudEffect(EffectManager manager){
		super(manager);
		type = EffectType.REPEATING;
		period = 5;
		iterations = 50;
	}
	
	@Override
	public void onRun(){
		Location location = getLocation();
		location.add(0, yOffset, 0);
		for(int i = 0; i < 50; i++){
			Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * cloudSize);
			cloudParticle.display(location.add(v), visibleRange, 0, 0, 0, 0, 7);
			location.subtract(v);
		}
		Location l = location.add(0, .2, 0);
		for(int i = 0; i < 15; i++){
			int r = RandomUtils.random.nextInt(2);
			double x = RandomUtils.random.nextDouble() * particleRadius;
			double z = RandomUtils.random.nextDouble() * particleRadius;
			l.add(x, 0, z);
			if(r!=1)
				mainParticle.display(l, visibleRange);
			l.subtract(x, 0, z);
			l.subtract(x, 0, z);
			if(r!=1)
				mainParticle.display(l, visibleRange);
			l.add(x, 0, z);
		}
	}

}
