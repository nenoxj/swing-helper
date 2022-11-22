package cn.note.swing.view.note.editor.core.richtext.handler.source;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 监听文本编辑器是否发生变化
 * 用htmlSource 存储当前文本编辑器的内容 可以作为debug打印输出
 * bindDocumentListener 自动绑定ExtendedEditor 的document 变化监听事件
 *
 * @author: jee
 */
@Slf4j
public class HtmlSourceHandler implements DocumentListener {
    /**
     * html source
     */
    @Getter
    private RSyntaxTextArea htmlSource;

    /**
     * 编辑器
     */
    private ExtendedEditor htmlEditor;


    /* 是否发生改变*/
    @Getter
    @Setter
    private boolean changed;

    public HtmlSourceHandler(ExtendedEditor extendedEditor) {
        htmlEditor = extendedEditor;
        htmlSource = new RSyntaxTextArea();
        htmlSource.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        htmlSource.setEditable(false);
        bindDocumentListener();
    }

    /**
     * 绑定文本变化监听
     */
    private void bindDocumentListener() {
        htmlEditor.getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        syncSource("insert");
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        syncSource("remove");

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        syncSource("style change");
    }

    /**
     * 记录当前html资源 及判断编辑器是否发生变化
     *
     * @param type 作为debug输出使用
     */
    private void syncSource(String type) {
        String htmlText = htmlEditor.getText();
        this.changed = true;
        htmlSource.setText(htmlText);
    }

}