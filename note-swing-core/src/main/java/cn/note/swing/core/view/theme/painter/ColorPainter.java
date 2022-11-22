package cn.note.swing.core.view.theme.painter;

import org.jdesktop.swingx.painter.AbstractPainter;

import java.awt.*;

/**
 * 简单颜色绘制
 */
public class ColorPainter extends AbstractPainter {

    private Color color;

    public ColorPainter(Color color) {
        this.color = color;
    }

    @Override
    protected void doPaint(Graphics2D g, Object object, int width, int height) {
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }
}
