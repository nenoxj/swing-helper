package cn.note.swing.view.note.editor.core.richtext.handler.hyperlink;

import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @description: 双击打开超链接事件
 * @author: jee
 */
@Slf4j
public class HyperlinkMouseHandler extends MouseAdapter {

    private ExtendedEditor htmlEditor;

    public HyperlinkMouseHandler(ExtendedEditor textPane) {
        this.htmlEditor = textPane;
        this.htmlEditor.addMouseListener(this);
        this.htmlEditor.addMouseMotionListener(this);
    }

    private String href;
    private Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private Cursor defaultCursor = Cursor.getDefaultCursor();

    @Override
    public void mouseClicked(MouseEvent e) {
        if (href != null) {
            try {
                Desktop.getDesktop().browse(new URI(href));
            } catch (IOException | URISyntaxException e1) {
                log.error("打开url:{} 失败:{}", href, e1.getMessage());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // 选择空白回车符 改为选择整行
        if ("\n".equals(htmlEditor.getSelectedText())) {
            htmlEditor.setCaretPosition(htmlEditor.getSelectionStart());
            htmlEditor.callEditorKitAction(DefaultEditorKit.selectLineAction);
        } else {
            super.mousePressed(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (href != null) {
            htmlEditor.setCursor(defaultCursor);
            htmlEditor.setToolTipText("");
        }
        href = null;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Element h = getHyperlinkElement(e);
        if (h != null) {
            Object attribute = h.getAttributes().getAttribute(HTML.Tag.A);
            if (attribute instanceof AttributeSet) {
                AttributeSet set = (AttributeSet) attribute;
                href = (String) set.getAttribute(HTML.Attribute.HREF);
                if (href != null) {
                    htmlEditor.setCursor(handCursor);
                    htmlEditor.setToolTipText("双击打开");
                }
            }
        }
    }

    /**
     * 获取超链接元素
     *
     * @param event 超链接事件
     * @return a标签
     */
    private Element getHyperlinkElement(MouseEvent event) {
        JEditorPane editor = (JEditorPane) event.getSource();
        int pos = editor.getUI().viewToModel(editor, event.getPoint());
        if (pos >= 0 && editor.getDocument() instanceof HTMLDocument) {
            HTMLDocument hdoc = (HTMLDocument) editor.getDocument();
            Element elem = hdoc.getCharacterElement(pos);
            if (elem.getAttributes().getAttribute(HTML.Tag.A) != null) {
                return elem;
            }
        }
        return null;
    }
}
