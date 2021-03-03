package de.slikey.effectlib.effect;

import java.awt.Font;
import java.awt.Color;
import java.util.Objects;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.VectorUtils;
import de.slikey.effectlib.util.StringParser;

public class TextEffect extends Effect {

    /**
     * Particle to draw the text
     */
    public Particle particle = Particle.FIREWORKS_SPARK;

    /**
     * Text to display
     */
    public String text = "Text";

    /**
     * Invert the text
     */
    public boolean invert = false;

    /**
     * Each stepX pixel will be shown. Saves packets for lower fontsizes.
     */
    public int stepX = 1;

    /**
     * Each stepY pixel will be shown. Saves packets for lower fontsizes.
     */
    public int stepY = 1;

    /**
     * Scale the font down
     */
    public float size = (float) 1 / 5;

    /**
     * Set this only to true if you are working with changing text. I'll advice
     * the parser to recalculate the BufferedImage every iteration.
     * Recommended FALSE
     */
    public boolean realtime = false;

    /**
     * Font to create the Text
     */
    public Font font;

    /**
     * Contains an image version of the String
     */
    protected BufferedImage image = null;
    
    /**
     * Track the text used most recently when parsing
     */
    private String lastParsedText = null;
    
    /**
     * Track the font used most recently when parsing
     */
    private Font lastParsedFont = null;

    public TextEffect(EffectManager effectManager) {
        super(effectManager);
        font = new Font("Tahoma", Font.PLAIN, 16);
        type = EffectType.REPEATING;
        period = 40;
        iterations = 20;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void onRun() {
        if (font == null) {
            cancel();
            return;
        }
        Location location = getLocation();
        int clr;
        try {
            if (image == null || shouldRecalculateImage()) {
                lastParsedText = text;
                lastParsedFont = font;
                // Use last parsed references instead for additional thread safety
                image = StringParser.stringToBufferedImage(lastParsedFont, lastParsedText);
            }
            for (int y = 0; y < image.getHeight(); y += stepY) {
                for (int x = 0; x < image.getWidth(); x += stepX) {
                    clr = image.getRGB(x, y);
                    if (!invert && Color.black.getRGB() != clr) continue;
                    else if (invert && Color.black.getRGB() == clr) continue;

                    Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0).multiply(size);
                    VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
                    display(particle, location.add(v));
                    location.subtract(v);
                }
            }
        } catch (Exception ex) {
            // This seems to happen on bad characters in strings,
            // I'm choosing to ignore the exception and cancel the effect for now.
            cancel();
        }
    }
    
    private boolean shouldRecalculateImage() {
        // Don't bother if we don't use real time updates
        if (!realtime) return false;
        
        // Text content or font is different, recalculate
        return !Objects.equals(lastParsedText, text) || !Objects.equals(lastParsedFont, font);
    }

}
