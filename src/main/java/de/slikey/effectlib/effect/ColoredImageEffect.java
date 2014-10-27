package de.slikey.effectlib.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.ChatColor;
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

public class ColoredImageEffect extends Effect{

    /**
     * Particle to draw the image
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

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

    /**
     * Rotation step counter
     */
    protected int rotationStep = 0;

    /**
     * Delay between steps
     */
    protected int delay = 0;

    public ColoredImageEffect(EffectManager effectManager) throws IOException {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 200;
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
        if (image == null) {
            cancel();
            return;
        }
        if(isGif) {
            try {
                image = getImg(step);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (delay == 5) {
                step++;
                delay = 0;
            }
            delay++;
        }
        Location location = getLocation();
        int clr;
        for (int y = 0; y < image.getHeight(); y += stepY) {
            for (int x = 0; x < image.getWidth(); x += stepX) {
                clr = image.getRGB(x, y);
                ParticleEffect colorEffect = particle;
                //Thanks to codename_B for the useful Color->ChatColor class!
                ChatColor cc = matchColor(new Color(image.getRGB(x, y)));
                if(cc.equals(ChatColor.BLACK)||cc.equals(ChatColor.DARK_GRAY))
                    colorEffect = ParticleEffect.SMOKE;
                else if(cc.equals(ChatColor.BLUE)||cc.equals(ChatColor.DARK_BLUE)||cc.equals(ChatColor.AQUA))
                    colorEffect = ParticleEffect.DRIP_WATER;
                else if(cc.equals(ChatColor.GREEN)||cc.equals(ChatColor.DARK_GREEN))
                    colorEffect = ParticleEffect.HAPPY_VILLAGER;
                else if(cc.equals(ChatColor.RED)||cc.equals(ChatColor.DARK_RED))
                    colorEffect = ParticleEffect.RED_DUST;
                else if(cc.equals(ChatColor.GOLD)||cc.equals(ChatColor.YELLOW))
                    colorEffect = ParticleEffect.FLAME;
                else if(cc.equals(ChatColor.DARK_PURPLE)||cc.equals(ChatColor.LIGHT_PURPLE))
                    colorEffect = ParticleEffect.WITCH_MAGIC;
                else if(cc.equals(ChatColor.WHITE)||cc.equals(ChatColor.GRAY))
                    colorEffect = ParticleEffect.SNOW_SHOVEL;

                Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
                if(enableRotation) {
                    double rotX = 0;
                    double rotY = 0;
                    double rotZ = 0;
                    switch(plane){
                        case X: rotX = angularVelocityX * rotationStep; break;
                        case Y: rotY = angularVelocityY * rotationStep; break;
                        case Z: rotZ = angularVelocityZ * rotationStep; break;
                        case XY: rotX = angularVelocityX * rotationStep; rotY = angularVelocityY * rotationStep; break;
                        case XZ: rotX = angularVelocityX * rotationStep; rotZ = angularVelocityZ * rotationStep; break;
                        case XYZ: rotX = angularVelocityX * rotationStep; rotY = angularVelocityY * rotationStep;
                            rotZ = angularVelocityZ * rotationStep; break;
                        case YZ: rotY = angularVelocityY * rotationStep; rotZ = angularVelocityZ * step; break;
                    }
                    VectorUtils.rotateVector(v, rotX, rotY, rotZ);
                }
                colorEffect.display(location.add(v), visibleRange, 0, 0, 0, 0, 2);
                location.subtract(v);
            }
        }
        rotationStep++;
    }

    //Reads gifs
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

    //Thanks to codename_B for the class!
    //https://github.com/codename-B/ChatColorPalette/blob/master/src/ChatColorPalette.java

    private Color c(int r, int g, int b) {
        return new Color(r, g, b);
    }

    private double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    private final Color[] colors = {
            // foreground
            c(0, 0, 0), c(0, 0, 170), c(0, 170, 0),
            c(0, 170, 170), c(170, 0, 0), c(170, 0, 170),
            c(255, 170, 0), c(170, 170, 170), c(85, 85, 85),
            c(85, 85, 255), c(85, 255, 85), c(85, 255, 255),
            c(255, 85, 85), c(255, 85, 255), c(255, 255, 85),
            c(255, 255, 255),
    };

    /**
     * A fuzzy matching function to grab colors that are very close together in RGB values
     * to allow for some slight distortion.
     *
     * @param c1
     * @param c2
     * @return if they are a match
     */
    public boolean areIdentical(Color c1, Color c2) {
        return Math.abs(c1.getRed()-c2.getRed()) <= 5 &&
                Math.abs(c1.getGreen()-c2.getGreen()) <= 5 &&
                Math.abs(c1.getBlue()-c2.getBlue()) <= 5;

    }

    /**
     * Get the index of the closest matching color in the palette to the given
     * color.
     *
     * @param r The red component of the color.
     * @param b The blue component of the color.
     * @param g The green component of the color.
     * @return The index in the palette.
     */
    public ChatColor matchColor(int r, int g, int b) {
        return matchColor(new Color(r, g, b));
    }

    /**
     * Get the index of the closest matching color in the palette to the given
     * color.
     *
     * @param color The Color to match.
     * @return The ChatColor in the palette.
     */
    public ChatColor matchColor(Color color) {
        if (color.getAlpha() < 128) return ChatColor.BLACK;

        int index = 0;
        double best = -1;

        for(int i = 0; i < colors.length; i++) {
            if(areIdentical(colors[i], color)) {
                return ChatColor.values()[i];
            }
        }

        for (int i = 0; i < colors.length; i++) {
            double distance = getDistance(color, colors[i]);
            if (distance < best || best == -1) {
                best = distance;
                index = i;
            }
        }

        // Minecraft has 15 colors
        return ChatColor.values()[index];
    }
}