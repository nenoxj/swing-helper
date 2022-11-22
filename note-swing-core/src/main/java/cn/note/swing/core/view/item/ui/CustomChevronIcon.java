package cn.note.swing.core.view.item.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author jee
 * @version 1.0
 */
public class CustomChevronIcon implements Icon {
    boolean up = true;
    private static int xAlias = UIManager.getFont("Label.font").getSize() / 3;
    private static int yAlias = 2 * xAlias;


    public CustomChevronIcon(boolean up) {
        this.up = up;
    }


    @Override
    public int getIconHeight() {
        return xAlias;
    }

    @Override
    public int getIconWidth() {
        return yAlias;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        // 指定粗细2.0
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2.0f));

        if (up) {

            g.drawLine(x + xAlias, y, x, y + xAlias);
            g.drawLine(x + xAlias, y, x + yAlias, y + xAlias);
        } else {
            g.drawLine(x, y, x + xAlias, y + xAlias);
            g.drawLine(x + xAlias, y + xAlias, x + yAlias, y);
        }
    }
}
