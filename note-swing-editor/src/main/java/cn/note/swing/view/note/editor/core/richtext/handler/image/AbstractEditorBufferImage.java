package cn.note.swing.view.note.editor.core.richtext.handler.image;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @description: 实现保存图片时保存至textPane的缓存区
 * @author: jee
 */
@Slf4j
public abstract class AbstractEditorBufferImage extends AbstractBufferedImageSupport {

    private JEditorPane editorPane;

    private String imageCache;

    public AbstractEditorBufferImage(JEditorPane editorPane) {
        this(editorPane, "imageCache");
    }

    public AbstractEditorBufferImage(JEditorPane editorPane, String cacheName) {
        this.editorPane = editorPane;
        this.imageCache = cacheName;
    }

    @Override
    public URL saveImage(Image image) {
        BufferedImage bufferedImage = toBufferedImage(image);
        URL url = saveBufferedImage(bufferedImage);
        putImageToCache(url, bufferedImage);
        return url;
    }

    /**
     * 添加url和缓存图像至jTextPane
     *
     * @param url           url地址
     * @param bufferedImage 缓存目录
     */
    public void putImageToCache(URL url, BufferedImage bufferedImage) {
        @SuppressWarnings("unchecked")
        Dictionary<URL, BufferedImage> imageCacheDictionary = (Dictionary) editorPane.getDocument().getProperty(imageCache);
        if (imageCacheDictionary == null) {
            // No cache exists, so create a new one.
            imageCacheDictionary = new Hashtable<>();
            editorPane.getDocument().putProperty(imageCache, imageCacheDictionary);
        }
        log.debug("store image url:{}", url);
        imageCacheDictionary.put(url, bufferedImage);

    }
}
