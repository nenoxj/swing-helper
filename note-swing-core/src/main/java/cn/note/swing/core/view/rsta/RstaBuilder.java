package cn.note.swing.core.view.rsta;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * RSyntaxTextArea 各种风格构建
 * RTextScrollPane
 */
public class RstaBuilder {

    /**
     * 默认与label 大小保持一致
     */
    private static Font uiFont = new Font("Monospaced", 0, UIManager.getFont("Label.font").getSize());

    /**
     * @return 创建java风格的rsta编辑器
     */
    public static RTextScrollPane createRsPane() {
        return createRsPane(null);
    }

    /**
     * 在win11 代码字体大小为固定值, 自适应处理
     *
     * @param lang SyntaxConstants 支持的代码风格
     * @return 兼容win11的rspane
     * @see SyntaxConstants
     */
    public static RTextScrollPane createRsPane(RstaLanguage lang) {
        RSyntaxTextArea rsta = new RSyntaxTextArea();
        if (lang != null) {
//            rsta.setSyntaxEditingStyle(codeStyle);
            lang.apply(rsta);
        }
        rsta.setHighlightCurrentLine(false);
        //取消默认的右键菜单
//        rsta.setPopupMenu(null);
        RTextScrollPane rsPane = new RTextScrollPane(rsta);
        updateRstaFont(rsPane);
        return rsPane;
    }

    // 设置RSyntaxTextArea 字体大小
    private static void setFont(RSyntaxTextArea textArea, Font font) {
        if (font != null) {
            SyntaxScheme ss = textArea.getSyntaxScheme();
            ss = (SyntaxScheme) ss.clone();
            for (int i = 0; i < ss.getStyleCount(); i++) {
                if (ss.getStyle(i) != null) {
                    ss.getStyle(i).font = font;
                }
            }
            textArea.setSyntaxScheme(ss);
            textArea.setFont(font);
        }
    }

    /**
     * 更新默认字体
     *
     * @param rtsp rsta的容器
     */
    public static void updateRstaFont(RTextScrollPane rtsp) {
        updateRstaFont(rtsp, uiFont);
    }

    /**
     * 更新字体
     *
     * @param rtsp rsta的容器
     */
    public static void updateRstaFont(RTextScrollPane rtsp, Font font) {
        RSyntaxTextArea rsta = (RSyntaxTextArea) rtsp.getTextArea();
        setFont(rsta, font);
        rtsp.getGutter().setLineNumberFont(font);
        rtsp.revalidate();
    }
}
