package cn.note.swing.view.note.editor.core.richtext.handler.caret;

import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.util.Enumeration;

/**
 * @description: 光标拦截器
 * 只关注颜色变化
 * @author: jee
 */
@Slf4j
public class CaretColorHandler implements CaretListener {

    /**
     * 编辑器
     */
    private ExtendedEditor htmlEditor;

    public CaretColorHandler(ExtendedEditor textPane) {
        htmlEditor = textPane;
        htmlEditor.addCaretListener(this);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int caretPos = e.getDot();
        Element element = htmlEditor.getStyledDocument().getParagraphElement(caretPos);
        if (element == null) {
            return;
        }
        checkCaretColor(element);
    }

    /**
     * 更新元素的光标颜色
     *
     * @param element html元素
     */
    private void checkCaretColor(Element element) {
        SwingUtilities.invokeLater(() -> {
            htmlEditor.setCaretColor(Color.BLACK);
            AttributeSet attributes = element.getAttributes();
            String className = (String) attributes.getAttribute(HTML.Attribute.CLASS);
            if (attributes.isDefined(StyleConstants.Background)) {
                final Color background = StyleConstants.getBackground(attributes);
                if (isDeepColor(background)) {
                    this.updateCaretColor(Color.white);
                } else {
                    this.updateCaretColor(Color.black);
                }
                // 如果为深色主题
            } else if (className != null && className.contains("dark")) {
                this.updateCaretColor(Color.white);
            }
        });

    }

    /**
     * 判断颜色是否为深色
     *
     * @param color 颜色
     * @return 是否深色
     */
    private boolean isDeepColor(Color color) {
        double grayLevel = color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114;
        if (grayLevel >= 192) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param color 更新光标颜色
     */
    private void updateCaretColor(Color color) {
        htmlEditor.setCaretColor(color);
        // 光标显示
        htmlEditor.getCaret().setSelectionVisible(true);
    }

    /**
     * 查找元素的style样式
     *
     * @param element 元素
     * @return 元素样式
     */
    private String findStyle(Element element) {
        AttributeSet as = element.getAttributes();
        if (as == null) {
            return null;
        }
        Object val = as.getAttribute(HTML.Attribute.CLASS);
        if (val != null && (val instanceof String)) {
            return (String) val;
        }
        for (Enumeration e = as.getAttributeNames(); e.hasMoreElements(); ) {
            Object key = e.nextElement();
            if (key instanceof HTML.Tag) {
                AttributeSet eas = (AttributeSet) (as.getAttribute(key));
                if (eas != null) {
                    val = eas.getAttribute(HTML.Attribute.CLASS);
                    if (val != null) {
                        return (String) val;
                    }
                }
            }

        }
        return null;
    }
}