package cn.note.swing.view.note.editor.core.richtext.handler.style;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedHTMLEditorKit;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.StyleSheet;
import java.awt.*;


/**
 * @description: + 全局样式设置
 * + 加载默认css文件
 * + 测试SimpleAttributeSet
 * * @author: jee
 */
public class GlobalStyleActionHandler {


    private static final String DEFAULT_CSS_FILE = "richtext/default.css";

    private ExtendedHTMLEditorKit htmlEditorKit;

    private ExtendedEditor htmlEditor;

    private static String defaultCss;

    public GlobalStyleActionHandler(ExtendedEditor extendedEditor) {
        this.htmlEditor = extendedEditor;
        this.htmlEditorKit = (ExtendedHTMLEditorKit) extendedEditor.getEditorKit();
        installGlobalStyle();
    }


    /**
     * 安装全局style样式
     */
    private void installGlobalStyle() {
        if (defaultCss == null) {
            defaultCss = ResourceUtil.readUtf8Str(DEFAULT_CSS_FILE);
        }
        StyleSheet styleSheet = htmlEditorKit.getStyleSheet();
        styleSheet.addRule(defaultCss);
    }

    private SimpleAttributeSet preSas;

    private SimpleAttributeSet createPreStyle() {
        if (preSas == null) {
            preSas = new SimpleAttributeSet();
            StyleConstants.setBackground(preSas, Color.decode("#23241f"));
            StyleConstants.setForeground(preSas, Color.decode("#f8f8f2"));
            StyleConstants.setLeftIndent(preSas, 5f);
            StyleConstants.setRightIndent(preSas, 5f);
            StyleConstants.setLineSpacing(preSas, 1.1f);
        }
        return preSas;

    }



}
