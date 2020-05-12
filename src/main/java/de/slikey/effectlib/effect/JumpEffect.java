package de.slikey.effectlib.effect;

import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;

public class JumpEffect extends Effect {

    /**
     * Power of jump. (0.5f)
     */
    public float power = .5f;

    public JumpEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
        period = 20;
        iterations = 1;
        asynchronous = false;
    }

    @Override
    public void onRun() {
        Entity entity = getEntity();
        if (entity == null) {
            cancel();
            return;
        }
        Vector v = entity.getVelocity();
        v.setY(v.getY() + power);
        entity.setVelocity(v);
    }

}
