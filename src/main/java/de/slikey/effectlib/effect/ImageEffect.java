package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageEffect extends Effect {

    /**
     * Particle to draw the image
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * For configuration-driven files
     */
    public String fileName = null;

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
     * Should it rotate?
     */
    public boolean enableRotation = true;

    /**
     * What plane should it rotate?
     */
    public Plane plane = Plane.XYZ;

    /**
     * Turns the cube by this angle each iteration around the x-axis
     */
    public double angularVelocityX = Math.PI / 200;

    /**
     * Turns the cube by this angle each iteration around the y-axis
     */
    public double angularVelocityY = Math.PI / 170;

    /**
     * Turns the cube by this angle each iteration around the z-axis
     */
    public double angularVelocityZ = Math.PI / 155;

    /**
     * Image as BufferedImage
     */
    protected BufferedImage image = null;

    /**
     * Is this a gif image?
     */
    protected boolean isGif = false;

    /**
     * File of the gif if needed
     */
    protected File gifFile = null;

    /**
     * Step counter
     */
    protected int step = 0;

    public ImageEffect(EffectManager effectManager) throws IOException {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 10;
        iterations = 60;
    }

    public void loadFile(File file) {
        try {
            image = ImageIO.read(file);
            this.isGif = file.getName().endsWith(".gif");
            this.gifFile = file;
        } catch (Exception ex) {
            ex.printStackTrace();
            image = null;
        }
    }

    @Override
    public void onRun() {
        if (image == null && fileName != null) {
            loadFile(new File(fileName));
        }
        if (image == null) {
            cancel();
            return;
        }
        if(isGif){
            try {
                image = getImg(step);
            } catch (IOException e) {
                e.printStackTrace();
            }
            step++;
        }
        Location location = getLocation();
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
                display(particle, location.add(v));
                location.subtract(v);
            }
        }
    }

    private BufferedImage getImg(int s) throws IOException{
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
        ImageInputStream in = ImageIO.createImageInputStream(gifFile);
        reader.setInput(in);
        for (int i = 0, count = reader.getNumImages(true); i < count; i++){
            BufferedImage image = reader.read(i);
            images.add(image);
        }
        if(step>=reader.getNumImages(true)) {
            step = 0;
            return images.get(s-1);
        }
        return images.get(s);
    }

    public enum Plane{
        X, Y, Z, XY, XZ, XYZ, YZ;
    }

}
