package cn.note.swing.view.note.editor.core.richtext.handler.image;

import java.awt.*;
import java.net.URL;

/**
 * 实现保存图像并返回图像地址
 */
public interface ImageSupport {

    /**
     * 保存图像并返回地址
     *
     * @param image 图像
     * @return 图像地址
     */
    URL saveImage(Image image);


    /**
     * 图片类型
     */
    public enum Type {
        jpg,
        png,
        gif;
    }
}
