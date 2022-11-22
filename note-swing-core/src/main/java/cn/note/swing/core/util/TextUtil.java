package cn.note.swing.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.view.theme.ThemeColor;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * 文本域工具类
 *
 * @author jee
 */
public class TextUtil {

    /**
     * 高亮文本 使用默认颜色
     *
     * @param textArea 文本域
     * @param startPos 开始位置
     * @param endPos   结束位置
     * @return 返回高亮对象
     */
    public static Object highLightText(@Nonnull JTextArea textArea, int startPos, int endPos) {

        return highLightText(textArea, ThemeColor.redColor, startPos, endPos);
    }

    /**
     * 高亮文本
     *
     * @param textArea 文本域
     * @param color    颜色
     * @param startPos 开始位置
     * @param endPos   结束位置
     * @return 返回高亮对象
     */
    public static Object highLightText(@Nonnull JTextArea textArea, Color color, int startPos, int endPos) {
        Highlighter highlighter = textArea.getHighlighter();
        DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(color);
        try {
            return highlighter.addHighlight(startPos, endPos, p);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 清除所有高亮
     *
     * @param textArea 文本域
     */
    public static void cleanHighLight(@Nonnull JTextArea textArea) {
        textArea.getHighlighter().removeAllHighlights();

    }


    /**
     * 自动换行
     * 允许光标显示,
     * 但是不可以编辑
     *
     * @param textComponent 文本域
     */
    public static void disableEditor(@Nonnull JTextComponent textComponent) {
        textComponent.setEditable(false);
        if (textComponent instanceof JTextArea) {
            JTextArea textArea = (JTextArea) textComponent;
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
        }
        Caret caret = textComponent.getCaret();
        caret.addChangeListener(e -> caret.setVisible(true));
    }


    /**
     * 计算 textarea 的第post位置的字符在窗口中的点位
     *
     * @param textArea 文本域
     * @param pos      位置
     */
    public static Point calcPosWindowPoint(JTextArea textArea, int pos) throws BadLocationException {
        // 获取相对点位
        Point loc = SwingUtilities.convertPoint(textArea, 0, 0, textArea.getRootPane());
        Rectangle posRec = textArea.modelToView(pos);
        Point posPoint = posRec.getLocation();
        double x = loc.getX() + posPoint.getX();
        double y = loc.getY() + posPoint.getY();
        Point screenPoint = new Point();
        screenPoint.setLocation(x, y);
        return screenPoint;
    }

    /**
     * 计算文本n行之前的偏移量
     * 12345
     * abcde
     * 第2行的偏移量=5  不包含当前字符所在行,前N行的字符总数
     *
     * @param text 文本内容
     * @return 不包含当前字符所在行, 前N行的字符总数
     */
    public static int calcLineOffset(String text, int line) {
        String[] lines = StrUtil.split(text, "\n");
        Assert.isTrue(lines.length > line, "文本总行数>查询函数");
        int offset = 1;
        for (int i = 0; i < line; i++) {
            offset += lines[i].length();
        }
        return offset;
    }

}
