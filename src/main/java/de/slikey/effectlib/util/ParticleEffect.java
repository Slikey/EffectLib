package de.slikey.effectlib.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * <b>ParticleEffect Enum</b>
 * <p>
 * This is here to support plugins built on older (pre-6) versions of EffectLib.
 * It should not longer be used, preferring to use the Spigot Particle API directly, or ParticleUtils.
 */
@Deprecated
public enum ParticleEffect {

    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    EXPLOSION_NORMAL("explode"),

    /**
     * A particle effect which is displayed by exploding ghast fireballs and wither skulls:
     * <ul>
     * <li>It looks like a gray ball which is fading away
     * <li>The speed value slightly influences the size of this particle effect
     * </ul>
     */
    EXPLOSION_LARGE("largeexplode"),

    /**
     * A particle effect which is displayed by exploding tnt and creepers:
     * <ul>
     * <li>It looks like a crowd of gray balls which are fading away
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    EXPLOSION_HUGE("hugeexplosion"),

    /**
     * A particle effect which is displayed by launching fireworks:
     * <ul>
     * <li>It looks like a white star which is sparkling
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    FIREWORKS_SPARK("fireworksSpark"),

    /**
     * A particle effect which is displayed by swimming entities and arrows in water:
     * <ul>
     * <li>It looks like a bubble
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    WATER_BUBBLE("bubble"),

    /**
     * A particle effect which is displayed by swimming entities and shaking wolves:
     * <ul>
     * <li>It looks like a blue drop
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    WATER_SPLASH("splash"),

    /**
     * A particle effect which is displayed on water when fishing:
     * <ul>
     * <li>It looks like a blue droplet
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    WATER_WAKE("wake"),

    /**
     * A particle effect which is displayed by water:
     * <ul>
     * <li>It looks like a tiny blue square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SUSPENDED("suspended"),

    /**
     * A particle effect which is displayed by air when close to bedrock and the in the void:
     * <ul>
     * <li>It looks like a tiny gray square
     * <li>The speed value has no influence on this particle effect
     * <li>Removed as of 1.13
     * </ul>
     */
    @Deprecated
    SUSPENDED_DEPTH("depthSuspend"),

    /**
     * A particle effect which is displayed when landing a critical hit and by arrows:
     * <ul>
     * <li>It looks like a light brown cross
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CRIT("crit"),

    /**
     * A particle effect which is displayed when landing a hit with an enchanted weapon:
     * <ul>
     * <li>It looks like a cyan star
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CRIT_MAGIC("magicCrit"),

    /**
     * A particle effect which is displayed by primed tnt, torches, droppers, dispensers, end portals, brewing stands and monster spawners:
     * <ul>
     * <li>It looks like a little gray cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    SMOKE_NORMAL("smoke"),

    /**
     * A particle effect which is displayed by fire, minecarts with furnace and blazes:
     * <ul>
     * <li>It looks like a large gray cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    SMOKE_LARGE("largesmoke"),

    /**
     * A particle effect which is displayed when splash potions or bottles o' enchanting hit something:
     * <ul>
     * <li>It looks like a white swirl
     * <li>The speed value causes the particle to only move upwards when set to 0
     * </ul>
     */
    SPELL("spell"),

    /**
     * A particle effect which is displayed when instant splash potions hit something:
     * <ul>
     * <li>It looks like a white cross
     * <li>The speed value causes the particle to only move upwards when set to 0
     * </ul>
     */
    SPELL_INSTANT("instantSpell"),

    /**
     * A particle effect which is displayed by entities with active potion effects:
     * <ul>
     * <li>It looks like a colored swirl
     * <li>The speed value causes the particle to be colored black when set to 0
     * <li>The particle color gets lighter when increasing the speed and darker when decreasing the speed
     * </ul>
     */
    SPELL_MOB("mobSpell"),

    /**
     * A particle effect which is displayed by entities with active potion effects applied through a beacon:
     * <ul>
     * <li>It looks like a transparent colored swirl
     * <li>The speed value causes the particle to be always colored black when set to 0
     * <li>The particle color gets lighter when increasing the speed and darker when decreasing the speed
     * </ul>
     */
    SPELL_MOB_AMBIENT("mobSpellAmbient"),

    /**
     * A particle effect which is displayed by witches:
     * <ul>
     * <li>It looks like a purple cross
     * <li>The speed value causes the particle to only move upwards when set to 0
     * </ul>
     */
    SPELL_WITCH("witchMagic"),

    /**
     * A particle effect which is displayed by blocks beneath a water source:
     * <ul>
     * <li>It looks like a blue drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIP_WATER("dripWater"),

    /**
     * A particle effect which is displayed by blocks beneath a lava source:
     * <ul>
     * <li>It looks like an orange drip
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    DRIP_LAVA("dripLava"),

    /**
     * A particle effect which is displayed when using bone meal and trading with a villager in a village:
     * <ul>
     * <li>It looks like a green star
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    VILLAGER_HAPPY("happyVillager"),

    /**
     * A particle effect which is displayed when attacking a villager in a village:
     * <ul>
     * <li>It looks like a cracked gray heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    VILLAGER_ANGRY("angryVillager"),

    /**
     * A particle effect which is displayed by mycelium:
     * <ul>
     * <li>It looks like a tiny gray square
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    TOWN_AURA("townaura"),

    /**
     * A particle effect which is displayed by note blocks:
     * <ul>
     * <li>It looks like a colored note
     * <li>The speed value causes the particle to be colored green when set to 0
     * </ul>
     */
    NOTE("note"),

    /**
     * A particle effect which is displayed by nether portals, endermen, ender pearls, eyes of ender, ender chests and dragon eggs:
     * <ul>
     * <li>It looks like a purple cloud
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    PORTAL("portal"),

    /**
     * A particle effect which is displayed by enchantment tables which are nearby bookshelves:
     * <ul>
     * <li>It looks like a cryptic white letter
     * <li>The speed value influences the spread of this particle effect
     * </ul>
     */
    ENCHANTMENT_TABLE("enchantmenttable"),

    /**
     * A particle effect which is displayed by torches, active furnaces, magma cubes and monster spawners:
     * <ul>
     * <li>It looks like a tiny flame
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    FLAME("flame"),

    /**
     * A particle effect which is displayed by lava:
     * <ul>
     * <li>It looks like a spark
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    LAVA("lava"),

    /**
     * A particle effect which is displayed when a mob dies:
     * <ul>
     * <li>It looks like a large white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * </ul>
     */
    CLOUD("cloud"),

    /**
     * A particle effect which is displayed by redstone ore, powered redstone, redstone torches and redstone repeaters:
     * <ul>
     * <li>It looks like a tiny colored cloud
     * <li>The speed value causes the particle to be colored red when set to 0
     * </ul>
     */
    REDSTONE("reddust"),

    /**
     * A particle effect which is displayed when snowballs hit a block:
     * <ul>
     * <li>It looks like a little piece with the snowball texture
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SNOWBALL("snowballpoof"),

    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a tiny white cloud
     * <li>The speed value influences the velocity at which the particle flies off
     * <li>This was removed as of 1.13
     * </ul>
     */
    @Deprecated
    SNOW_SHOVELL("snowshovel"),

    /**
     * A particle effect which is displayed by slimes:
     * <ul>
     * <li>It looks like a tiny part of the slimeball icon
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    SLIME("slime"),

    /**
     * A particle effect which is displayed when breeding and taming animals:
     * <ul>
     * <li>It looks like a red heart
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    HEART("heart"),

    /**
     * A particle effect which is displayed by barriers:
     * <ul>
     * <li>It looks like a red box with a slash through it
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    BARRIER("barrier"),

    /**
     * A particle effect which is displayed when breaking a tool or eggs hit a block:
     * <ul>
     * <li>It looks like a little piece with an item texture
     * </ul>
     */
    ITEM_CRACK("iconcrack"),

    /**
     * A particle effect which is displayed when breaking blocks or sprinting:
     * <ul>
     * <li>It looks like a little piece with a block texture
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    BLOCK_CRACK("blockcrack"),

    /**
     * A particle effect which is displayed when falling:
     * <ul>
     * <li>It looks like a little piece with a block texture
     * <li>This was merged with "block as of 1.13
     * </ul>
     */
    BLOCK_DUST("blockdust"),

    /**
     * A particle effect which is displayed when rain hits the ground:
     * <ul>
     * <li>It looks like a blue droplet
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    WATER_DROP("droplet"),

    /**
     * A particle effect which is displayed by elder guardians:
     * <ul>
     * <li>It looks like the shape of the elder guardian
     * <li>The speed value has no influence on this particle effect
     * </ul>
     */
    MOB_APPEARANCE("mobappearance"),

    /**
     * A particle effect which is displayed by the ender dragon when breathing poison:
     * <ul>
     * <li>It looks like a purple redstone particle
     * </ul>
     */
    DRAGON_BREATH("dragonbreath"),

    /**
     * A particle effect which is displayed by end rods.
     * <ul>
     * <li>It looks like a bright white glow
     * </ul>
     */
    END_ROD("endRod"),

    /**
     * A particle effect which is displayed when damaging a mob:
     * <ul>
     * <li>It looks like a dark red heart
     * </ul>
     */
    DAMAGE_INDICATOR("damageIndicator"),

    /**
     * A particle effect which is displayed when performing a well-timed attack
     * <ul>
     * <li>It looks like a gray/white arc
     * </ul>
     */
    SWEEP_ATTACK("sweepAttack"),

    /**
     * A particle effect which is displayed by sand and other gravity-effected blocks while
     * they are suspended in the air.
     * <ul>
     * <li>It looks like falling particles of the target block
     * </ul>
     */
    FALLING_DUST("fallingdust"),

    /**
     * A particle effect which is displayed when using a totem of undying:
     * <ul>
     * <li>It looks like a green diamond
     * </ul>
     */
    TOTEM("totem"),

    /**
     * A particle effect which is displayed when a llama spits:
     * <ul>
     * <li>It looks like a white cloud
     * </ul>
     */
    SPIT("spit"),

    /**
     * A particle effect which is displayed when a squid shoots out ink
     * <ul>
     * <li>It looks like a cloud of black squares
     * </ul>
     */
    SQUID_INK("squid_ink"),

    /**
     * A particle effect which is displayed at the top bubble columns:
     * <ul>
     * <li>It looks like a bubble popping
     * </ul>
     */
    BUBBLE_POP("bubble_pop"),

    /**
     * A particle effect which is displayed for underwater downward bubble columns:
     * <ul>
     * <li>It looks like a stream of bubbles of going downward
     * </ul>
     */
    CURRENT_DOWN("current_down"),

    /**
     * A particle effect which is displayed by underwater bubble columns:
     * <ul>
     * <li>It looks like a stream of bubbles
     * </ul>
     */
    BUBBLE_COLUMN_UP("bubble_column_up"),

    /**
     * TODO: Document me!
     */
    NAUTILUS("nautilus"),

    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It looks like a transparent gray square
     * <li>The speed value has no influence on this particle effect
     * <li>Removed as of 1.13
     * </ul>
     */
    @Deprecated
    FOOTSTEP("footstep"),

    /**
     * A particle effect which is currently unused:
     * <ul>
     * <li>It has no visual effect
     * <li>This was removed as of 1.13
     * </ul>
     */
    @Deprecated
    ITEM_TAKE("item_take")
    ;

    private Particle particle;
    private final String name;

    private static ParticleDisplay display;
    private static final Map<String, ParticleEffect> NAME_MAP = new HashMap<String, ParticleEffect>();

    // Initialize map for quick name and id lookup
    static {
        for (ParticleEffect effect : values()) {
            NAME_MAP.put(effect.name, effect);
        }
    }

    ParticleEffect(String name) {
        this.name = name;

        try {
            particle = Particle.valueOf(name());
        } catch (Exception ex) {
            particle = null;
        }
    }

    /**
     * Returns the particle effect with the given name
     *
     * @param name Name of the particle effect
     * @return The particle effect
     */
    public static ParticleEffect fromName(String name) {
        for (Map.Entry<String, ParticleEffect> entry : NAME_MAP.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }

    /**
     * Determine if this particle effect is supported by your current server version
     *
     * @return Whether the particle effect is supported or not
     */
    public boolean isSupported() {
        return particle != null;
    }

    /**
     * Displays a particle effect which is only visible for all players within a certain range in the world of @param center
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param range Range of the visibility
     * @param targetPlayers if set, will send this packet to a specific list of players
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range, List<Player> targetPlayers) throws ParticleVersionException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }

        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, 1, null, null, (byte)0, range, targetPlayers);
    }

    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws ParticleVersionException {
        display(offsetX, offsetY, offsetZ, speed, amount, center, range, null);
    }

    /**
     * Displays a particle effect which is only visible for the specified players
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws ParticleVersionException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, 1, null, null, (byte)0, 0, players);
    }

    /**
     * Displays a particle effect which is only visible for the specified players
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @see #display(float, float, float, float, int, Location, List)
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, Player... players) throws ParticleVersionException {
        display(offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for all players within a certain range in the world of @param center
     *
     * @param direction Direction of the particle
     * @param speed Display speed of the particle
     * @param center Center location of the effect
     * @param range Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(Vector direction, float speed, Location center, double range) throws ParticleVersionException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }

        display(particle, center, (float)direction.getX(), (float)direction.getY(), (float)direction.getZ(),
                speed, 1, 1, null, null, (byte)0, range, null);
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for the specified players
     *
     * @param direction Direction of the particle
     * @param speed Display speed of the particle
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(Vector direction, float speed, Location center, List<Player> players) throws ParticleVersionException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }

        display(particle, center, (float)direction.getX(), (float)direction.getY(), (float)direction.getZ(),
                speed, 1, 1, null, null, (byte)0, 0, players);
    }

    /**
     * Displays a single particle which flies into a determined direction and is only visible for the specified players
     *
     * @param direction Direction of the particle
     * @param speed Display speed of the particle
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     * @see #display(Vector, float, Location, List)
     */
    public void display(Vector direction, float speed, Location center, Player... players) throws ParticleVersionException {
        display(direction, speed, center, Arrays.asList(players));
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for all players within a certain range in the world of @param center
     *
     * @param data Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param range Range of the visibility
     * @param targetPlayers if set, will send to a specific set of players
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range, List<Player> targetPlayers) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }

        Material material = null;
        byte materialData = 0;
        if (data != null) {
            material = data.material;
            materialData = data.data;
        }

        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, 1, null, material, materialData, range, targetPlayers);
    }

    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws ParticleVersionException, ParticleDataException {
        display(data, offsetX, offsetY, offsetZ, speed, amount, center, range, null);
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for the specified players
     *
     * @param data Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }

        Material material = null;
        byte materialData = 0;
        if (data != null) {
            material = data.material;
            materialData = data.data;
        }
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, 1, null, material, materialData, 0, players);
    }

    /**
     * Displays a particle effect which requires additional data and is only visible for the specified players
     *
     * @param data Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, Player... players) throws ParticleVersionException, ParticleDataException {
        display(data, offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined direction and is only visible for all players within a certain range in the world of @param center
     *
     * @param data Data of the effect
     * @param direction Direction of the particle
     * @param speed Display speed of the particles
     * @param center Center location of the effect
     * @param range Range of the visibility
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, double range) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }

        Material material = null;
        byte materialData = 0;
        if (data != null) {
            material = data.material;
            materialData = data.data;
        }
        display(particle, center, (float)direction.getX(), (float)direction.getY(), (float)direction.getZ(),
                speed, 1, 1, null, material, materialData, range, null);
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined direction and is only visible for the specified players
     *
     * @param data Data of the effect
     * @param direction Direction of the particle
     * @param speed Display speed of the particles
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, List<Player> players) throws ParticleVersionException, ParticleDataException {
        if (!isSupported()) {
            throw new ParticleVersionException("The " + this + " particle effect is not supported by your server version.");
        }

        Material material = null;
        byte materialData = 0;
        if (data != null) {
            material = data.material;
            materialData = data.data;
        }
        display(particle, center, (float)direction.getX(), (float)direction.getY(), (float)direction.getZ(),
                speed, 1, 1, null, material, materialData, 0, players);
    }

    /**
     * Displays a single particle which requires additional data that flies into a determined direction and is only visible for the specified players
     *
     * @param data Data of the effect
     * @param direction Direction of the particle
     * @param speed Display speed of the particles
     * @param center Center location of the effect
     * @param players Receivers of the effect
     * @throws ParticleVersionException If the particle effect is not supported by the server version
     */
    public void display(ParticleData data, Vector direction, float speed, Location center, Player... players) throws ParticleVersionException, ParticleDataException {
        display(data, direction, speed, center, Arrays.asList(players));
    }

    public void display(Location center, Color color, double range) {
        display(null, center, color, range, 0, 0, 0, 1, 0);
    }

    public void display(ParticleData data, Location center, Color color, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        display(data, center, color, range, offsetX, offsetY, offsetZ, speed, amount, null);
    }

    public void display(ParticleData data, Location center, Color color, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount, List<Player> targetPlayers) {
        Material material = null;
        byte materialData = 0;
        if (data != null) {
            material = data.material;
            materialData = data.data;
        }
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, 1, color, material, materialData, range, targetPlayers);
    }

    public void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, float size, Color color, Material material, byte materialData, double range, List<Player> targetPlayers) {
        if (display == null) {
            display = ParticleDisplay.newInstance();
        }

        display.display(particle, center, offsetX, offsetY, offsetZ, speed, amount, size, color, material, materialData, range, targetPlayers);
    }

    /**
     * Represents the particle data for effects like {@link ParticleEffect#ITEM_CRACK}, {@link ParticleEffect#BLOCK_CRACK} and {@link ParticleEffect#BLOCK_DUST}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    @Deprecated
    public static abstract class ParticleData {

        private final Material material;
        private final byte data;

        public ParticleData(Material material, byte data) {
            this.material = material;
            this.data = data;
        }

        /**
         * Returns the material of this data
         *
         * @return The material
         */
        public Material getMaterial() {
            return material;
        }

        /**
         * Returns the data value of this data
         *
         * @return The data value
         */
        public byte getData() {
            return data;
        }
    }

    /**
     * Represents the item data for the {@link ParticleEffect#ITEM_CRACK} effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    @Deprecated
    public static final class ItemData extends ParticleData {

        /**
         * Construct a new item data
         *
         * @param material Material of the item
         * @param data Data value of the item
         * @see ParticleData#ParticleData(Material, byte)
         */
        @SuppressWarnings("deprecation")
        public ItemData(Material material, byte data) {
            super(material, data);
        }
    }

    /**
     * Represents the block data for the {@link ParticleEffect#BLOCK_CRACK} and {@link ParticleEffect#BLOCK_DUST} effects
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    @Deprecated
    public static final class BlockData extends ParticleData {

        /**
         * Construct a new block data
         *
         * @param material Material of the block
         * @param data Data value of the block
         * @throws IllegalArgumentException If the material is not a block
         * @see ParticleData#ParticleData(Material, byte)
         */
        @SuppressWarnings("deprecation")
        public BlockData(Material material, byte data) throws IllegalArgumentException {
            super(material, data);
            if (!material.isBlock()) {
                throw new IllegalArgumentException("The material is not a block");
            }
        }
    }

    /**
     * Represents a runtime exception that is thrown either if the displayed particle effect requires data and has none or vice-versa or if the data type is wrong
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    @Deprecated
    private static final class ParticleDataException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle data exception
         *
         * @param message Message that will be logged
         */
        public ParticleDataException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown if the displayed particle effect requires a newer version
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    @Deprecated
    private static final class ParticleVersionException extends RuntimeException {

        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle version exception
         *
         * @param message Message that will be logged
         */
        public ParticleVersionException(String message) {
            super(message);
        }
    }
}
