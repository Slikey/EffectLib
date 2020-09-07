package de.slikey.effectlib.effect;

import org.bukkit.Particle;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;

public class ParticleEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particle = Particle.VILLAGER_ANGRY;

    public ParticleEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 1;
    }

    @Override
    public void onRun() {
        display(particle, getLocation());
    }
}
