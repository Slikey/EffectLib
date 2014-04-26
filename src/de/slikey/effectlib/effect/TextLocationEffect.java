package de.slikey.effectlib.effect;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.StringParser;
import de.slikey.effectlib.util.VectorUtils;

public class TextLocationEffect extends LocationEffect {

	/**
	 * Particle to draw the text
	 */
	public ParticleEffect particle = ParticleEffect.FIREWORKS_SPARK;

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
	
	public Font font;

	protected BufferedImage image = null;

	/**
	 * 
	 * @param effectManager
	 * @param location Location of the point in the middle of the text
	 */
	public TextLocationEffect(EffectManager effectManager, Location location) {
		this(effectManager, location, new Font("Tahoma", Font.PLAIN, 16));
	}

	public TextLocationEffect(EffectManager effectManager, Location location, Font font) {
		super(effectManager, location);
		this.font = font;
		image = StringParser.stringToBufferedImage(font, text);
		type = EffectType.REPEATING;
		period = 40;
		iterations = 20;
	}

	@Override
	public void onRun() {
		int clr = 0;
		if (image == null || realtime)
			image = StringParser.stringToBufferedImage(font, text);
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
