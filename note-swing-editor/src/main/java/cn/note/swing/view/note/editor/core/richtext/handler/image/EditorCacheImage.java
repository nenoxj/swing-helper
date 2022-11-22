package cn.note.swing.view.note.editor.core.richtext.handler.image;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description:
 *  editor的缓存图像, 无法序列化,只是为展示使用
 * @author: jee
 */
@Slf4j
public class EditorCacheImage extends AbstractEditorBufferImage {

    public EditorCacheImage(JEditorPane editorPane) {
        super(editorPane);
    }

    /**
     * 不实现bufferedImage 直接返回虚拟的url
     *
     * @param bufferedImage 缓冲图像
     * @return 图像地址
     */
    @Override
    public URL saveBufferedImage(BufferedImage bufferedImage) {
        try {
            return new URL("http:\\buffered/" + IdUtil.fastSimpleUUID());
        } catch (MalformedURLException e) {
            log.error("random url fail:{}", e.getMessage());
        }
        return null;
    }

}
