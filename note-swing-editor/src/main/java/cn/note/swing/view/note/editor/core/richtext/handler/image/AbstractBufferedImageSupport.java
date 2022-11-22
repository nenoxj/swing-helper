package cn.note.swing.view.note.editor.core.richtext.handler.image;

import com.vladsch.flexmark.util.misc.ImageUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * @description: 实现了保存缓冲图像
 * @author: jee
 */
@Slf4j
public abstract class AbstractBufferedImageSupport implements ImageSupport {


    /**
     * @param image 图像
     * @return buffered图像
     */
    public BufferedImage toBufferedImage(Image image) {
        return ImageUtils.toBufferedImage(image);
    }

    /**
     * @param bufferedImage 缓冲图像
     * @return 图像地址
     */
    public abstract URL saveBufferedImage(BufferedImage bufferedImage);

    /**
     * 保存图像并返回地址
     *
     * @param image 图像
     * @return 图像地址
     */
    @Override
    public URL saveImage(Image image) {
        return saveBufferedImage(toBufferedImage(image));
    }
}
