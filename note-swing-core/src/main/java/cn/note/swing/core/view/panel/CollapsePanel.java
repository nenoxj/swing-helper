package cn.note.swing.core.view.panel;

import cn.note.swing.core.view.theme.LightThemeUI;
import cn.note.swing.core.view.theme.ThemeIcon;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 基于JXTitledPanel 扩展折叠面板
 *
 * @author jee
 */
public class CollapsePanel extends JXTitledPanel implements LightThemeUI {

    /**
     * 折叠开关
     */
    private JButton expandedSwitch;

    /**
     * 展开图标
     */
    private Icon expandedIcon;

    /**
     * 收起图标
     */
    private Icon collapsedIcon;


    public CollapsePanel() {
        this.setTitlePainter(backgroundPainter());
        this.setTitleForeground(foregroundColor());
        expandedSwitch = optIconBtn(ThemeIcon.expandedIcon, "", (e) -> toggleVisible());
        this.expandedIcon = ThemeIcon.expandedIcon;
        this.collapsedIcon = ThemeIcon.collapsedIcon;
        this.setLeftDecoration(expandedSwitch);
        this.getLeftDecoration().getParent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    toggleVisible();
                }
            }
        });
    }


    private void toggleVisible() {
        Container container = this.getContentContainer();
        boolean visible = container.isVisible();
        container.setVisible(!visible);
        if (container.isVisible()) {
            expandedSwitch.setIcon(this.expandedIcon);
        } else {
            expandedSwitch.setIcon(this.collapsedIcon);
        }
    }

}
