package de.slikey.effectlib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import de.slikey.effectlib.entity.EntityManager;
import de.slikey.effectlib.listener.ItemListener;

public final class EffectLib extends JavaPlugin {

	private static EffectLib instance;
	private EntityManager entityManager;
	private List<EffectManager> effectManagers;

	public EffectLib() {
		super();
		instance = this;
	}

	public static EffectLib instance() {
		return instance;
	}

	@Override
	public void onEnable() {
		entityManager = new EntityManager(this);
		effectManagers = new ArrayList<EffectManager>();

		loadListeners();
	}

	@Override
	public void onDisable() {
		entityManager.dispose();
		for (Iterator<EffectManager> i = effectManagers.iterator(); i.hasNext();) {
			EffectManager em = i.next();
			i.remove();
			em.dispose();
		}
		HandlerList.unregisterAll(this);
	}

	private void loadListeners() {
		getServer().getPluginManager().registerEvents(new ItemListener(), this);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<EffectManager> getEffectManagers() {
		return effectManagers;
	}
}
