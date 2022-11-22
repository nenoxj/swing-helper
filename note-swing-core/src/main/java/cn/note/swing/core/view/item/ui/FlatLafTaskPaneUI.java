package cn.note.swing.core.view.item.ui;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

/**
 * 构建taskPane的ui样式
 *
 * @author jee
 */
public class FlatLafTaskPaneUI extends BasicTaskPaneUI {

    public FlatLafTaskPaneUI() {

        // 自定义title 高度
        titleHeight = UIManager.getFont("Label.font").getSize() * 2 + 10;
        if (titleHeight < 25) {
            titleHeight = 25;
        }

    }

    public static ComponentUI createUI(JComponent c) {
        return new FlatLafTaskPaneUI();
    }

    @Override
    protected Border createPaneBorder() {
        return new FlatLafPaneBorder();
    }

    /**
     * Overriden to paint the background of the component but keeping the
     * rounded corners.
     */
    @Override
    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getParent().getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
            g.setColor(c.getBackground());
            g.fillRect(0, getRoundHeight(), c.getWidth(), c.getHeight() - getRoundHeight());
        }
        paint(g, c);
    }

    @Override
    public Component createAction(Action action) {
        return super.createAction(action);
    }

    /**
     * The border of the task pane group paints the "text", the "icon", the
     * "expanded" status and the "special" type.
     */
    class FlatLafPaneBorder extends PaneBorder {

        @Override
        protected void paintTitleBackground(JXTaskPane group, Graphics g) {

            Paint oldPaint = ((Graphics2D) g).getPaint();

            roundHeight = 10;

            if (group.isSpecial()) {
                g.setColor(specialTitleBackground);

                g.fillRoundRect(0, 0, group.getWidth(), getRoundHeight() * 2, getRoundHeight(), getRoundHeight());
                g.fillRect(0, getRoundHeight(), group.getWidth(), getTitleHeight(group) - getRoundHeight());

            } else {
                Color[] colors = {titleBackgroundGradientStart, titleBackgroundGradientEnd};

                float[] fractions = {0.0f, 1.0f};

                LinearGradientPaint gradient = new LinearGradientPaint(group.getWidth() / 2, 0.0f, group.getWidth() / 2,
                        getTitleHeight(group), fractions, colors);

                ((Graphics2D) g).setPaint(gradient);

                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                        RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g.fillRoundRect(0, 0, group.getWidth(), getTitleHeight(group) / 2, getRoundHeight(), getRoundHeight());

                g.fillRect(0, getRoundHeight(), group.getWidth(), getTitleHeight(group) - getRoundHeight());

            }

            // draw the border around the title area
            g.setColor(borderColor);

            // @==@去除半角
            g.drawRoundRect(0, 0, group.getWidth() - 1, getTitleHeight(group) + getRoundHeight(), getRoundHeight(),
                    getRoundHeight());
            g.drawLine(0, getTitleHeight(group) - 1, group.getWidth(), getTitleHeight(group) - 1);

            ((Graphics2D) g).setPaint(oldPaint);
        }

        @Override
        protected void paintExpandedControls(JXTaskPane group, Graphics g, int x, int y, int width, int height) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(getPaintColor(group));
            this.paintChevronControls(group, g, x, y, width, height);

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        @Override
        protected void paintTitle(JXTaskPane group, Graphics g, Color textColor, int x, int y, int width, int height) {
            configureLabel(group);
            // FlatLaf has some issues with ColorUIResource
            label.setForeground(new Color(textColor.getRGB()));
            if (group.getFont() != null && !(group.getFont() instanceof FontUIResource)) {
                label.setFont(group.getFont());
            }
            g.translate(x, y);
            label.setBounds(0, 0, width, height);
            label.paint(g);
            g.translate(-x, -y);
        }

        @Override
        protected boolean isMouseOverBorder() {
            return true;
        }


        /*
         * @==@ 右侧图标 (non-Javadoc)
         *
         * @see org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI.PaneBorder#
         * paintChevronControls(org.jdesktop.swingx.JXTaskPane,
         * java.awt.Graphics, int, int, int, int)
         */
        @Override
        protected void paintChevronControls(JXTaskPane group, Graphics g, int x, int y, int width, int height) {
            CustomChevronIcon chevron;
            if (group.isCollapsed()) {
//                chevron = new ChevronIcon(false);
                chevron = new CustomChevronIcon(false);
            } else {
//                chevron = new ChevronIcon(true);
                chevron = new CustomChevronIcon(true);
            }
            int chevronX = x + width / 2 - chevron.getIconWidth() / 2;
            int chevronY = y + (height / 2 - chevron.getIconHeight() / 2);
            // 绘制为一个箭头
            chevron.paintIcon(group, g, chevronX, chevronY);
        }

    }

    /**
     * @==@ 去除圆角border (non-Javadoc)
     */
    @Override
    protected Border createContentPaneBorder() {
        Color borderColor = UIManager.getColor("TaskPane.borderColor");

        Border contentBorder = new ContentPaneBorder(borderColor) {
            @Override
            public Insets getBorderInsets(Component c) {

                return new Insets(5, 0, 5, 0);
            }
        };
        return new CompoundBorder(contentBorder, BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    /**
     * @==@ 去除圆角 (non-Javadoc)
     */
    @Override
    protected int getRoundHeight() {
        return 0;
    }

}
