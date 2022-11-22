package cn.note.swing.core.view.loading;

import cn.note.swing.core.view.theme.ThemeColor;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;

/**
 * 简单的加载 可以用于glasspane
 *
 * <pre>
 *
 *     frame.setGlassPane(new SimpleLoading("正在加载..."));
 *     frame.getClassPane().setVisible(true)
 *
 * </pre>
 *
 * <pre>
 *
 *
 *       JXPanel panel = new JXPanel(new MigLayout());
 *       JLabel closeTip = new JLabel("正在保存", PngIconFactory.ICON_LOADING, SwingConstants.LEFT);
 *       panel.add(closeTip, "pos 0.5al 0.5al");
 * </pre>
 *
 * @author jee
 * @version 1.0
 */
public class SimpleLoading extends JXPanel {

    private String text;

    private JXBusyLabel busyLabel;

    private int size;

    public SimpleLoading(String text) {
        this(text, 20);
    }

    public SimpleLoading(String text, int size) {
        this.text = text;
        this.size = size;
        this.create();
    }

    protected void create() {


        busyLabel = new JXBusyLabel(new Dimension(size, size));
        busyLabel.setBusy(true);

        // nocolor 区域
        JPanel centerArea = new JPanel();
        JXLabel textLabel = new JXLabel(text);
        centerArea.add(busyLabel);
        centerArea.add(textLabel);
        centerArea.setBackground(ThemeColor.noColor);


        this.setLayout(new MigLayout(""));
        this.setAlpha(0.9f);
        this.setOpaque(false);
        this.setBackground(ThemeColor.noColor);
        this.add(centerArea, "pos 0.5al 0.5al");
    }

}
