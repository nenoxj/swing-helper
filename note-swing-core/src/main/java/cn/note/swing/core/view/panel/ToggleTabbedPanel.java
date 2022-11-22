package cn.note.swing.core.view.panel;


import cn.note.swing.core.view.theme.DarkThemeUI;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 选项切换面板
 */
public class ToggleTabbedPanel extends JPanel implements DarkThemeUI {

    /*右侧容器*/
    private JPanel container;

    /*按钮组*/
    private ButtonGroup buttonGroup;

    /*左侧面板*/
    private JXPanel leftPanel;

    /*button列表*/
    private List<JToggleButton> buttonList;


    public ToggleTabbedPanel() {
        super.setLayout(new MigLayout("insets 0,gap 0", "[60][grow]", "[grow]"));
        container = new JPanel(new GridLayout(1, 1));
        buttonGroup = new ButtonGroup();
        leftPanel = new JXPanel(new MigLayout("wrap 1,insets 0,gap 0", "[grow]", ""));
        // 使用自定义绘制器
        leftPanel.setBackgroundPainter(verticalLinePainter(0.2f));
        buttonList = new ArrayList<>();
        super.add(leftPanel, "grow,cell 0 0");
        super.add(container, "grow,cell 1 0");

    }


    /**
     * 设置选择下标
     *
     * @param index 下标索引
     */
    public void setSelected(int index) {
        buttonGroup.setSelected(buttonList.get(index).getModel(), true);
    }

    public void addTab(String iconPath, String tip, Supplier<Component> component) {
        addTab(iconPath, tip, component, false);
    }

    /**
     * 插入ToggleButton 作为选项卡
     */
    public void addTab(String iconPath, String tip, Supplier<Component> component, boolean selected) {
        JToggleButton button = tabIconToggleButton(iconPath);
        balloonTip(button, tip);
        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setBorderPainted(true);
                container.removeAll();
                container.add(component.get());
                container.revalidate();
                container.repaint();
            } else {
                button.setBorderPainted(false);
            }
        });

        buttonGroup.add(button);
        buttonList.add(button);
        leftPanel.add(button, "growx,h 55!");
        buttonGroup.setSelected(button.getModel(), selected);
    }

}
