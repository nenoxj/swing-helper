package cn.note.swing.view.note.editor.core.richtext.handler.image;

import cn.hutool.core.util.IdUtil;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import com.vladsch.flexmark.util.misc.ImageUtils;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description: 实现保存图标至文件
 * @author: jee
 */
@Slf4j
public class EditorRelativeFileImage extends AbstractEditorBufferImage {

    private RelativeFileStore fileStore;

    public EditorRelativeFileImage(JEditorPane editorPane, RelativeFileStore fileStore) {
        super(editorPane);
        this.fileStore = fileStore;
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
        String fileName = IdUtil.fastSimpleUUID() + "." + Type.png.name();
        return saveImage2File(image, fileStore.getFile(fileName), Type.png);
    }

    /**
     * @param image 图片
     * @param file  文件
     * @param type  图片类型
     * @return 图片地址
     */
    public URL saveImage2File(Image image, File file, Type type) {
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
