package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.BaseImageEffect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ColoredImageEffect extends BaseImageEffect {
;
    public ColoredImageEffect(EffectManager effectManager) throws IOException {
        super(effectManager);
    }

    protected void display(BufferedImage image, Vector v, Location location, int x, int y) {
        int r = (new Color(image.getRGB(x, y))).getRed();
        int g = (new Color(image.getRGB(x, y))).getGreen();
        int b = (new Color(image.getRGB(x, y))).getBlue();
        display(particle, location.add(v), org.bukkit.Color.fromRGB(r, g, b));
    }
}
