package de.slikey.effectlib.effect;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageEffect extends LocationEffect {

    /**
     * Particle to draw the image
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Invert the image
     */
    public boolean invert = false;

    /**
     * Each stepX pixel will be shown. Saves packets for high resolutions.
     */
    public int stepX = 10;

    /**
     * Each stepY pixel will be shown. Saves packets for high resolutions.
     */
    public int stepY = 10;

    /**
     * Scale the image down
     */
    public float size = (float) 1 / 40;

    /**
     * Image as BufferedImage
     */
    protected BufferedImage image = null;

    public ImageEffect(EffectManager effectManager, Location location, File file) throws IOException {
        super(effectManager, location);
        image = ImageIO.read(file);
        type = EffectType.REPEATING;
        period = 10;
        iterations = 60;
    }

    @Override
    public void onRun() {
        int clr;
        for (int y = 0; y < image.getHeight(); y += stepY) {
            for (int x = 0; x < image.getWidth(); x += stepX) {
                clr = image.getRGB(x, y);
                if (!invert && Color.black.getRGB() != clr)
                    continue;
                else if (invert && Color.black.getRGB() == clr)
                    continue;
                Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
                particle.display(location.add(v), visibleRange);
                location.subtract(v);
            }
        }
    }
}
