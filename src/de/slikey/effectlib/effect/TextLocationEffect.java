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
	public ParticleEffect particle = ParticleEffect.FLAME;
	
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
	public int stepX = 2;
	
	/**
	 * Each stepY pixel will be shown. Saves packets for lower fontsizes.
	 */
	public int stepY = 2;
	
	/**
	 * Scale the font down
	 */
	public float size = (float) 1 / 15;

	protected final StringParser parser;

	public TextLocationEffect(EffectManager effectManager, Location location) {
		this(effectManager, location, new Font("Tahoma", Font.PLAIN, 48));
	}

	public TextLocationEffect(EffectManager effectManager, Location location, Font font) {
		super(effectManager, location);
		parser = new StringParser(font);
		type = EffectType.REPEATING;
		period = 10;
		iterations = -1;
	}

	@Override
	public void onRun() {
		int clr = 0;
		BufferedImage image = parser.stringToBufferedImage(text);
		for (int y = 0; y < image.getHeight(); y += stepY) {
			for (int x = 0; x < image.getWidth(); x += stepX) {
				clr = image.getRGB(x, y);
				if (!invert && Color.black.getRGB() != clr)
					continue;
				else if (invert && Color.black.getRGB() == clr)
					continue;
				Vector v = new Vector(x, image.getHeight() - y, 0).multiply(size);
				VectorUtils.rotateAroundAxisY(v, location.getYaw() * MathUtils.degreesToRadians);
				particle.display(location.add(v), visibleRange);
				location.subtract(v);
			}
		}
	}
}
