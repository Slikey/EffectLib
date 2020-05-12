package de.slikey.effectlib.effect;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.BaseImageEffect;

public class ImageEffect extends BaseImageEffect {

    /**
     * Invert the image
     */
    public boolean invert = false;

    public ImageEffect(EffectManager effectManager) {
        super(effectManager);
    }

    protected void display(BufferedImage image, Vector v, Location location, int pixel) {
        if (!invert && Color.black.getRGB() != pixel) return;
        else if (invert && Color.black.getRGB() == pixel) return;

        display(particle, location.add(v));
        location.subtract(v);
    }

}
