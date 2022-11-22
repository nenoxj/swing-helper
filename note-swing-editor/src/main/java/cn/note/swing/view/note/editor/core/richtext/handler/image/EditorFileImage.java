package cn.note.swing.view.note.editor.core.richtext.handler.image;

import cn.hutool.core.util.IdUtil;
import com.vladsch.flexmark.util.misc.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description: 实现保存图标至文件
 * @author: jee
 */
@Slf4j
public class EditorFileImage extends AbstractEditorBufferImage {

    private String imageDirPath;

    public EditorFileImage(JEditorPane editorPane, String imageDirPath) {
        super(editorPane);
        this.imageDirPath = imageDirPath;
    }

    /**
     * @param bufferedImage 缓冲图像
     * @return 图像地址
     */
    @Override
    public URL saveBufferedImage(BufferedImage bufferedImage) {
        return null;
    }

    @Override
    public URL saveImage(Image image) {
        File imageDir = FileUtils.getFile(imageDirPath);
        try {
            if (!imageDir.exists()) {
                FileUtils.forceMkdir(imageDir);
            }
            String fileName = IdUtil.fastSimpleUUID() + "." + ImageSupport.Type.png.name();
            File imageFile = FileUtils.getFile(imageDir, fileName);
            return saveImage2File(image, imageFile, ImageSupport.Type.png);
        } catch (IOException e) {
            log.error("创建图片失败:{}", e);
        }
        return null;
    }

    /**
     * @param image 图片
     * @param file  文件
     * @param type  图片类型
     * @return 图片地址
     */
    public URL saveImage2File(Image image, File file, ImageSupport.Type type) {
        try {
            ImageUtils.save(toBufferedImage(image), file, type.name());
            URL url = file.toURI().toURL();
            log.info("save file url:{}", url);
            return url;
        } catch (MalformedURLException e) {
            log.error("save image error:{} !!!,file:{}", e.getMessage(), file.getAbsolutePath());
        }
        return null;
    }
}
