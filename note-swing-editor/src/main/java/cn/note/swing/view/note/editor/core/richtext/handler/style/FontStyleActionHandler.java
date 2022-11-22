package cn.note.swing.view.note.editor.core.richtext.handler.style;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyAction;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * @description: + 字体样式设置
 * + 加粗
 * + 斜体
 * + H1-H6
 * @author: jee
 */
public class FontStyleActionHandler implements RegisterKeyAction {

    private ExtendedEditor htmlEditor;

    public FontStyleActionHandler(ExtendedEditor extendedEditor) {
        this.htmlEditor = extendedEditor;
    }


    @KeyAction("设置字体大小 H1-H6")
    public void fontAction() {
        registerKeyAction(EditorAction.H1, new ConsumerAction((e) -> triggerFontAction(EditorAction.H1)));
        registerKeyAction(EditorAction.H2, new ConsumerAction((e) -> triggerFontAction(EditorAction.H2)));
        registerKeyAction(EditorAction.H3, new ConsumerAction((e) -> triggerFontAction(EditorAction.H3)));
        registerKeyAction(EditorAction.H4, new ConsumerAction((e) -> triggerFontAction(EditorAction.H4)));
        registerKeyAction(EditorAction.H5, new ConsumerAction((e) -> triggerFontAction(EditorAction.H5)));
        registerKeyAction(EditorAction.H6, new ConsumerAction((e) -> triggerFontAction(EditorAction.H6)));

    }

    @KeyAction("设置字体风格,粗体 斜体")
    public void styleAction() {
        registerKeyAction(EditorAction.FONT_BOLD, new ConsumerAction((e) -> triggerDefaultAction("font-bold")));
        registerKeyAction(EditorAction.FONT_ITALIC, new ConsumerAction((e) -> triggerDefaultAction("font-italic")));
    }


    @KeyAction("设置字体颜色")
    public void colorAction() {
        registerKeyAction(EditorAction.COLOR_DANGER, new ConsumerAction((e) -> triggerColorAction(ThemeColor.redColor)));
        registerKeyAction(EditorAction.COLOR_DARK, new ConsumerAction((e) -> triggerColorAction(Color.BLACK)));
    }


    @Override
    public JComponent getComponent() {
        return htmlEditor;
    }


    private void triggerColorAction(Color color) {
        if (htmlEditor.isElement(HTML.Tag.P)) {
            int p0 = htmlEditor.getSelectionStart();
            int p1 = htmlEditor.getSelectionEnd();
            if (p0 != p1) {
                StyledDocument doc = htmlEditor.getStyledDocument();
                MutableAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, color);
                doc.setCharacterAttributes(p0, p1 - p0, attr, false);
            }

        }
    }

    /**
     * 触发内置事件
     *
     * @param name 事件名称
     */
    private void triggerDefaultAction(String name) {
        if (htmlEditor.isElement(HTML.Tag.P)) {
            htmlEditor.callEditorKitAction(name);
        }
    }

    /**
     * @param extendedAction 修改字体action
     */
    private void triggerFontAction(EditorAction extendedAction) {
        if (htmlEditor.isElement(HTML.Tag.P)) {
            String fontName = extendedAction.getName();
            int fontSize = Integer.valueOf(StrUtil.subAfter(fontName, "-", true));
            Action action = new MyFontSizeAction(fontName, fontSize);
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    /**
     * FontSizeAction 无法设置Alt 1-6
     */
    class MyFontSizeAction extends StyledEditorKit.StyledTextAction {

        MyFontSizeAction(String nm, int size) {
            super(nm);
            this.size = size;
        }

        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                int size = this.size;
                if (size != 0) {
                    MutableAttributeSet attr = new SimpleAttributeSet();
                    StyleConstants.setFontSize(attr, size);
                    setCharacterAttributes(editor, attr, false);
                } else {
                    UIManager.getLookAndFeel().provideErrorFeedback(editor);
                }
            }
        }

        private int size;
    }

}
