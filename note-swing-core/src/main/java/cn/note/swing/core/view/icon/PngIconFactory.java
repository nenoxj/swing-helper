package cn.note.swing.core.view.icon;

import cn.hutool.core.io.resource.ResourceUtil;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * 快速构建图标
 * 图片请选择16*16 -32*32
 * <p>
 * 该类的接口只为了分类规划 资源
 *
 * @author: jee
 */
public class PngIconFactory {

    public static Image IMAGE_NOTE = image(Common.note);
    public static Image IMAGE_EXAMPLE = image(Common.example);
    /**
     * 图片例子
     */
    public static Icon ICON_EXAMPLE = imageIcon(Common.example);
    /**
     * 加载中
     */
    public static Icon ICON_LOADING = imageIcon(Common.loading);
    /*
     * 完成
     */
    public static Icon ICON_COMPLETE = imageIcon(EditorImage.complete);
    /**
     * 图片加载失败
     */
    public static Icon ICON_IMAGE_FAIL = imageIcon(EditorImage.imageFail);
    /**
     * 笔记
     */
    public static Icon ICON_NOTE = imageIcon(Common.note);


    public interface Common {
        String note = "images/note.png";
        String loading = "images/loading.gif";
        String example = "images/example.png";
    }

    /**
     * 编辑器图片
     * 用作笔记相关
     */
    public interface EditorImage {
        String complete = "images/editor/complete.png";
        String imageFail = "images/editor/file-failed.png";
    }


    /**
     * 默认图片大小的 icon 图标
     * 参考image方法
     */
    public static Icon imageIcon(String pngPath) {
//        URL imageURL = ResourceUtil.getResource(pngPath);
//        return new ImageIcon(imageURL);
        return new ImageIcon(image(pngPath));
    }


    /**
     * 指定图片大小的icon 图标
     * 参考image方法
     */
    public static Icon imageIcon(String pngPath, int width, int height) {
        return new ImageIcon(image(pngPath, width, height));
    }

    /**
     * @param pngPath 文件路径
     * @return 图片
     */
    public static Image image(String pngPath) {
        URL imageURL = ResourceUtil.getResource(pngPath);
        return new ImageIcon(imageURL).getImage();

    }

    /**
     * @param pngPath 文件路径
     * @param width   宽度
     * @param height  高度
     * @return 图片
     */
    public static Image image(String pngPath, int width, int height) {
        URL imageURL = ResourceUtil.getResource(pngPath);
        return new ImageIcon(imageURL).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);

    }


    /**
     * @param icon   图标
     * @param width  宽度
     * @param height 高度
     * @return 将图标转换为图片
     */
    public static Image iconToImage(Icon icon, int width, int height) {
        if (icon instanceof ImageIcon) {
            ImageIcon imageIcon = (ImageIcon) icon;
            return imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        } else {
            throw new IllegalArgumentException("仅仅支持ImageIcon 转换为Image");
        }
    }

}
