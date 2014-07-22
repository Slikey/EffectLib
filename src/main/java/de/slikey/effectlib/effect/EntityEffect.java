package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import net.minecraft.util.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Entity;

public abstract class EntityEffect extends Effect {
    /**
     * Entity that is affected by the Effect
     */
    protected final Entity entity;

    public EntityEffect(EffectManager effectManager, Entity entity) {
        super(effectManager);
        Validate.notNull(entity, "Entity cannot be null!");
        this.entity = entity;
    }

}
