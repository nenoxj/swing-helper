package cn.note.swing.view.note.editor.core.richtext.handler.style;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyAction;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedHTMLDocument;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Utilities;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.event.ActionEvent;


/**
 * @description: P 段落代码块
 * 插入新段落
 * 取消pre
 * @author: jee
 */
@Slf4j
public class PStyleActionHandler implements RegisterKeyAction {

    private ExtendedEditor htmlEditor;

    private ExtendedHTMLDocument htmlDocument;

    public PStyleActionHandler(ExtendedEditor extendedEditor) {
        this.htmlEditor = extendedEditor;
        this.htmlDocument = (ExtendedHTMLDocument) htmlEditor.getDocument();
    }

    /**
     * @param text 创建p元素
     */
    private void createPElement(String text) {
        if (text == null) {
            text = "";
        }
        String pTemplate = StrUtil.format("<p style=\"margin-top: 0\">{}</p>", text);
        Action action = new HTMLEditorKit.InsertHTMLTextAction(EditorAction.INSERT_P.getName(), pTemplate,
                HTML.Tag.BODY, HTML.Tag.P);
        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }




    @Override
    public JComponent getComponent() {
        return htmlEditor;
    }


    /**
     * @return 创建空的段落元素
     */
    @KeyAction
    public void insertPAction() {
        registerKeyAction(EditorAction.INSERT_P, new ConsumerAction((e) -> {
            createPElement("");
            htmlEditor.setCaretPosition(htmlEditor.getDocument().getLength());
        }));
    }


    @KeyAction
    public void cancelPreAction() {
        registerKeyAction(EditorAction.CANCEL_PRE, new ConsumerAction(e -> htmlEditor.batchLineRun(this::deletePreElement)));
    }


    private void deletePreElement() {
        if (htmlEditor.isParentElement(HTML.Tag.PRE)) {
            Element element = Utilities.getParagraphElement(htmlEditor, htmlEditor.getCaretPosition());
            int start = element.getStartOffset();
            int end = element.getEndOffset();
            String text = null;
            try {
                log.debug("remove pre:{},{}", start, end);
                text = htmlDocument.getText(start, end - start);
                // 仅当只有一行 并且为pre标签时
                if (start == 0 && end == 1) {
                    createPElement(text);
                    htmlDocument.removeElement(element);
                } else {
                    htmlDocument.removeElement(element);
                    createPElement(text);
                }
                if (start == 0) {
                    // 如果第一个元素为空行
                    Element first = Utilities.getParagraphElement(htmlEditor, 0);
                    if (first.getEndOffset() - first.getStartOffset() == 1) {
                        htmlDocument.removeElement(first);
                    }
                }

            } catch (BadLocationException e) {
                log.error("bad create p:{}", e.getMessage());
            }
        }
    }
}
