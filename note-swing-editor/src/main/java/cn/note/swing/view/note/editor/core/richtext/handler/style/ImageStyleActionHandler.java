package cn.note.swing.view.note.editor.core.richtext.handler.style;

import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyAction;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;


/**
 * @description: 图片处理
 * @author: jee
 */
@Slf4j
public class ImageStyleActionHandler implements RegisterKeyAction {

    private ExtendedEditor htmlEditor;

    public ImageStyleActionHandler(ExtendedEditor extendedEditor) {
        this.htmlEditor = extendedEditor;
    }

    /**
     * @param url 创建图片元素
     */
    private void createImageElement(@Nonnull String url) {
        String imageTemplate = StrUtil.format("<div style=\"margin-top: 0\"><img src=\"{}\"></div>", url);
        Action action = new HTMLEditorKit.InsertHTMLTextAction(EditorAction.INSERT_IMG_FROM_CLIP.getName(), imageTemplate,
                HTML.Tag.BODY, HTML.Tag.DIV);
        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }


    @Override
    public JComponent getComponent() {
        return htmlEditor;
    }


    /**
     * 创建粘贴板图片
     */
    @KeyAction
    public void insertClipImageAction() {
        this.registerKeyAction(EditorAction.INSERT_IMG_FROM_CLIP, new ConsumerAction((e) -> {
            insertImageFromClip();
            htmlEditor.moveCaretNextLine();
        }));
    }


    private void insertImageFromClip() {
        Image image = ClipboardUtil.getImage();
        if (image != null) {
            try {
                URL imageUrl = htmlEditor.getImageSupport().saveImage(image);
                createImageElement(imageUrl.toString());
            } catch (Exception e) {
                log.error("{} create image error:{}", this.getClass().getSimpleName(), e);
            }
        } else {
            log.warn("{} clip not found Image!!!", this.getClass().getSimpleName());
        }
    }
}
