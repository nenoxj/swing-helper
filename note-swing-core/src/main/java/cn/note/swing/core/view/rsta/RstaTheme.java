package cn.note.swing.core.view.rsta;

import cn.hutool.log.StaticLog;
import org.fife.ui.rsyntaxtextarea.Theme;

import java.io.IOException;

/**
 * rsta 编辑器主题
 */
public enum RstaTheme {
    /**
     * 默认主题
     */
    defaults("/org/fife/ui/rsyntaxtextarea/themes/default.xml"),
    /**
     * 黑色主题
     */
    dark("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"),
    /**
     * monokai 主题
     */
    monokai("/org/fife/ui/rsyntaxtextarea/themes/monokai.xml"),

    /**
     * eclipse 默认主题
     */
    eclipse("/org/fife/ui/rsyntaxtextarea/themes/eclipse.xml"),

    /**
     * idea 默认主题
     */
    idea("/org/fife/ui/rsyntaxtextarea/themes/idea.xml");

    private String themeSource;

    RstaTheme(String themeSource) {
        this.themeSource = themeSource;
    }

    public Theme getTheme() {
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(this.themeSource));
            return theme;
        } catch (IOException e) {
            StaticLog.error("加载主题失败:" + e.getMessage());
        }
        return null;
    }
}
