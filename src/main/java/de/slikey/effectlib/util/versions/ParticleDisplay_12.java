package de.slikey.effectlib.util.versions;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import de.slikey.effectlib.util.ParticleDisplay;

public class ParticleDisplay_12 extends ParticleDisplay {
    @Override
    @SuppressWarnings("deprecation")
    public void display(Particle particle, Location center, float offsetX, float offsetY, float offsetZ, float speed, int amount, float size, Color color, Material material, byte materialData, double range, List<Player> targetPlayers) {
        // Legacy colorizeable particles
        if (color != null && (particle == Particle.REDSTONE || particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT)) {
            displayLegacyColored(particle, center, speed, color, range, targetPlayers);
            return;
        }

        if (particle == Particle.ITEM_CRACK) {
            displayItem(particle, center, offsetX, offsetY, offsetZ, speed, amount, material, materialData, range, targetPlayers);
            return;
        }

        Object data = null;
        if (particle == Particle.BLOCK_CRACK || particle == Particle.BLOCK_DUST || particle.name().equals("FALLING_DUST")) {
            if (material == null || material == Material.AIR) {
                return;
            }
            data = new MaterialData(material, materialData);
        }

        display(particle, center, offsetX, offsetY, offsetZ, speed, amount, data, range, targetPlayers);
    }
}
