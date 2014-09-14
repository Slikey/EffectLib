package de.slikey.effectlib.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.slikey.effectlib.util.ReflectionHandler.PackageType;
import de.slikey.effectlib.util.ReflectionHandler.PacketType;
import de.slikey.effectlib.util.ReflectionHandler.SubPackageType;

/**
 * ParticleEffect Library v1.4
 * 
 * This library was created by @DarkBlade12 based on content related to
 * particles of @microgeek (names and packet values), it allows you to display
 * all Minecraft particle effects on a Bukkit server
 * 
 * You are welcome to use it, modify it and redistribute it under the following
 * conditions: 1. Don't claim this class as your own 2. Don't remove this text
 * 
 * (Would be nice if you provide credit to me)
 * 
 * @author DarkBlade12
 * @changed Slikey
 */
public enum ParticleEffect {
	/**
	 * @appearance Huge explosions
	 * @displayed by TNT and creepers
	 */
	HUGE_EXPLOSION("hugeexplosion"),
	/**
	 * @appearance Smaller explosions
	 * @displayed by TNT and creepers
	 */
	LARGE_EXPLODE("largeexplode"),
	/**
	 * @appearance Little white sparkling stars
	 * @displayed by Fireworks
	 */
	FIREWORKS_SPARK("fireworksSpark"),
	/**
	 * @appearance Bubbles
	 * @displayed in water
	 */
	BUBBLE("bubble"),
	/**
	 * @appearance Unknown
	 */
	SUSPEND("suspend"),
	/**
	 * @appearance Little gray dots
	 * @displayed in the Void and water
	 */
	DEPTH_SUSPEND("depthSuspend"),
	/**
	 * @appearance Little gray dots
	 * @displayed by Mycelium
	 */
	TOWN_AURA("townaura"),
	/**
	 * @appearance Light brown crosses
	 * @displayed by critical hits
	 */
	CRIT("crit"),
	/**
	 * @appearance Cyan stars
	 * @displayed by hits with an enchanted weapon
	 */
	MAGIC_CRIT("magicCrit"),
	/**
	 * @appearance Little black/gray clouds
	 * @displayed by torches, primed TNT and end portals
	 */
	SMOKE("smoke"),
	/**
	 * @appearance Colored swirls
	 * @displayed by potion effects
	 */
	MOB_SPELL("mobSpell"),
	/**
	 * @appearance Transparent colored swirls
	 * @displayed by beacon effect
	 */
	MOB_SPELL_AMBIENT("mobSpellAmbient"),
	/**
	 * @appearance Colored swirls
	 * @displayed by splash potions
	 */
	SPELL("spell"),
	/**
	 * @appearance Colored crosses
	 * @displayed by instant splash potions (instant health/instant damage)
	 */
	INSTANT_SPELL("instantSpell"),
	/**
	 * @appearance Colored crosses
	 * @displayed by witches
	 */
	WITCH_MAGIC("witchMagic"),
	/**
	 * @appearance Colored notes
	 * @displayed by note blocks
	 */
	NOTE("note"),
	/**
	 * @appearance Little purple clouds
	 * @displayed by nether portals, endermen, ender pearls, eyes of ender and
	 *            ender chests
	 */
	PORTAL("portal"),
	/**
	 * @appearance: White letters
	 * @displayed by enchantment tables that are near bookshelves
	 */
	ENCHANTMENT_TABLE("enchantmenttable"),
	/**
	 * @appearance White clouds
	 */
	EXPLODE("explode"),
	/**
	 * @appearance Little flames
	 * @displayed by torches, furnaces, magma cubes and monster spawners
	 */
	FLAME("flame"),
	/**
	 * @appearance Little orange blobs
	 * @displayed by lava
	 */
	LAVA("lava"),
	/**
	 * @appearance Gray transparent squares
	 */
	FOOTSTEP("footstep"),
	/**
	 * @appearance Blue drops
	 * @displayed by water, rain and shaking wolves
	 */
	SPLASH("splash"),
	/**
	 * @appearance Blue droplets
	 * @displayed on water when fishing
	 */
	WAKE("wake"),
	/**
	 * @appearance Black/Gray clouds
	 * @displayed by fire, minecarts with furance and blazes
	 */
	LARGE_SMOKE("largesmoke"),
	/**
	 * @appearance Large white clouds
	 * @displayed on mob death
	 */
	CLOUD("cloud"),
	/**
	 * @appearance Little colored clouds
	 * @displayed by active redstone wires and redstone torches
	 */
	RED_DUST("reddust"),
	/**
	 * @appearance Little white parts
	 * @displayed by cracking snowballs and eggs
	 */
	SNOWBALL_POOF("snowballpoof"),
	/**
	 * @appearance Blue drips
	 * @displayed by blocks below a water source
	 */
	DRIP_WATER("dripWater"),
	/**
	 * @appearance Orange drips
	 * @displayed by blocks below a lava source
	 */
	DRIP_LAVA("dripLava"),
	/**
	 * @appearance White clouds
	 */
	SNOW_SHOVEL("snowshovel"),
	/**
	 * @appearance Little green parts
	 * @displayed by slimes
	 */
	SLIME("slime"),
	/**
	 * @appearance Red hearts
	 * @displayed when breeding
	 */
	HEART("heart"),
	/**
	 * @appearance Dark gray cracked hearts
	 * @displayed when attacking a villager in a village
	 */
	ANGRY_VILLAGER("angryVillager"),
	/**
	 * @appearance Green stars
	 * @displayed by bone meal and when trading with a villager
	 */
	HAPPY_VILLAGER("happyVillager"),
    /**
     * @version 1.8
     * @appearance "No" Sign
     * @displayed by barrier blocks
     */
    BARRIER("barrier"),
    /**
     * @version 1.8
     * @appearance Droplets
     * @displayed ?
     */
    DROPLET("droplet"),
    /**
     * @version 1.8
     * @appearance Take
     * @displayed ?
     */
    TAKE("take"),
    /**
     * @version 1.8
     * @appearance Mob appearance
     * @displayed The appearance of elder guardians
     */
    MOB_APPEARANCE("mobappearance"),

    ICON_CRACK("iconcrack_{subtype}"),
    BLOCK_CRACK("blockcrack_{subtype}"),
    TILE_CRACK("tilecrack_{subtype}");

	private static final Map<String, ParticleEffect> NAME_MAP = new HashMap<String, ParticleEffect>();
	private static final double MAX_RANGE = 50;
	private static Constructor<?> packetPlayOutWorldParticles;
    private static boolean legacy = false;
	private static Method getHandle;
	private static Field playerConnection;
	private static Method sendPacket;
	private final String name;

	static {
		for (ParticleEffect p : values())
			NAME_MAP.put(p.name, p);
		try {
			packetPlayOutWorldParticles = ReflectionHandler.getConstructor(PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket(), String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
			// 1.6 Backwards compatibility
            if (packetPlayOutWorldParticles == null) {
                packetPlayOutWorldParticles = ReflectionHandler.getConstructor(PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket());
                legacy = true;
            }
            getHandle = ReflectionHandler.getMethod("CraftPlayer", SubPackageType.ENTITY, "getHandle");
			playerConnection = ReflectionHandler.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, "playerConnection");
			sendPacket = ReflectionHandler.getMethod(playerConnection.getType(), "sendPacket", ReflectionHandler.getClass("Packet", PackageType.MINECRAFT_SERVER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param name
	 *            Name of this particle effect
	 */
	private ParticleEffect(String name) {
		this.name = name;
	}

	/**
	 * @return The name of this particle effect
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets a particle effect from name
	 * 
	 * @param name
	 *            Name of the particle effect
	 * @return The particle effect
	 */
	public static ParticleEffect fromName(String name) {
		if (name != null)
			for (Entry<String, ParticleEffect> e : NAME_MAP.entrySet())
				if (e.getKey().equalsIgnoreCase(name))
					return e.getValue();
		return null;
	}

	/**
	 * Gets a list of players in a certain range
	 * 
	 * @param center
	 *            Center location
	 * @param range
	 *            Range
	 * @return The list of players in the specified range
	 */
	private static List<Player> getPlayers(Location center, double range) {
		List<Player> players = new ArrayList<Player>();
		double squared = range * range;
		for (Player p : center.getWorld().getPlayers())
			if (p.getLocation().distanceSquared(center) <= squared)
				players.add(p);
		return players;
	}

	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if
	 *         the @PacketPlayOutWorldParticles has changed its name or
	 *         constructor parameters
	 */
	private static Object instantiatePacket(String name, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (amount < 1)
			amount = 1;
		try {
            if (legacy) {
                // This is really slow, but only here for backwards compatibility.
                Object packet = packetPlayOutWorldParticles.newInstance();
                for (Field field : packet.getClass().getDeclaredFields())
                {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (fieldName.equals("a")) {
                        field.set(packet, name);
                    } else if (fieldName.equals("b")) {
                        field.setFloat(packet, (float)center.getX());
                    } else if (fieldName.equals("c")) {
                        field.setFloat(packet, (float)center.getY());
                    } else if (fieldName.equals("d")) {
                        field.setFloat(packet, (float)center.getZ());
                    } else if (fieldName.equals("e")) {
                        field.setFloat(packet, offsetX);
                    } else if (fieldName.equals("f")) {
                        field.setFloat(packet, offsetY);
                    } else if (fieldName.equals("g")) {
                        field.setFloat(packet, offsetZ);
                    } else if (fieldName.equals("h")) {
                        field.setFloat(packet, speed);
                    } else if (fieldName.equals("i")) {
                        field.setInt(packet, amount);
                    }
                }

                return packet;
            }
			return packetPlayOutWorldParticles.newInstance(name, (float) center.getX(), (float) center.getY(), (float) center.getZ(), offsetX, offsetY, offsetZ, speed, amount);
		} catch (Exception e) {
			throw new PacketInstantiationException("Packet instantiation failed", e);
		}
	}

	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection
	 * especially for the "iconcrack" effect
	 * 
	 * @param id
	 *            Id of the icon
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if
	 *         the @PacketPlayOutWorldParticles has changed its name or
	 *         constructor parameters
	 * @see #instantiatePacket
	 */
	private static Object instantiateIconCrackPacket(int id, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		return instantiatePacket("iconcrack_" + id, center, offsetX, offsetY, offsetZ, speed, amount);
	}

	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection
	 * especially for the "blockcrack" effect
	 * 
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param amount
	 *            Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if
	 *         the @PacketPlayOutWorldParticles has changed its name or
	 *         constructor parameters
	 * @see #instantiatePacket
	 */
	private static Object instantiateBlockCrackPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, int amount) {
		return instantiatePacket("blockcrack_" + id + "_" + data, center, offsetX, offsetY, offsetZ, 0, amount);
	}

	/**
	 * Instantiates a new @PacketPlayOutWorldParticles object through reflection
	 * especially for the "blockdust" effect
	 * 
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @return The packet object
	 * @throws #PacketInstantiationException if the amount is lower than 1 or if
	 *         the name or the constructor of @PacketPlayOutWorldParticles have
	 *         changed
	 * @see #instantiatePacket
	 */
	private static Object instantiateBlockDustPacket(int id, byte data, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		return instantiatePacket("blockdust_" + id + "_" + data, center, offsetX, offsetY, offsetZ, speed, amount);
	}

	/**
	 * Sends a packet through reflection to a player
	 * 
	 * @param p
	 *            Receiver of the packet
	 * @param packet
	 *            Packet that is sent
	 * @throws #PacketSendingException if the packet is null or some methods
	 *         which are accessed through reflection have changed
	 */
	private static void sendPacket(Player p, Object packet) {
		try {
			sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), packet);
		} catch (Exception e) {
			throw new PacketSendingException("Failed to send a packet to player '" + p.getName() + "'", e);
		}
	}

	/**
	 * Sends a packet through reflection to a collection of players
	 * 
	 * @param players
	 *            Receivers of the packet
	 * @param packet
	 *            Packet that is sent
	 * @throws #PacketSendingException if the sending to a single player fails
	 * @see #sendPacket
	 */
	private static void sendPacket(Collection<Player> players, Object packet) {
		for (Player p : players)
			sendPacket(p, packet);
	}

	/**
	 * Displays a particle effect which is only visible for all players within a
	 * certain range in the world of @param center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param range
	 *            Range of the visibility
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @throws @IllegalArgumentException if providing a parameterized particle
	 * @see #sendPacket
	 * @see #instantiatePacket
	 */
	public void display(Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (range > MAX_RANGE)
			range = MAX_RANGE;
		// throw new
		// IllegalArgumentException("Range cannot exceed the maximum value of 16");
        if (this == BLOCK_CRACK || this == ICON_CRACK || this == TILE_CRACK) {
            throw new IllegalArgumentException("Missing subtype for parameterized ParticleEffect");
        }
		sendPacket(getPlayers(center, range), instantiatePacket(name, center, offsetX, offsetY, offsetZ, speed, amount));
	}

    /**
     * Display this effect to a specific player.
     *
     * @param player
     *            The player who will see the effect.
     * @param location
     *            Center location of the effect
     * @param offsetX
     *            Maximum distance particles can fly away from the center on the
     *            x-axis
     * @param offsetY
     *            Maximum distance particles can fly away from the center on the
     *            y-axis
     * @param offsetZ
     *            Maximum distance particles can fly away from the center on the
     *            z-axis
     * @param speed
     *            Display speed of the particles
     * @param amount
     *            Amount of particles
     * @throws @IllegalArgumentException if providing a parameterized particle
     */
    public void displayTo(Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (this == BLOCK_CRACK || this == ICON_CRACK || this == TILE_CRACK) {
            throw new IllegalArgumentException("Missing subtype for parameterized ParticleEffect");
        }
        sendPacket(player, instantiatePacket(name, location, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a particle effect to a specific player.
     *
     * This can be used with the parameterized particle types.
     *
     * @param player
     *            The player who will see the effect.
     * @param subtype
     *            The particle subtype, normally an item id
     * @param location
     *            Center location of the effect
     * @param offsetX
     *            Maximum distance particles can fly away from the center on the
     *            x-axis
     * @param offsetY
     *            Maximum distance particles can fly away from the center on the
     *            y-axis
     * @param offsetZ
     *            Maximum distance particles can fly away from the center on the
     *            z-axis
     * @param speed
     *            Display speed of the particles
     * @param amount
     *            Amount of particles
     * @throws @IllegalArgumentException if the range is higher than 20
     * @see #sendPacket
     * @see #instantiatePacket
     */
    public void displayTo(Player player, String subtype, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        String particleName = name;
        if (this == BLOCK_CRACK || this == ICON_CRACK || this == TILE_CRACK) {
            particleName = particleName.replace("{subtype}", subtype);
        }
        sendPacket(player, instantiatePacket(particleName, location, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a particle effect which is only visible for all players within a
     * certain range in the world of @param center
     *
     * This can be used with the parameterized particle types.
     *
     * @param subtype
     *            The particle subtype, normally an item id
     * @param center
     *            Center location of the effect
     * @param range
     *            Range of the visibility
     * @param offsetX
     *            Maximum distance particles can fly away from the center on the
     *            x-axis
     * @param offsetY
     *            Maximum distance particles can fly away from the center on the
     *            y-axis
     * @param offsetZ
     *            Maximum distance particles can fly away from the center on the
     *            z-axis
     * @param speed
     *            Display speed of the particles
     * @param amount
     *            Amount of particles
     * @throws @IllegalArgumentException if the range is higher than 20
     * @see #sendPacket
     * @see #instantiatePacket
     */
    public void display(String subtype, Location center, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            range = MAX_RANGE;
        String particleName = name;
        if (this == BLOCK_CRACK || this == ICON_CRACK || this == TILE_CRACK) {
            particleName = particleName.replace("{subtype}", subtype);
        }
        sendPacket(getPlayers(center, range), instantiatePacket(particleName, center, offsetX, offsetY, offsetZ, speed, amount));
    }

	public void display(Location center, double range) {
		display(center, range, 0, 0, 0, 0, 0);
	}

	/**
	 * Displays a particle effect which is only visible for all players within a
	 * range of 20 in the world of @param center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @see #display(Location, double, float, float, float, float, int)
	 */
	public void display(Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		display(center, MAX_RANGE, offsetX, offsetY, offsetZ, speed, amount);
	}

	/**
	 * Displays an icon crack (item break) particle effect which is only visible
	 * for the specified players
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param id
	 *            Id of the icon
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @param players
	 *            Receivers of the effect
	 * @see #sendPacket
	 * @see #instantiateIconCrackPacket
	 */
	public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
		sendPacket(Arrays.asList(players), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	}

	/**
	 * Displays an icon crack (item break) particle effect which is only visible
	 * for all players within a certain range in the world of @param center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param range
	 *            Range of the visibility
	 * @param id
	 *            Id of the icon
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @throws @IllegalArgumentException if the range is higher than 20
	 * @see #sendPacket
	 * @see #instantiateIconCrackPacket
	 */
	public static void displayIconCrack(Location center, double range, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (range > MAX_RANGE)
			throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
		sendPacket(getPlayers(center, range), instantiateIconCrackPacket(id, center, offsetX, offsetY, offsetZ, speed, amount));
	}

	/**
	 * Displays an icon crack (item break) effect which is visible for all
	 * players whitin the maximum range of 20 blocks in the world of @param
	 * center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param id
	 *            Id of the icon
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @see #displayIconCrack(Location, double, int, float, float, float, float,
	 *      int)
	 */
	public static void displayIconCrack(Location center, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		displayIconCrack(center, MAX_RANGE, id, offsetX, offsetY, offsetZ, speed, amount);
	}

	/**
	 * Displays a block crack (block break) particle effect which is only
	 * visible for the specified players
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param amount
	 *            Amount of particles
	 * @param players
	 *            Receivers of the effect
	 * @see #sendPacket
	 * @see #instantiateBlockCrackPacket
	 */
	public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Player... players) {
		sendPacket(Arrays.asList(players), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	}

	/**
	 * Displays a block crack (block break) particle effect which is only
	 * visible for all players within a certain range in the world of @param
	 * center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param range
	 *            Range of the visibility
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param amount
	 *            Amount of particles
	 * @throws @IllegalArgumentException if the range is higher than 20
	 * @see #sendPacket
	 * @see #instantiateBlockCrackPacket
	 */
	public static void displayBlockCrack(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount) {
		if (range > MAX_RANGE)
			throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
		sendPacket(getPlayers(center, range), instantiateBlockCrackPacket(id, data, center, offsetX, offsetY, offsetZ, amount));
	}

	/**
	 * Displays a block crack (block break) effect which is visible for all
	 * players whitin the maximum range of 20 blocks in the world of @param
	 * center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param amount
	 *            Amount of particles
	 * @see #displayBlockCrack(Location, double, int, byte, float, float, float,
	 *      int)
	 */
	public static void displayBlockCrack(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, int amount) {
		displayBlockCrack(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, amount);
	}

	/**
	 * Displays a block dust particle effect which is only visible for the
	 * specified players
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @param players
	 *            Receivers of the effect
	 * @see #sendPacket
	 * @see #instantiateBlockDustPacket
	 */
	public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
		sendPacket(Arrays.asList(players), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	}

	/**
	 * Displays a block dust particle effect which is only visible for all
	 * players within a certain range in the world of @param center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param range
	 *            Range of the visibility
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @throws @IllegalArgumentException if the range is higher than 20
	 * @see #sendPacket
	 * @see #instantiateBlockDustPacket
	 */
	public static void displayBlockDust(Location center, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		if (range > MAX_RANGE)
			throw new IllegalArgumentException("Range has to be lower/equal the maximum of 16");
		sendPacket(getPlayers(center, range), instantiateBlockDustPacket(id, data, center, offsetX, offsetY, offsetZ, speed, amount));
	}

	/**
	 * Displays a block dust effect which is visible for all players whitin the
	 * maximum range of 20 blocks in the world of @param center
	 * 
	 * @param center
	 *            Center location of the effect
	 * @param id
	 *            Id of the block
	 * @param data
	 *            Data value
	 * @param offsetX
	 *            Maximum distance particles can fly away from the center on the
	 *            x-axis
	 * @param offsetY
	 *            Maximum distance particles can fly away from the center on the
	 *            y-axis
	 * @param offsetZ
	 *            Maximum distance particles can fly away from the center on the
	 *            z-axis
	 * @param speed
	 *            Display speed of the particles
	 * @param amount
	 *            Amount of particles
	 * @see #displayBlockDust(Location, double, int, byte, float, float, float,
	 *      float, int)
	 */
	public static void displayBlockDust(Location center, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		displayBlockDust(center, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
	}

	/**
	 * Represents a runtime exception that can be thrown upon packet
	 * instantiation
	 */
	private static final class PacketInstantiationException extends RuntimeException {
		private static final long serialVersionUID = 3203085387160737484L;

		/**
		 * @param message
		 *            Message that will be logged
		 * @param cause
		 *            Cause of the exception
		 */
		public PacketInstantiationException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/**
	 * Represents a runtime exception that can be thrown upon packet sending
	 */
	private static final class PacketSendingException extends RuntimeException {
		private static final long serialVersionUID = 3203085387160737484L;

		/**
		 * @param message
		 *            Message that will be logged
		 * @param cause
		 *            Cause of the exception
		 */
		public PacketSendingException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}