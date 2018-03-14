package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.BaseImageEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColoredImageEffect extends BaseImageEffect {
    public ColoredImageEffect(EffectManager effectManager) {
        super(effectManager);
    }

    protected void display(BufferedImage image, Vector v, Location location, int pixel) {
        Color color = new Color(pixel);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        display(particle, location.add(v), org.bukkit.Color.fromRGB(r, g, b));
        location.subtract(v);
    }
}
