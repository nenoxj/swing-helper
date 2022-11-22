package cn.note.swing.core.view.theme.painter;

import cn.note.swing.core.view.theme.ColorUI;
import org.jdesktop.swingx.painter.Painter;

/**
 * 定义丰富的painter
 */
public interface PainterUI extends ColorUI {
    /**
     * 背景画笔
     */
    default Painter backgroundPainter() {
        return new ColorPainter(backgroundColor());
    }


    /**
     * 前景画笔
     */
    default Painter foregroundPainter() {
        return new ColorPainter(foregroundColor());
    }


    /**
     * 绘制带有垂线的背景
     *
     * @param lineSize 线的大小
     */
    default Painter verticalLinePainter(float lineSize) {
        return new VerticalLinePainter(backgroundColor(), foregroundColor(), lineSize);
    }

}
