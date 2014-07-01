package de.slikey.effectlib.effect;

import net.minecraft.util.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Entity;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;

public abstract class EntityEffect extends Effect {
	protected final Entity entity;
	
	public EntityEffect(EffectManager effectManager, Entity entity) {
		super(effectManager);
        Validate.notNull(entity, "Entity cannot be null!");
		this.entity = entity;
	}

}
