package cn.note.swing.core.view.theme.tooltip;

import cn.note.swing.core.view.theme.ThemeColor;
import net.java.balloontip.styles.BalloonTipStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/**
 * 失败的改造....
 * 先放一放
 */
public class ArrowTipBalloonStyle extends BalloonTipStyle {
    private final Color borderColor;
    private final Color fillColor;
    private final JComponent parent;

    private final int arrowSize = 4;

    /**
     * Constructor
     *
     * @param borderColor border line color
     * @param fillColor   fill color
     */
    public ArrowTipBalloonStyle(JComponent parent, Color fillColor, Color borderColor) {
        super();
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.parent = parent;
    }

    public Insets getBorderInsets(Component c) {
        if (flipY) {
            return new Insets(verticalOffset + 1, 1, 1, 1);
        }
        return new Insets(1, 1, verticalOffset + 1, 1);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        width -= 1;
        height -= 1;

        int yTop;        // Y-coordinate of the top side of the balloon
        int yBottom;    // Y-coordinate of the bottom side of the balloon
        if (flipY) {
            yTop = y + verticalOffset;
            yBottom = y + height;
        } else {
            yTop = y;
            yBottom = y + height - verticalOffset;
        }
//        // Draw the outline of the balloon
        g2d.setPaint(ThemeColor.fontColor);
        g2d.fillRect(x, yTop, width, yBottom);
////        g2d.setPaint(borderColor);
//        g2d.drawRect(x, yTop, width, yBottom);

        Shape shape = makeBalloonShape(width, height, yTop, yBottom);
        g2d.setPaint(ThemeColor.themeColor);
        g2d.draw(shape);
        g2d.dispose();
    }


    private Shape makeBalloonShape(int width, int height, int yTop, int yBottom) {
        float w = width - 1f;
        float h = height - 1f;
        // 中间位置
        float m = w / 2;
        // 组件中间位置
        int x = parent.getWidth() / 2;
        Path2D triangle = new Path2D.Float();
        triangle.moveTo(m, 0f);
        triangle.lineTo(m - arrowSize, arrowSize);
        triangle.lineTo(m + arrowSize, arrowSize);
        Area area = new Area(new RoundRectangle2D.Float(0f, arrowSize, w, yBottom - arrowSize, arrowSize, arrowSize));
        area.add(new Area(triangle));
        return area;
    }
}
