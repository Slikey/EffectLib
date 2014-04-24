package de.slikey.effectlib.util;

public enum ParticleType {
	LARGE_EXPLOSION("largeexplode"),
	FIREWORK_SPARK("fireworksSpark"),
	BUBBLE("bubble"),
	SUSPENDED("suspended"),
	DEPTH_SUSPEND("depthsuspend"),
	TOWN_AURA("townaura"),
	CRITICAL_HIT("crit"),
	MAGICAL_CRITICAL_HIT("magicCrit"),
	SMOKE("smoke"),
	MOB_SPELL("mobSpell"),
	MOB_SPELL_AMBIENT("mobSpellAmbient"),
	SPELL("spell"),
	INSTANT_SPELL("instantSpell"),
	WITCH_MAGIC("witchMagic"),
	NOTE("note"),
	PORTAL("portal"),
	ENCHANTMENT_TABLE("enchantmenttable"),
	EXPLODE("explode"),
	FLAME("flame"),
	LAVA("lava"),
	FOOTSTEP("footstep"),
	SPLASH("splash"),
	LARGE_SMOKE("largesmoke"),
	CLOUD("cloud"),
	REDSTONE("reddust"),
	SNOWBALL_POOF("snowballpoof"),
	DROP_WATER("dripWater"),
	DROP_LAVA("dripLava"),
	SNOW_SHOVEL("snowshovel"),
	SLIME("slime"),
	HEART("heart"),
	ANGRY_VILLAGER("angryVillager"),
	HAPPY_VILLAGER("happyVillager"),
	HUGE_EXPLOSION("hugeexplosion");

	private String id;

	private ParticleType(String name) {
		id = name;
	}

	public String getParticleName() {
		return id;
	}
}