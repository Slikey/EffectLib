package de.slikey.effectlib.util;

import java.io.File;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.ColoredImageEffect;

public abstract class BaseImageEffect extends Effect {

    /**
     * Particle to draw the image
     */
    public Particle particle = Particle.REDSTONE;

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
     * Apply a fixed rotation
     */
    public Vector rotation = null;

    /**
     * Should it orient to face the player's direction?
     */
    public boolean orient = true;
    
    /**
     * Should it face in the same direction as the location. Obeying yaw and pitch?
     */
    public boolean orientPitch = false;

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
    protected int stepDelay = 0;

    protected ImageLoadCallback imageLoadCallback;

    public BaseImageEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 200;
    }

    @Override
    public void reset() {
        step = 0;
        rotationStep = 0;
    }

    public void load(String fileName) {
        imageLoadCallback = new ImageLoadCallback() {
            @Override
            public void loaded(BufferedImage[] i) {
                images = i;
                imageLoadCallback = null;
            }
        };
        effectManager.loadImage(fileName, imageLoadCallback);
    }

    public void loadFile(File file) {
        load(file.getName());
    }

    @Override
    public void onRun() {
        if (images == null && imageLoadCallback != null) return;

        if (images == null && fileName != null) {
            load(fileName);
            return;
        }
        if (images == null || images.length == 0) {
            cancel();
            return;
        }

        if (stepDelay == frameDelay) {
            step++;
            stepDelay = 0;
        }
        stepDelay++;

        if (step >= images.length) step = 0;

        BufferedImage image = images[step];

        Location location = getLocation();
        for (int y = 0; y < image.getHeight(); y += stepY) {
            for (int x = 0; x < image.getWidth(); x += stepX) {
                Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                if (rotation != null) {
                    VectorUtils.rotateVector(v, rotation.getX() * MathUtils.degreesToRadians, rotation.getY() * MathUtils.degreesToRadians, rotation.getZ() * MathUtils.degreesToRadians);
                }

                if (orientPitch) VectorUtils.rotateAroundAxisX(v, Math.toRadians(location.getPitch()));
                if (orient) VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

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

                int pixel = image.getRGB(x, y);
                if (transparency && (pixel >> 24) == 0) continue;

                display(image, v, location, pixel);
            }
        }
        rotationStep++;
    }

    public enum Plane {
        X, Y, Z, XY, XZ, XYZ, YZ
    }

    protected abstract void display(BufferedImage image, Vector v, Location location, int pixel);

}
