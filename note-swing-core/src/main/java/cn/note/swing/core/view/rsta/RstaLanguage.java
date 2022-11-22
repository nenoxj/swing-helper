package cn.note.swing.core.view.rsta;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

public enum RstaLanguage {

    text(SyntaxConstants.SYNTAX_STYLE_NONE),

    java(SyntaxConstants.SYNTAX_STYLE_JAVA),
    properties(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE),
    yaml(SyntaxConstants.SYNTAX_STYLE_YAML),

    html(SyntaxConstants.SYNTAX_STYLE_HTML),
    css(SyntaxConstants.SYNTAX_STYLE_CSS),
    javascript(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT),
    typescript(SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT),

    json(SyntaxConstants.SYNTAX_STYLE_JSON),
    sql(SyntaxConstants.SYNTAX_STYLE_SQL),

    groovy(SyntaxConstants.SYNTAX_STYLE_GROOVY);

    private String lang;

    RstaLanguage(String lang) {
        this.lang = lang;
    }

    public void apply(RSyntaxTextArea rsta) {
        rsta.setSyntaxEditingStyle(this.lang);
    }
}
