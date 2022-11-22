package cn.note.swing.view.note.editor.core.richtext.handler.keyboard;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * 回车两次拦截器
 */
@Slf4j
public class EnterTwiceHandler implements KeyListener {

    /**
     * 编辑器
     */
    private ExtendedEditor htmlEditor;

    private StyledDocument htmlDoc;

    private List<String> ignoreTags;

    /**
     * 上一次标签数量
     */
    private int prevEnterCount;

    public EnterTwiceHandler(ExtendedEditor textPane) {
        htmlEditor = textPane;
        htmlEditor.getCaret().setBlinkRate(400);
        htmlDoc = htmlEditor.getStyledDocument();
        ignoreTags = Lists.newArrayList(HTML.Tag.BODY.toString(), HTML.Tag.P.toString(), HTML.Tag.HTML.toString());
        htmlEditor.addKeyListener(this);
    }


    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ENTER) {
            int pos = htmlEditor.getCaretPosition();

            Element element = htmlDoc.getParagraphElement(pos);

            if (element != null) {
                Element parentElement = element.getParentElement();
                String tagName = parentElement.getName();
                // 不是ignoreTags 插入标签
                if (!ignoreTags.contains(tagName.toLowerCase())) {
                    //行元素开始
                    int start = element.getStartOffset();
                    // 父元素末尾
                    int end = parentElement.getEndOffset();
                    try {
                        String elementText = htmlDoc.getText(start, end - start);
                        if (prevEnterCount > 0 && StrUtil.isBlank(elementText)) {
                            prevEnterCount = 0;
                            if (htmlEditor.isLastCaret()) {
                                if (htmlEditor.isLastLine()) {
                                    // 插入新行
                                    htmlEditor.callEditorKitAction(EditorAction.INSERT_P);
                                    htmlEditor.refreshText();
                                }

                            } else {
                                // 跳出该元素
                                htmlEditor.setCaretPosition(htmlEditor.getCaretPosition() + 1);
                                removeDoubleEnterLine();
                            }
                        } else {
                            prevEnterCount++;
                        }
                    } catch (BadLocationException e) {
                        log.error("{} exec error: {}", this.getClass().getSimpleName(), e);
                    }
                }
            }
        } else {
            prevEnterCount = 0;
        }
    }

    private int getElementCount() {
        Element root = htmlEditor.getDocument().getDefaultRootElement();
        return root.getElementCount();
    }

    /**
     * 删除双回车行
     */
    private void removeDoubleEnterLine() {
        log.debug("end press enter twice,break tag!");
        htmlEditor.callEditorKitAction(DefaultEditorKit.deletePrevCharAction);
        htmlEditor.callEditorKitAction(DefaultEditorKit.deletePrevCharAction);
    }
}