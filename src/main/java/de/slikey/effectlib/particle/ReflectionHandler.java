package de.slikey.effectlib.particle;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

// TODO: This is out of date, but still used by ParticlePacket
// Since this looked experimental I didn't want to mess with it.
// If ParticlePacket doesn't work out, this file could be removed as it
// has been replaced by ReflectionUtils.

/**
 * ReflectionHandler v1.0
 * 
 * This class makes dealing with reflection much easier, especially when working
 * with Bukkit
 * 
 * You are welcome to use it, modify it and redistribute it under the following
 * conditions: 1. Don't claim this class as your own 2. Don't remove this text
 * 
 * (Would be nice if you provide credit to me)
 * 
 * @author DarkBlade12
 */
public final class ReflectionHandler {
	private ReflectionHandler() {
	}

	public static Class<?> getClass(String name, PackageType type) throws Exception {
		return Class.forName(type + "." + name);
	}

	public static Class<?> getClass(String name, SubPackageType type) throws Exception {
		return Class.forName(type + "." + name);
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) {
		Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		for (Constructor<?> c : clazz.getConstructors())
			if (DataType.equalsArray(DataType.convertToPrimitive(c.getParameterTypes()), p))
				return c;
		return null;
	}

	public static Constructor<?> getConstructor(String className, PackageType type, Class<?>... parameterTypes) throws Exception {
		return getConstructor(getClass(className, type), parameterTypes);
	}

	public static Constructor<?> getConstructor(String className, SubPackageType type, Class<?>... parameterTypes) throws Exception {
		return getConstructor(getClass(className, type), parameterTypes);
	}

	public static Object newInstance(Class<?> clazz, Object... args) throws Exception {
		return getConstructor(clazz, DataType.convertToPrimitive(args)).newInstance(args);
	}

	public static Object newInstance(String className, PackageType type, Object... args) throws Exception {
		return newInstance(getClass(className, type), args);
	}

	public static Object newInstance(String className, SubPackageType type, Object... args) throws Exception {
		return newInstance(getClass(className, type), args);
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		Class<?>[] p = DataType.convertToPrimitive(parameterTypes);
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name) && DataType.equalsArray(DataType.convertToPrimitive(m.getParameterTypes()), p))
				return m;
		return null;
	}

	public static Method getMethod(String className, PackageType type, String name, Class<?>... parameterTypes) throws Exception {
		return getMethod(getClass(className, type), name, parameterTypes);
	}

	public static Method getMethod(String className, SubPackageType type, String name, Class<?>... parameterTypes) throws Exception {
		return getMethod(getClass(className, type), name, parameterTypes);
	}

	public static Object invokeMethod(String name, Object instance, Object... args) throws Exception {
		return getMethod(instance.getClass(), name, DataType.convertToPrimitive(args)).invoke(instance, args);
	}

	public static Object invokeMethod(Class<?> clazz, String name, Object instance, Object... args) throws Exception {
		return getMethod(clazz, name, DataType.convertToPrimitive(args)).invoke(instance, args);
	}

	public static Object invokeMethod(String className, PackageType type, String name, Object instance, Object... args) throws Exception {
		return invokeMethod(getClass(className, type), name, instance, args);
	}

	public static Object invokeMethod(String className, SubPackageType type, String name, Object instance, Object... args) throws Exception {
		return invokeMethod(getClass(className, type), name, instance, args);
	}

	public static Field getField(Class<?> clazz, String name) throws Exception {
		Field f = clazz.getField(name);
		f.setAccessible(true);
		return f;
	}

	public static Field getField(String className, PackageType type, String name) throws Exception {
		return getField(getClass(className, type), name);
	}

	public static Field getField(String className, SubPackageType type, String name) throws Exception {
		return getField(getClass(className, type), name);
	}

	public static Field getDeclaredField(Class<?> clazz, String name) throws Exception {
		Field f = clazz.getDeclaredField(name);
		f.setAccessible(true);
		return f;
	}

	public static Field getDeclaredField(String className, PackageType type, String name) throws Exception {
		return getDeclaredField(getClass(className, type), name);
	}

	public static Field getDeclaredField(String className, SubPackageType type, String name) throws Exception {
		return getDeclaredField(getClass(className, type), name);
	}

	public static Object getValue(Object instance, String fieldName) throws Exception {
		return getField(instance.getClass(), fieldName).get(instance);
	}

	public static Object getValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		return getField(clazz, fieldName).get(instance);
	}

	public static Object getValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		return getValue(getClass(className, type), instance, fieldName);
	}

	public static Object getValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		return getValue(getClass(className, type), instance, fieldName);
	}

	public static Object getDeclaredValue(Object instance, String fieldName) throws Exception {
		return getDeclaredField(instance.getClass(), fieldName).get(instance);
	}

	public static Object getDeclaredValue(Class<?> clazz, Object instance, String fieldName) throws Exception {
		return getDeclaredField(clazz, fieldName).get(instance);
	}

	public static Object getDeclaredValue(String className, PackageType type, Object instance, String fieldName) throws Exception {
		return getDeclaredValue(getClass(className, type), instance, fieldName);
	}

	public static Object getDeclaredValue(String className, SubPackageType type, Object instance, String fieldName) throws Exception {
		return getDeclaredValue(getClass(className, type), instance, fieldName);
	}

	public static void setValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getField(instance.getClass(), fieldName);
		f.set(instance, fieldValue);
	}

	public static void setValue(Object instance, FieldPair pair) throws Exception {
		setValue(instance, pair.getName(), pair.getValue());
	}

	public static void setValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getField(clazz, fieldName);
		f.set(instance, fieldValue);
	}

	public static void setValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		setValue(clazz, instance, pair.getName(), pair.getValue());
	}

	public static void setValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		setValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		setValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setValues(Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setValue(instance, pair);
	}

	public static void setValues(Class<?> clazz, Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setValue(clazz, instance, pair);
	}

	public static void setValues(String className, PackageType type, Object instance, FieldPair... pairs) throws Exception {
		setValues(getClass(className, type), instance, pairs);
	}

	public static void setValues(String className, SubPackageType type, Object instance, FieldPair... pairs) throws Exception {
		setValues(getClass(className, type), instance, pairs);
	}

	public static void setDeclaredValue(Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getDeclaredField(instance.getClass(), fieldName);
		f.set(instance, fieldValue);
	}

	public static void setDeclaredValue(Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValue(Class<?> clazz, Object instance, String fieldName, Object fieldValue) throws Exception {
		Field f = getDeclaredField(clazz, fieldName);
		f.set(instance, fieldValue);
	}

	public static void setDeclaredValue(Class<?> clazz, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(clazz, instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValue(String className, PackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setDeclaredValue(String className, PackageType type, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValue(String className, SubPackageType type, Object instance, String fieldName, Object fieldValue) throws Exception {
		setDeclaredValue(getClass(className, type), instance, fieldName, fieldValue);
	}

	public static void setDeclaredValue(String className, SubPackageType type, Object instance, FieldPair pair) throws Exception {
		setDeclaredValue(className, type, instance, pair.getName(), pair.getValue());
	}

	public static void setDeclaredValues(Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setDeclaredValue(instance, pair);
	}

	public static void setDeclaredValues(Class<?> clazz, Object instance, FieldPair... pairs) throws Exception {
		for (FieldPair pair : pairs)
			setDeclaredValue(clazz, instance, pair);
	}

	public static void setDeclaredValues(String className, PackageType type, Object instance, FieldPair... pairs) throws Exception {
		setDeclaredValues(getClass(className, type), instance, pairs);
	}

	public static void setDeclaredValues(String className, SubPackageType type, Object instance, FieldPair... pairs) throws Exception {
		setDeclaredValues(getClass(className, type), instance, pairs);
	}

	/**
	 * This class is part of the ReflectionHandler and follows the same usage
	 * conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum DataType {
		BYTE(byte.class, Byte.class),
		SHORT(short.class, Short.class),
		INTEGER(int.class, Integer.class),
		LONG(long.class, Long.class),
		CHARACTER(char.class, Character.class),
		FLOAT(float.class, Float.class),
		DOUBLE(double.class, Double.class),
		BOOLEAN(boolean.class, Boolean.class);

		private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<Class<?>, DataType>();
		private final Class<?> primitive;
		private final Class<?> reference;

		static {
			for (DataType t : values()) {
				CLASS_MAP.put(t.primitive, t);
				CLASS_MAP.put(t.reference, t);
			}
		}

		private DataType(Class<?> primitive, Class<?> reference) {
			this.primitive = primitive;
			this.reference = reference;
		}

		public Class<?> getPrimitive() {
			return this.primitive;
		}

		public Class<?> getReference() {
			return this.reference;
		}

		public static DataType fromClass(Class<?> c) {
			return CLASS_MAP.get(c);
		}

		public static Class<?> getPrimitive(Class<?> c) {
			DataType t = fromClass(c);
			return t == null ? c : t.getPrimitive();
		}

		public static Class<?> getReference(Class<?> c) {
			DataType t = fromClass(c);
			return t == null ? c : t.getReference();
		}

		public static Class<?>[] convertToPrimitive(Class<?>[] classes) {
			int length = classes == null ? 0 : classes.length;
			Class<?>[] types = new Class<?>[length];
			for (int i = 0; i < length; i++)
				types[i] = getPrimitive(classes[i]);
			return types;
		}

		public static Class<?>[] convertToPrimitive(Object[] objects) {
			int length = objects == null ? 0 : objects.length;
			Class<?>[] types = new Class<?>[length];
			for (int i = 0; i < length; i++)
				types[i] = getPrimitive(objects[i].getClass());
			return types;
		}

		public static boolean equalsArray(Class<?>[] a1, Class<?>[] a2) {
			if (a1 == null || a2 == null || a1.length != a2.length)
				return false;
			for (int i = 0; i < a1.length; i++)
				if (!a1[i].equals(a2[i]) && !a1[i].isAssignableFrom(a2[i]))
					return false;
			return true;
		}
	}

	/**
	 * This class is part of the ReflectionHandler and follows the same usage
	 * conditions
	 * 
	 * @author DarkBlade12
	 */
	public final class FieldPair {
		private final String name;
		private final Object value;

		public FieldPair(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return this.name;
		}

		public Object getValue() {
			return this.value;
		}
	}

	/**
	 * This class is part of the ReflectionHandler and follows the same usage
	 * conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum PackageType {
		MINECRAFT_SERVER("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23)),
		CRAFTBUKKIT(Bukkit.getServer().getClass().getPackage().getName());

		private final String name;

		private PackageType(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * This class is part of the ReflectionHandler and follows the same usage
	 * conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum SubPackageType {
		BLOCK,
		CHUNKIO,
		COMMAND,
		CONVERSATIONS,
		ENCHANTMENS,
		ENTITY,
		EVENT,
		GENERATOR,
		HELP,
		INVENTORY,
		MAP,
		METADATA,
		POTION,
		PROJECTILES,
		SCHEDULER,
		SCOREBOARD,
		UPDATER,
		UTIL;

		private final String name;

		private SubPackageType() {
			name = PackageType.CRAFTBUKKIT + "." + name().toLowerCase();
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * This class is part of the ReflectionHandler and follows the same usage
	 * conditions
	 * 
	 * @author DarkBlade12
	 */
	public enum PacketType {
		HANDSHAKING_IN_SET_PROTOCOL("PacketHandshakingInSetProtocol"),
		LOGIN_IN_ENCRYPTION_BEGIN("PacketLoginInEncryptionBegin"),
		LOGIN_IN_START("PacketLoginInStart"),
		LOGIN_OUT_DISCONNECT("PacketLoginOutDisconnect"),
		LOGIN_OUT_ENCRYPTION_BEGIN("PacketLoginOutEncryptionBegin"),
		LOGIN_OUT_SUCCESS("PacketLoginOutSuccess"),
		PLAY_IN_ABILITIES("PacketPlayInAbilities"),
		PLAY_IN_ARM_ANIMATION("PacketPlayInArmAnimation"),
		PLAY_IN_BLOCK_DIG("PacketPlayInBlockDig"),
		PLAY_IN_BLOCK_PLACE("PacketPlayInBlockPlace"),
		PLAY_IN_CHAT("PacketPlayInChat"),
		PLAY_IN_CLIENT_COMMAND("PacketPlayInClientCommand"),
		PLAY_IN_CLOSE_WINDOW("PacketPlayInCloseWindow"),
		PLAY_IN_CUSTOM_PAYLOAD("PacketPlayInCustomPayload"),
		PLAY_IN_ENCHANT_ITEM("PacketPlayInEnchantItem"),
		PLAY_IN_ENTITY_ACTION("PacketPlayInEntityAction"),
		PLAY_IN_FLYING("PacketPlayInFlying"),
		PLAY_IN_HELD_ITEM_SLOT("PacketPlayInHeldItemSlot"),
		PLAY_IN_KEEP_ALIVE("PacketPlayInKeepAlive"),
		PLAY_IN_LOOK("PacketPlayInLook"),
		PLAY_IN_POSITION("PacketPlayInPosition"),
		PLAY_IN_POSITION_LOOK("PacketPlayInPositionLook"),
		PLAY_IN_SET_CREATIVE_SLOT("PacketPlayInSetCreativeSlot "),
		PLAY_IN_SETTINGS("PacketPlayInSettings"),
		PLAY_IN_STEER_VEHICLE("PacketPlayInSteerVehicle"),
		PLAY_IN_TAB_COMPLETE("PacketPlayInTabComplete"),
		PLAY_IN_TRANSACTION("PacketPlayInTransaction"),
		PLAY_IN_UPDATE_SIGN("PacketPlayInUpdateSign"),
		PLAY_IN_USE_ENTITY("PacketPlayInUseEntity"),
		PLAY_IN_WINDOW_CLICK("PacketPlayInWindowClick"),
		PLAY_OUT_ABILITIES("PacketPlayOutAbilities"),
		PLAY_OUT_ANIMATION("PacketPlayOutAnimation"),
		PLAY_OUT_ATTACH_ENTITY("PacketPlayOutAttachEntity"),
		PLAY_OUT_BED("PacketPlayOutBed"),
		PLAY_OUT_BLOCK_ACTION("PacketPlayOutBlockAction"),
		PLAY_OUT_BLOCK_BREAK_ANIMATION("PacketPlayOutBlockBreakAnimation"),
		PLAY_OUT_BLOCK_CHANGE("PacketPlayOutBlockChange"),
		PLAY_OUT_CHAT("PacketPlayOutChat"),
		PLAY_OUT_CLOSE_WINDOW("PacketPlayOutCloseWindow"),
		PLAY_OUT_COLLECT("PacketPlayOutCollect"),
		PLAY_OUT_CRAFT_PROGRESS_BAR("PacketPlayOutCraftProgressBar"),
		PLAY_OUT_CUSTOM_PAYLOAD("PacketPlayOutCustomPayload"),
		PLAY_OUT_ENTITY("PacketPlayOutEntity"),
		PLAY_OUT_ENTITY_DESTROY("PacketPlayOutEntityDestroy"),
		PLAY_OUT_ENTITY_EFFECT("PacketPlayOutEntityEffect"),
		PLAY_OUT_ENTITY_EQUIPMENT("PacketPlayOutEntityEquipment"),
		PLAY_OUT_ENTITY_HEAD_ROTATION("PacketPlayOutEntityHeadRotation"),
		PLAY_OUT_ENTITY_LOOK("PacketPlayOutEntityLook"),
		PLAY_OUT_ENTITY_METADATA("PacketPlayOutEntityMetadata"),
		PLAY_OUT_ENTITY_STATUS("PacketPlayOutEntityStatus"),
		PLAY_OUT_ENTITY_TELEPORT("PacketPlayOutEntityTeleport"),
		PLAY_OUT_ENTITY_VELOCITY("PacketPlayOutEntityVelocity"),
		PLAY_OUT_EXPERIENCE("PacketPlayOutExperience"),
		PLAY_OUT_EXPLOSION("PacketPlayOutExplosion"),
		PLAY_OUT_GAME_STATE_CHANGE("PacketPlayOutGameStateChange"),
		PLAY_OUT_HELD_ITEM_SLOT("PacketPlayOutHeldItemSlot"),
		PLAY_OUT_KEEP_ALIVE("PacketPlayOutKeepAlive"),
		PLAY_OUT_KICK_DISCONNECT("PacketPlayOutKickDisconnect"),
		PLAY_OUT_LOGIN("PacketPlayOutLogin"),
		PLAY_OUT_MAP("PacketPlayOutMap"),
		PLAY_OUT_MAP_CHUNK("PacketPlayOutMapChunk"),
		PLAY_OUT_MAP_CHUNK_BULK("PacketPlayOutMapChunkBulk"),
		PLAY_OUT_MULTI_BLOCK_CHANGE("PacketPlayOutMultiBlockChange"),
		PLAY_OUT_NAMED_ENTITY_SPAWN("PacketPlayOutNamedEntitySpawn"),
		PLAY_OUT_NAMED_SOUND_EFFECT("PacketPlayOutNamedSoundEffect"),
		PLAY_OUT_OPEN_SIGN_EDITOR("PacketPlayOutOpenSignEditor"),
		PLAY_OUT_OPEN_WINDOW("PacketPlayOutOpenWindow"),
		PLAY_OUT_PLAYER_INFO("PacketPlayOutPlayerInfo"),
		PLAY_OUT_POSITION("PacketPlayOutPosition"),
		PLAY_OUT_REL_ENTITY_MOVE("PacketPlayOutRelEntityMove"),
		PLAY_OUT_REL_ENTITY_MOVE_LOOK("PacketPlayOutRelEntityMoveLook"),
		PLAY_OUT_REMOVE_ENTITY_EFFECT("PacketPlayOutRemoveEntityEffect"),
		PLAY_OUT_RESPAWN("PacketPlayOutRespawn"),
		PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE("PacketPlayOutScoreboardDisplayObjective"),
		PLAY_OUT_SCOREBOARD_OBJECTIVE("PacketPlayOutScoreboardObjective"),
		PLAY_OUT_SCOREBOARD_SCORE("PacketPlayOutScoreboardScore"),
		PLAY_OUT_SCOREBOARD_TEAM("PacketPlayOutScoreboardTeam"),
		PLAY_OUT_SET_SLOT("PacketPlayOutSetSlot"),
		PLAY_OUT_SPAWN_ENTITY("PacketPlayOutSpawnEntity"),
		PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB("PacketPlayOutSpawnEntityExperienceOrb"),
		PLAY_OUT_SPAWN_ENTITY_LIVING("PacketPlayOutSpawnEntityLiving"),
		PLAY_OUT_SPAWN_ENTITY_PAINTING("PacketPlayOutSpawnEntityPainting"),
		PLAY_OUT_SPAWN_ENTITY_WEATHER("PacketPlayOutSpawnEntityWeather"),
		PLAY_OUT_SPAWN_POSITION("PacketPlayOutSpawnPosition"),
		PLAY_OUT_STATISTIC("PacketPlayOutStatistic"),
		PLAY_OUT_TAB_COMPLETE("PacketPlayOutTabComplete"),
		PLAY_OUT_TILE_ENTITY_DATA("PacketPlayOutTileEntityData"),
		PLAY_OUT_TRANSACTION("PacketPlayOutTransaction"),
		PLAY_OUT_UPDATE_ATTRIBUTES("PacketPlayOutUpdateAttributes"),
		PLAY_OUT_UPDATE_HEALTH("PacketPlayOutUpdateHealth"),
		PLAY_OUT_UPDATE_SIGN("PacketPlayOutUpdateSign"),
		PLAY_OUT_UPDATE_TIME("PacketPlayOutUpdateTime"),
		PLAY_OUT_WINDOW_ITEMS("PacketPlayOutWindowItems"),
		PLAY_OUT_WORLD_EVENT("PacketPlayOutWorldEvent"),
		PLAY_OUT_WORLD_PARTICLES("PacketPlayOutWorldParticles", "Packet63WorldParticles"),
		STATUS_IN_PING("PacketStatusInPing"),
		STATUS_IN_START("PacketStatusInStart"),
		STATUS_OUT_PONG("PacketStatusOutPong"),
		STATUS_OUT_SERVER_INFO("PacketStatusOutServerInfo");

		private final String name;
        private final String legacy;
		private Class<?> packet;

		private PacketType(String name) {
			this.name = name;
            this.legacy = null;
		}

        private PacketType(String name, String legacy) {
            this.name = name;
            this.legacy = legacy;
        }

		public String getName() {
			return this.getName();
		}

		public Class<?> getPacket() throws Exception {
            if (packet == null) {
                try {
                    packet = ReflectionHandler.getClass(name, PackageType.MINECRAFT_SERVER);
                } catch(Exception ex) {
                    if (legacy == null) {
                        throw ex;
                    }
                    packet = ReflectionHandler.getClass(legacy, PackageType.MINECRAFT_SERVER);
                }
            }
            return packet;
		}
	}
}