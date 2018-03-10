package de.slikey.effectlib.util;

import de.slikey.effectlib.EffectManager;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageLoadTask implements Runnable {
    private final String fileName;
    private final ImageLoadCallback callback;
    private final EffectManager effectManager;

    public ImageLoadTask(EffectManager manager, String fileName, ImageLoadCallback callback) {
        this.fileName = fileName;
        this.callback = callback;
        this.effectManager = manager;
    }

    @Override
    public void run() {
        BufferedImage[] images;
        File imageFile;
        if (!fileName.startsWith(File.pathSeparator)) {
            imageFile = new File(effectManager.getOwningPlugin().getDataFolder(), fileName);
        } else {
            imageFile = new File(fileName);
        }
        try {
            if (fileName.endsWith(".gif")) {
                ImageReader reader = ImageIO.getImageReadersBySuffix("GIF").next();
                ImageInputStream in = ImageIO.createImageInputStream(imageFile);
                reader.setInput(in);
                int numImages = reader.getNumImages(true);
                images = new BufferedImage[numImages];
                for (int i = 0, count = numImages; i < count; i++) {
                    images[i] = reader.read(i);
                }
            } else {
                images = new BufferedImage[1];
                images[0] = ImageIO.read(imageFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            images = new BufferedImage[0];
        }

        callback.loaded(images);
    }
}
