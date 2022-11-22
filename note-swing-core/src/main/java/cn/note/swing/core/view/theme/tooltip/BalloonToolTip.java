package cn.note.swing.core.view.theme.tooltip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Optional;

/**
 * @author jee
 * @version 1.0
 */
public class BalloonToolTip extends JToolTip {
    private static final int TRI_HEIGHT = 4;
    private transient HierarchyListener listener;

    @Override
    public void updateUI() {
        removeHierarchyListener(listener);
        super.updateUI();
        listener = e -> {
            Component c = e.getComponent();
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && c.isShowing()) {
                Optional.ofNullable(SwingUtilities.getRoot(c))
                        .filter(JWindow.class::isInstance).map(JWindow.class::cast)
                        .ifPresent(w -> w.setBackground(new Color(0x0, true)));
            }
        };
        addHierarchyListener(listener);
        setOpaque(false);
        setForeground(Color.WHITE);
        setBackground(new Color(0xC8_00_00_00, true));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5 + TRI_HEIGHT, 5));
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.height = 32;
        return d;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Shape s = makeBalloonShape();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(s);
        g2.dispose();
        super.paintComponent(g);
    }

    private Shape makeBalloonShape() {
        float w = getWidth() - 1f;
        float h = getHeight() - TRI_HEIGHT - 1f;
        float r = 10f;
        float cx = getWidth() * .5f;
        Path2D triangle = new Path2D.Float();
        triangle.moveTo(cx - TRI_HEIGHT, h);
        triangle.lineTo(cx, h + TRI_HEIGHT);
        triangle.lineTo(cx + TRI_HEIGHT, h);
        Area area = new Area(new RoundRectangle2D.Float(0f, 0f, w, h, r, r));
        area.add(new Area(triangle));
        return area;
    }
}