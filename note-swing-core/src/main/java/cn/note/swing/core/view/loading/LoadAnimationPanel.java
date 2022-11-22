package cn.note.swing.core.view.loading;

import cn.note.swing.core.util.SwingCoreUtil;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * 动画面板
 *
 * @author jee
 * @version 1.0
 */
class LoadAnimationPanel extends JXPanel {
    private String message;
    private int triState = 0;
    private boolean animating = false;
    private Animator animator;


    public LoadAnimationPanel(String message) {
        super.setBackground(SwingCoreUtil.deriveColorHSB(
                UIManager.getColor("Panel.background"), 0, 0, -.06f));
        this.message = message;

        PropertySetter rotated = new PropertySetter(this, "triState", 0, 3);
        animator = new Animator(300, Animator.INFINITE,
                Animator.RepeatBehavior.LOOP, rotated);
        // Don't animate gears if loading is quick
        animator.setStartDelay(200);

    }

    public LoadAnimationPanel() {
        this("Loading");
    }

    public void setAnimating(boolean animating) {
        this.animating = animating;
        if (animating) {
            animator.start();
        } else {
            animator.stop();
        }
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setTriState(int triState) {
        this.triState = triState;
        repaint();
    }

    private int getTriState() {
        return triState;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        Dimension size = getSize();

        Color textColor = SwingCoreUtil.deriveColorHSB(getBackground(), 0, 0, -.3f);
        Color dotColor = SwingCoreUtil.deriveColorHSB(textColor, 0, .2f, -.08f);
        g2.setColor(textColor);
        g2.setFont(UIManager.getFont("Label.font").deriveFont(24f));
        FontMetrics metrics = g2.getFontMetrics();
        Rectangle2D rect = metrics.getStringBounds(message, g2);
        Rectangle2D dotRect = metrics.getStringBounds(".", g2);
        float x = (float) (size.width - (rect.getWidth() + 3 * dotRect.getWidth())) / 2;
        float y = (float) (size.height - rect.getHeight()) / 2;
        g2.drawString(message, x, y);
        int tri = getTriState();
        float dx = 0;
        for (int i = 0; i < 3; i++) {
            g2.setColor(animator.isRunning() && i == tri ? dotColor : textColor);
            g2.drawString(".", x + (float) (rect.getWidth() + dx), y);
            dx += dotRect.getWidth();
        }
    }
}