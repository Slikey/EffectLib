package de.slikey.effectlib.particle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.slikey.effectlib.util.ReflectionHandler;
import de.slikey.effectlib.util.ReflectionHandler.PackageType;
import de.slikey.effectlib.util.ReflectionHandler.PacketType;
import de.slikey.effectlib.util.ReflectionHandler.SubPackageType;

/**
 * DO NOT USE! It is damn slow and I have to do some theory on this!
 * @author Kevin
 *
 */
public final class ParticlePacket {

	private static Method getHandle;
	private static Field playerConnection;
	private static Method sendPacket;

	private static Class<?> packet;
	private static Object obj;
	private static Field fParticle, fX, fY, fZ, fOffX, fOffY, fOffZ, fSpeed, fAmount;

	public static String particle;
	public static float x, y, z, offX, offY, offZ, speed, range;
	public static int amount;

	static {
		try {
			getHandle = ReflectionHandler.getMethod("CraftPlayer", SubPackageType.ENTITY, "getHandle");
			playerConnection = ReflectionHandler.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, "playerConnection");
			sendPacket = ReflectionHandler.getMethod(playerConnection.getType(), "sendPacket", ReflectionHandler.getClass("Packet", PackageType.MINECRAFT_SERVER));

			range = 32;

			packet = PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket();
			obj = ReflectionHandler.getConstructor(packet, String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class).newInstance("", 0, 0, 0, 0, 0, 0, 0, 0);
			fParticle = packet.getDeclaredField("a");
			fParticle.setAccessible(true);
			fX = packet.getDeclaredField("b");
			fX.setAccessible(true);
			fY = packet.getDeclaredField("c");
			fY.setAccessible(true);
			fZ = packet.getDeclaredField("d");
			fZ.setAccessible(true);
			fOffX = packet.getDeclaredField("e");
			fOffX.setAccessible(true);
			fOffY = packet.getDeclaredField("f");
			fOffY.setAccessible(true);
			fOffZ = packet.getDeclaredField("g");
			fOffZ.setAccessible(true);
			fSpeed = packet.getDeclaredField("h");
			fSpeed.setAccessible(true);
			fAmount = packet.getDeclaredField("i");
			fAmount.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ParticlePacket() {
	}

	public static void reset() {
		particle = "";
		x = y = z = offX = offY = offZ = speed = amount = 0;
	}

	public static void setLocation(Location l) {
		x = (float) l.getX();
		y = (float) l.getY();
		z = (float) l.getZ();
	}

	public static void set(ParticleType particle, float x, float y, float z, float offX, float offY, float offZ, float speed, int amount) {
		ParticlePacket.particle = particle.getId();
		ParticlePacket.x = x;
		ParticlePacket.y = y;
		ParticlePacket.z = z;
		ParticlePacket.offX = offX;
		ParticlePacket.offY = offY;
		ParticlePacket.offZ = offZ;
		ParticlePacket.speed = speed;
		ParticlePacket.amount = amount;
		update();
	}

	public static void update() {
		try {
			fParticle.set(obj, particle);
			fX.set(obj, (Float) x);
			fY.set(obj, (Float) y);
			fZ.set(obj, (Float) z);
			fOffX.set(obj, (Float) offX);
			fOffY.set(obj, (Float) offY);
			fOffZ.set(obj, (Float) offZ);
			fSpeed.set(obj, (Float) speed);
			fAmount.set(obj, (Integer) amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<Player> getPlayers(World world) {
		List<Player> players = new ArrayList<Player>();
		float squared = range * range;
		for (Player p : world.getPlayers())
			if (inRange(p.getLocation(), squared))
				players.add(p);
		return players;
	}

	private static boolean inRange(Location l, float squared) {
		return Math.pow(l.getX() - x, 2) + Math.pow(l.getY() - y, 2) + Math.pow(l.getZ() - z, 2) <= squared;
	}

	public static void display(ParticleType p, Location l) {
		reset();
		set(p, (float) l.getX(), (float) l.getY(), (float) l.getZ(), 0, 0, 0, 0, 1);
		display(l.getWorld());
	}

	public static void display(World world) {
		for (Player p : getPlayers(world))
			sendPacket(p);
	}

	private static void sendPacket(Player p) {
		try {
			sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public enum ParticleType {

		HUGE_EXPLOSION("hugeexplosion"),
		LARGE_EXPLODE("largeexplode"),
		FIREWORKS_SPARK("fireworksSpark"),
		BUBBLE("bubble"),
		SUSPEND("suspend"),
		DEPTH_SUSPEND("depthSuspend"),
		TOWN_AURA("townaura"),
		CRIT("crit"),
		MAGIC_CRIT("magicCrit"),
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
		WAKE("wake"),
		LARGE_SMOKE("largesmoke"),
		CLOUD("cloud"),
		RED_DUST("reddust"),
		SNOWBALL_POOF("snowballpoof"),
		DRIP_WATER("dripWater"),
		DRIP_LAVA("dripLava"),
		SNOW_SHOVEL("snowshovel"),
		SLIME("slime"),
		HEART("heart"),
		ANGRY_VILLAGER("angryVillager"),
		HAPPY_VILLAGER("happyVillager");

		private String id;

		private ParticleType(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}
	}
}
