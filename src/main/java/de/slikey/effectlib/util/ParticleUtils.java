package de.slikey.effectlib.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleUtils {
    public static void display(Particle particle, Location center, Color color, double range) {
        display(particle, center, 0, 0, 0, 0, 0, color, null, range);
    }

    public static void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Color color, Material material, double range) {
        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, color, material, range);
    }

    public static void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, Color color, Material material, double range, List<Player> targetPlayers) {
        // Colorizeable!
        if (color != null && (particle == Particle.REDSTONE || particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT)) {
            amount = 0;
            // Colored particles can't have a speed of 0.
            if (speed == 0) {
                speed = 1;
            }
            offsetX = (float) color.getRed() / 255;
            offsetY = (float) color.getGreen() / 255;
            offsetZ = (float) color.getBlue() / 255;

            // The redstone particle reverts to red if R is 0!
            if (offsetX < Float.MIN_NORMAL) {
                offsetX = Float.MIN_NORMAL;
            }
        }

        if (targetPlayers == null) {
            String worldName = center.getWorld().getName();
            double squared = range * range;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(worldName) || player.getLocation().distanceSquared(center) > squared) {
                    continue;
                }
                player.spawnParticle(particle, center, amount, offsetX, offsetY, offsetZ, speed, material);
            }
        } else {
            for (Player player : targetPlayers) {
                player.spawnParticle(particle, center, amount, offsetX, offsetY, offsetZ, speed, material);
            }
        }
    }
}
