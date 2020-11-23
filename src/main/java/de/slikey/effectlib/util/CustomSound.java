package de.slikey.effectlib.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CustomSound {
    private static boolean initializedReflection;
    private static Method player_playCustomSoundMethod;
    private static Method player_stopSoundMethod;
    private static Method player_stopCustomSoundMethod;

    private Sound sound;
    private String customSound;
    private float volume = 1.0f;
    private float pitch = 1.0f;
    private int range = 0;

    public CustomSound(Sound sound) {
        this.sound = sound;
        this.customSound = null;
    }

    public CustomSound(String key) {
        if (key != null && key.length() > 0) {
            String[] pieces = StringUtils.split(key, ',');
            String soundName = pieces[0];

            if (soundName.indexOf('.') < 0) {
                try {
                    sound = Sound.valueOf(soundName.toUpperCase());
                } catch (Exception ex) {
                    sound = null;
                    customSound = soundName;
                }
            } else {
                customSound = soundName;
            }
            if (pieces.length > 1) {
                try {
                    volume = Float.parseFloat(pieces[1]);
                } catch (Exception ex) {
                    volume = 1;
                }
            }
            if (pieces.length > 2) {
                try {
                    pitch = Float.parseFloat(pieces[2]);
                } catch (Exception ex) {
                    pitch = 1;
                }
            }
            if (pieces.length > 3) {
                try {
                    range = Integer.parseInt(pieces[3]);
                } catch (Exception ex) {
                    range = 0;
                }
            }
        }
    }

    public void stop(Player player) {
        if (sound != null) {
            stopSound(null, player, sound);
        }
        if (customSound != null && !customSound.isEmpty()) {
            stopSound(null, player, customSound);
        }
    }

    public boolean isCustom() {
        return sound == null;
    }

    public String getCustomSound() {
        return customSound;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public String toString() {
        String soundName = sound == null ? customSound : sound.name();
        if (soundName == null) {
            return "";
        }
        return soundName + "," + volume + "," + pitch + "," + range;
    }

    public int hashCode() {
        return (sound == null ? 0 : sound.hashCode())
                + 31 * (Float.floatToIntBits(pitch)
                        + 31 * Float.floatToIntBits(volume));
    }

    public boolean equals(Object other) {
        if (!(other instanceof CustomSound)) return false;

        CustomSound otherEffect = (CustomSound)other;
        return sound != otherEffect.sound || pitch != otherEffect.pitch || volume != otherEffect.volume;
    }

    public int getRange() {
        return range;
    }

    public void play(Plugin plugin, Location sourceLocation) {
        play(plugin, plugin == null ? null : plugin.getLogger(), sourceLocation);
    }

    public void play(Plugin plugin, Logger logger, Location sourceLocation) {
        if (sourceLocation == null || plugin == null) return;

        if (customSound != null) {
            try {
                int range = this.range;
                if (range <= 0) {
                    range = (int)(volume > 1.0 ? (16.0 * volume) : 16.0);
                }
                int rangeSquared = range * range;
                Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
                for (Player player : players) {
                    Location location = player.getLocation();
                    if (location.getWorld().equals(sourceLocation.getWorld()) && location.distanceSquared(sourceLocation) <= rangeSquared) {
                        // player.playSound(sourceLocation, customSound, volume, pitch);
                        playCustomSound(logger, player, sourceLocation, customSound, volume, pitch);
                    }
                }
            } catch (Exception ex) {
                if (logger != null) {
                    logger.warning("Failed to play custom sound: " + customSound);
                }
            }
        }

        if (sound != null) {
            try {
                sourceLocation.getWorld().playSound(sourceLocation, sound, volume, pitch);
            } catch (Exception ex) {
                if (logger != null) {
                    logger.warning("Failed to play sound: " + sound);
                }
            }
        }
    }

    public void play(Plugin plugin, Entity entity) {
        play(plugin, plugin == null ? null : plugin.getLogger(), entity);
    }

    public void play(Plugin plugin, Logger logger, Entity entity) {
        if (entity == null || plugin == null) return;

        Location sourceLocation = entity.getLocation();
        if (customSound != null) {
            try {
                if (range > 0) {
                    int rangeSquared = range * range;
                    Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
                    for (Player player : players) {
                        Location location = player.getLocation();
                        if (location.getWorld().equals(sourceLocation.getWorld()) && location.distanceSquared(sourceLocation) <= rangeSquared) {
                            // player.playSound(sourceLocation, customSound, volume, pitch);
                            playCustomSound(logger, player, sourceLocation, customSound, volume, pitch);
                        }
                    }
                } else if (entity instanceof Player) {
                    Player player = (Player)entity;
                    // player.playSound(sourceLocation, customSound, volume, pitch);
                    playCustomSound(logger, player, sourceLocation, customSound, volume, pitch);

                }
            } catch (Exception ex) {
                if (logger != null) {
                    logger.warning("Failed to play custom sound: " + customSound);
                }
            }
        }

        if (sound != null) {
            try {
                if (entity instanceof Player && range <= 0) {
                    Player player = (Player)entity;
                    player.playSound(sourceLocation, sound, volume, pitch);
                } else if (range > 0) {
                    sourceLocation.getWorld().playSound(sourceLocation, sound, volume, pitch);
                }
            } catch (Exception ex) {
                if (logger != null) {
                    logger.warning("Failed to play sound: " + sound);
                }
            }
        }
    }

    public void setRange(int range) {
        this.range = range;
    }

    private static void initializeReflection(Logger logger) {
        if (!initializedReflection) {
            initializedReflection = true;
            try {
                player_playCustomSoundMethod = Player.class.getMethod("playSound", Location.class, String.class, Float.TYPE, Float.TYPE);
            } catch (Exception ex) {
                if (logger != null) {
                    logger.warning("Failed to bind to custom sound method");
                }
            }
            try {
                player_stopCustomSoundMethod = Player.class.getMethod("stopSound", String.class);
            } catch (Exception ex) {
                if (logger != null) {
                    logger.warning("Failed to bind to stop custom sound method");
                }
            }
            try {
                player_stopSoundMethod = Player.class.getMethod("stopSound", Sound.class);
            } catch (Exception ex) {
                if (logger != null) {
                    logger.warning("Failed to bind to stop sound method");
                }
            }
        }
    }

    public static void stopSound(Logger logger, Player player, String sound) {
        initializeReflection(logger);
        if (player_stopCustomSoundMethod == null) return;
        try {
            player_stopCustomSoundMethod.invoke(player, sound);
        } catch (Exception ex) {
            if (logger != null) {
                logger.log(Level.WARNING, "Failed to stop custom sound: " + sound, ex);
            }
        }
    }

    public static void stopSound(Logger logger, Player player, Sound sound) {
        initializeReflection(logger);
        if (player_stopSoundMethod == null) return;
        try {
            player_stopSoundMethod.invoke(player, sound);
        } catch (Exception ex) {
            if (logger != null) {
                logger.log(Level.WARNING, "Failed to stop sound: " + sound, ex);
            }
        }
    }

    public static void playCustomSound(Logger logger, Player player, Location location, String sound, float volume, float pitch) {
        initializeReflection(logger);
        if (player_playCustomSoundMethod == null) return;
        try {
            player_playCustomSoundMethod.invoke(player, location, sound, volume, pitch);
        } catch (Exception ex) {
            if (logger != null) {
                logger.log(Level.WARNING, "Failed to play custom sound: " + sound, ex);
            }
        }
    }
}
