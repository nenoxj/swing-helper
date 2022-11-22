package cn.note.swing.core.util;

import cn.hutool.log.StaticLog;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * 图像工具类
 *
 * @author: jee
 */
public class ImageUtil {

    /**
     * image to bufferedImage
     *
     * @param image 图像
     * @return bufferedImage
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            StaticLog.error("parse Image error", e);
        }

        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();

        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     * 临时保存图像至textPane的缓存目录
     *
     * @param textPane 文本编辑器
     * @param img      图像
     * @param name     名称
     * @return 图像url地址
     * @throws MalformedURLException 图像url生成异常
     */
    public static String saveImageToPaneCache(JTextPane textPane, Image img, String name) throws MalformedURLException {
        @SuppressWarnings("unchecked")
        Dictionary<URL, BufferedImage> imageCacheDictionary = (Dictionary) textPane.getDocument().getProperty("imageCache");
        if (imageCacheDictionary == null) {
            // No cache exists, so create a new one.
            imageCacheDictionary = new Hashtable<>();
            textPane.getDocument().putProperty("imageCache", imageCacheDictionary);
        }
        String url = "http:\\buffered/" + name;
        imageCacheDictionary.put(new URL(url), toBufferedImage(img));
        return url;
    }
}
