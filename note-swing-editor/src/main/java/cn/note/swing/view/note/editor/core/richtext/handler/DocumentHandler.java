package cn.note.swing.view.note.editor.core.richtext.handler;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedHTMLDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * @description: doc拦截器
 * <p>
 * --- 无法拦截内容为空时替换文本操作
 * @author: jee
 */
public class DocumentHandler extends DocumentFilter {
    /**
     * 编辑器
     */
    private ExtendedEditor htmlEditor;
    /**
     * doc
     */
    private ExtendedHTMLDocument htmlDocument;

    public DocumentHandler(ExtendedEditor extendedEditor) {
        htmlEditor = extendedEditor;
        htmlDocument = (ExtendedHTMLDocument) htmlEditor.getDocument();
        bindDocumentListener();
    }

    /**
     * 绑定文本变化监听
     */
    private void bindDocumentListener() {
        AbstractDocument abstractDocument = (AbstractDocument) htmlEditor.getDocument();
        abstractDocument.setDocumentFilter(this);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        super.replace(fb, offset, length, text, attrs);
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {
        super.insertString(fb, offset, string, attr);
    }

    /**
     * if (StrUtil.equals(type, "remove") && htmlDocument.getLength() == 0) {
     * String formatBody = formatDocumentBody();
     * SwingUtilities.invokeLater(() -> {
     * htmlSource.setText(formatBody);
     * htmlEditor.setText(formatBody);
     * });
     * } else {
     * <p>
     * }
     * 格式化文本body
     *
     * @return 格式化文本body
     */
    public String formatDocumentBody() {
        String html = htmlEditor.getText();
        if (StrUtil.isBlank(htmlEditor.getText())) {
            return "";
        }
        Document document = Jsoup.parse(html);
        Element body = document.selectFirst("body");
        String bodyText = body.text();
        body.children().remove();
        body.append("<p style=\"margin-top: 0\"></p>");
        document.selectFirst("head").children().remove();
        return document.html();
    }
}