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
 * @description: + pre 代码块
 * @author: jee
 */
@Slf4j
public class PreCodeStyleActionHandler implements RegisterKeyAction {


    private ExtendedEditor htmlEditor;

    private ExtendedHTMLDocument htmlDocument;

    public PreCodeStyleActionHandler(ExtendedEditor extendedEditor) {
        this.htmlEditor = extendedEditor;
        this.htmlDocument = (ExtendedHTMLDocument) htmlEditor.getDocument();
    }

    @Override
    public JComponent getComponent() {
        return htmlEditor;
    }


    @KeyAction
    public void insertPreAction() {
        // p==>pre
        registerKeyAction(EditorAction.INSERT_PRE, new ConsumerAction((e) -> htmlEditor.batchLineRun(this::createPreCode)));
    }

    private void createPreCode() {
        if (htmlEditor.isElement(HTML.Tag.P) && !htmlEditor.isParentElement(HTML.Tag.DIV)) {
            try {
                Element element = Utilities.getParagraphElement(htmlEditor, htmlEditor.getCaretPosition());
                if (htmlDocument.getLength() == 0) {
                    createPreHtml("");
                    htmlDocument.removeElement(element);
                    return;
                }
                int start = element.getStartOffset();
                int end = element.getEndOffset();
                int realLength = end - 1;
                int docLength = htmlDocument.getLength();
                String text = null;

                text = htmlDocument.getText(start, end - start);
                // 在中间一行
                if (start > 0 && realLength < docLength) {
                    log.debug("check other==>{},{}", start, end);
                    createPreAndCheckAll(element, text);
                } else if (start == 0 && realLength == docLength) {
                    log.debug("check only one line ==>{},{}", start, end);
                    // 第一行并且最后一行
                    createPreHtml(text);
                    htmlDocument.removeElement(element);

                } else if (realLength == docLength) {
                    log.debug("check last==>{},{}", start, end);
                    // 在最后一行,但不是第一行
                    text = htmlDocument.getText(start, end - start);
                    createPreAndCheckLast(element, text);
                } else {
                    log.debug("check first==>{},{}", start, end);
                    // 第一行,但不是最后一行
                    createPreAndCheckNext(element, text);
                }
            } catch (BadLocationException e) {
                log.error("{} operation error :{}", this.getClass().getName(), e.getMessage());
            }

        }
    }


    /**
     * 创建pre元素并检查上一个和下一个
     *
     * @param element 元素
     * @param text    内容
     */
    private void createPreAndCheckAll(Element element, String text) {
        text = text == null ? "" : text;
        log.debug("check all==>{}", text);
        boolean prevPre = htmlEditor.isPrevPreElement();
        boolean nextPre = htmlEditor.isNextPreElement();
        if (prevPre && nextPre) {
            int prevEndPos = element.getStartOffset() - 1;
            int nextStartPos = element.getEndOffset() + 1;
            Element nextElement = Utilities.getParagraphElement(htmlEditor, nextStartPos);
            Element nextPreElement = nextElement.getParentElement();

            int nextPreStart = nextPreElement.getStartOffset();
            int nextPreEnd = nextPreElement.getEndOffset();
            try {
                if (StrUtil.isNotBlank(text)) {
                    text = "\n" + text;
                }
                String nextPreText = htmlDocument.getText(nextPreStart, nextPreEnd - nextPreStart - 1);
                if (StrUtil.isNotBlank(nextPreText)) {
                    text = text + nextPreText;
                }
                htmlDocument.removeElement(element);
                htmlDocument.removeElement(nextPreElement);
                htmlDocument.insertString(prevEndPos, text, null);
                htmlEditor.setCaretPosition(prevEndPos);
            } catch (BadLocationException e) {
                log.error("合并prev&next pre失败:{}", e.getMessage());
            }

        } else if (prevPre) {
            createPreAndCheckPrev(element, text);
        } else if (nextPre) {
            text = text.replace("\n", "");
            createPreAndCheckNext(element, text);
        } else {
            htmlDocument.removeElement(element);
            createPreHtml(text);
        }
    }


    /**
     * 在最后一个元素只移除内容
     */
    private void createPreAndCheckLast(Element element, String text) {
        text = text == null ? "" : text;
        log.debug("check last==>{}", text);
        try {
            if (htmlEditor.isPrevPreElement()) {
                int prevEndPos = element.getStartOffset() - 1;
                htmlDocument.insertString(prevEndPos, "\n" + text, null);
            } else {
                createPreHtml(text);
            }
            htmlDocument.removeElement(element);
        } catch (BadLocationException e1) {
            log.error("合并last pre失败:{}", e1.getMessage());

        }
    }

    /**
     * 创建pre并合并上一个
     *
     * @param element 元素
     * @param text    内容
     */
    private void createPreAndCheckPrev(Element element, String text) {
        text = text == null ? "" : text;
        log.debug("check prev==>{}", text);
        if (htmlEditor.isPrevPreElement()) {
            try {
                int prevEndPos = element.getStartOffset() - 1;
                htmlDocument.insertString(prevEndPos, "\n" + text, null);
                htmlDocument.removeElement(element);
            } catch (BadLocationException e1) {
                log.error("合并prev pre失败:{}", e1.getMessage());

            }
        } else {
            createPreHtml(text);
            htmlDocument.removeElement(element);
        }
    }

    /**
     * 创建pre的同时校验下一个是否为pre
     *
     * @param element 元素
     * @param text    内容
     */
    private void createPreAndCheckNext(Element element, String text) {
        text = text == null ? "" : text;
        log.debug("check next==>{}", text);
        if (htmlEditor.isNextPreElement()) {
            try {
                int start = element.getStartOffset();
                int nextStartPos = element.getEndOffset();
                if (start != 0) {
                    text += "\n";
                }
                htmlDocument.insertString(nextStartPos, text, null);
                htmlDocument.removeElement(element);

            } catch (BadLocationException e1) {
                log.error("合并next pre失败:{}", e1.getMessage());
            }
        } else {
            htmlDocument.removeElement(element);
            createPreHtml(text);
        }
    }

    /**
     * @param text 创建pre元素
     */
    private void createPreHtml(String text) {
        text = text == null ? "" : text;
        String preTemplate = StrUtil.format("<pre class=\"{}\">{}</pre>", "code-dark", text);
        Action action = new HTMLEditorKit.InsertHTMLTextAction("InsertPre", preTemplate,
                HTML.Tag.BODY, HTML.Tag.PRE);
        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }


}
