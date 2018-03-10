package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.BaseImageEffect;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageEffect extends BaseImageEffect {

    /**
     * Invert the image
     */
    public boolean invert = false;

    public ImageEffect(EffectManager effectManager) throws IOException {
        super(effectManager);
    }

    protected void display(BufferedImage image, Vector v, Location location, int x, int y) {
        int clr = image.getRGB(x, y);
        if (!invert && Color.black.getRGB() != clr) {
            return;
        } else if (invert && Color.black.getRGB() == clr) {
            return;
        }
        VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
        display(particle, location.add(v));
    }

}
