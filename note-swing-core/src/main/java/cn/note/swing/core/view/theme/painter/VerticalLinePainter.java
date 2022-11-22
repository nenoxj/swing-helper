package cn.note.swing.core.view.theme.painter;

import org.jdesktop.swingx.painter.AbstractPainter;

import java.awt.*;


/**
 * 背景加垂直线
 */
public class VerticalLinePainter extends AbstractPainter {

    /**
     * 背景色
     */
    private Color bgColor;
    /**
     * 线颜色
     */
    private Color lineColor;
    /**
     * 线粗细
     */
    private float lineSize;

    public VerticalLinePainter(Color bgColor, Color lineColor, float lineSize) {
        this.bgColor = bgColor;
        this.lineColor = lineColor;
        this.lineSize = lineSize;
    }

    @Override
    protected void doPaint(Graphics2D g, Object object, int width, int height) {
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);
        g.setStroke(new BasicStroke(lineSize));
        g.setColor(lineColor);
        g.drawLine(width - 1, 0, width - 1, height);
        g.dispose();
    }
}