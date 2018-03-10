package de.slikey.effectlib.util;

import de.slikey.effectlib.EffectManager;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

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
        if (fileName.startsWith("http")) {
            try {
                File cacheFolder = effectManager.getImageCacheFolder();
                if (cacheFolder == null) {
                    effectManager.getOwningPlugin().getLogger().log(Level.WARNING, "Can't load from URL because no cache folder has been set by the owning plugin: " + fileName);
                    callback.loaded(new BufferedImage[0]);
                    return;
                }

                String cacheFileName = URLEncoder.encode(fileName, "UTF-8");
                imageFile = cacheFolder != null ? new File(cacheFolder, cacheFileName) : null;
                if (!imageFile.exists()) {
                    URL imageUrl = new URL(fileName);
                    HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setInstanceFollowRedirects(true);
                    InputStream in = conn.getInputStream();
                    OutputStream out = new FileOutputStream(imageFile);
                    byte[] buffer = new byte[10 * 1024];
                    int len;

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                }
            } catch (Exception ex) {
                effectManager.getOwningPlugin().getLogger().log(Level.WARNING, "Failed to load file " + fileName, ex);
                callback.loaded(new BufferedImage[0]);
                return;
            }
        } else if (!fileName.startsWith(File.pathSeparator)) {
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
            effectManager.getOwningPlugin().getLogger().log(Level.WARNING, "Failed to load file " + fileName, ex);
            images = new BufferedImage[0];
        }

        callback.loaded(images);
    }
}
