package cn.note.swing.core.view.panel;


import cn.note.swing.core.view.theme.DarkThemeUI;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 选项切换面板
 */
public class ToggleLRCardPanel extends JPanel implements DarkThemeUI {

    /*左侧面板*/
    private JXPanel leftPanel;

    /*右侧容器*/
    private LRCardSplitPanel lrCardSplitPanel;

    /*按钮组*/
    private ButtonGroup buttonGroup;

    /*button列表*/
    private List<JToggleButton> buttonList;


    public ToggleLRCardPanel() {
        super.setLayout(new MigLayout("insets 0,gap 0", "[60][grow]", "[grow]"));
        lrCardSplitPanel = new LRCardSplitPanel();
        buttonGroup = new ButtonGroup();
        leftPanel = new JXPanel(new MigLayout("wrap 1,insets 0,gap 0", "[grow]", ""));
        // 使用自定义绘制器
        leftPanel.setBackgroundPainter(verticalLinePainter(0.2f));
        buttonList = new ArrayList<>();
        super.add(leftPanel, "grow,cell 0 0");
        super.add(lrCardSplitPanel, "grow,cell 1 0");

    }

    /**
     * 设置选择下标
     *
     * @param index 下标索引
     */
    public void setSelected(int index) {
        buttonGroup.setSelected(buttonList.get(index).getModel(), true);
    }


    public void addTab(String iconPath, String tip, LRCard lrCard) {
        addTab(iconPath, tip, lrCard, false);
    }

    public void addTab(String iconPath, String tip, LRCardView lrCardView) {
        addTab(iconPath, tip, lrCardView.getCardView(), false);
    }

    public void addTab(String iconPath, String tip, LRCardView lrCardView, boolean selected) {
        addTab(iconPath, tip, lrCardView.getCardView(), selected);
    }

    /**
     * 插入ToggleButton 作为选项卡
     */
    public void addTab(String iconPath, String tip, LRCard lrCard, boolean selected) {
        JToggleButton button = tabIconToggleButton(iconPath);
        balloonTip(button, tip);
        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setBorderPainted(true);
                lrCardSplitPanel.activeCard(lrCard);
            } else {
                button.setBorderPainted(false);
            }
        });
        lrCardSplitPanel.addCard(lrCard);
        buttonGroup.add(button);
        buttonList.add(button);
        leftPanel.add(button, "growx,h 55!");
        buttonGroup.setSelected(button.getModel(), selected);
    }

}
