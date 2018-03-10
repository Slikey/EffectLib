package de.slikey.effectlib.util;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.ColoredImageEffect;
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

public abstract class BaseImageEffect extends Effect {

    /**
     * Particle to draw the image
     */
    public ParticleEffect particle = ParticleEffect.REDSTONE;

    /**
     * For configuration-driven files
     */
    public String fileName = null;

    /**
     * Whether or not to check for transparent pixels
     */
    public boolean transparency = false;

    /**
     * How many ticks to show each frame
     */
    public int frameDelay = 5;

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
    public ColoredImageEffect.Plane plane = ColoredImageEffect.Plane.XYZ;

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
    protected BufferedImage[] images = null;

    /**
     * Step counter
     */
    protected int step = 0;

    /**
     * Rotation step counter
     */
    protected int rotationStep = 0;

    /**
     * Delay between steps
     */
    protected int delay = 0;

    public BaseImageEffect(EffectManager effectManager) throws IOException {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 200;
    }

    @Override
    public void reset() {
        this.step = 0;
        this.rotationStep = 0;
    }

    public void loadFile(File file) {
        try {
            if (file.getName().endsWith(".gif")) {
                ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
                ImageInputStream in = ImageIO.createImageInputStream(file);
                reader.setInput(in);
                int numImages = reader.getNumImages(true);
                images = new BufferedImage[numImages];
                for (int i = 0, count = numImages; i < count; i++) {
                    images[i] = reader.read(i);
                }
            } else {
                images = new BufferedImage[1];
                images[0] = ImageIO.read(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            images = null;
        }
    }

    @Override
    public void onRun() {
        if (images == null && fileName != null) {
            File file;
            if (!fileName.startsWith(File.pathSeparator)) {
                file = new File(effectManager.getOwningPlugin().getDataFolder(), fileName);
            } else {
                file = new File(fileName);
            }
            loadFile(file);
        }
        if (images == null || images.length == 0) {
            cancel();
            return;
        }

        if (delay == frameDelay) {
            step++;
            delay = 0;
        }
        delay++;

        if (step >= images.length) {
            step = 0;
        }
        BufferedImage image = images[step];

        Location location = getLocation();
        for (int y = 0; y < image.getHeight(); y += stepY) {
            for (int x = 0; x < image.getWidth(); x += stepX) {
                Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
                if (enableRotation) {
                    double rotX = 0;
                    double rotY = 0;
                    double rotZ = 0;
                    switch (plane) {
                        case X:
                            rotX = angularVelocityX * rotationStep;
                            break;
                        case Y:
                            rotY = angularVelocityY * rotationStep;
                            break;
                        case Z:
                            rotZ = angularVelocityZ * rotationStep;
                            break;
                        case XY:
                            rotX = angularVelocityX * rotationStep;
                            rotY = angularVelocityY * rotationStep;
                            break;
                        case XZ:
                            rotX = angularVelocityX * rotationStep;
                            rotZ = angularVelocityZ * rotationStep;
                            break;
                        case XYZ:
                            rotX = angularVelocityX * rotationStep;
                            rotY = angularVelocityY * rotationStep;
                            rotZ = angularVelocityZ * rotationStep;
                            break;
                        case YZ:
                            rotY = angularVelocityY * rotationStep;
                            rotZ = angularVelocityZ * step;
                            break;
                    }
                    VectorUtils.rotateVector(v, rotX, rotY, rotZ);
                }
                if (transparency) {
                    int pixel = image.getRGB(x, y);
                    if ((pixel >> 24) == 0) {
                        continue;
                    }
                }

                display(image, v, location, x, y);
                location.subtract(v);
            }
        }
        rotationStep++;
    }

    public enum Plane {

        X, Y, Z, XY, XZ, XYZ, YZ;
    }

    protected abstract void display(BufferedImage image, Vector v, Location location, int x, int y);
}
