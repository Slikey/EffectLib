package de.slikey.effectlib.util;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import de.slikey.effectlib.util.versions.ParticleDisplay_12;
import de.slikey.effectlib.util.versions.ParticleDisplay_13;

public class ParticleUtils {
    private static ParticleDisplay display;

    private static ParticleDisplay getDisplay() {
        if (display == null) {
            try {
               Particle.valueOf("SQUID_INK");
               display = new ParticleDisplay_13();
            } catch (Throwable not13) {
               display = new ParticleDisplay_12();
            }
        }

        return display;
    }

    public static void display(Particle particle, Location center, Color color, double range) {
        display(particle, center, 0, 0, 0, 0, 0, color, null, range, null);
    }

    public static void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Color color, Material material, double range) {
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, color, material, range, null);
    }

    public static void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Color color, Material material, byte materialData, double range) {
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, 1, color, material, materialData, range, null);
    }

    public static void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Color color, Material material, double range, List<Player> targetPlayers) {
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, 1, color, material, (byte)0, range, targetPlayers);
    }

    public static void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, float size, Color color, Material material, double range, List<Player> targetPlayers) {
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, size, color, material, (byte)0, range, targetPlayers);
    }

    public static void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, float size, Color color, Material material, byte materialData, double range, List<Player> targetPlayers) {
        getDisplay().display(particle, center, offsetX, offsetY, offsetZ, speed, amount, size, color, material, materialData, range, targetPlayers);
    }
}
