package cn.note.swing.core.view.base;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.util.function.IntConsumer;

/**
 * 实现选项卡的常用风格
 */
public class TabbedPaneFactory {


    /**
     * 单行可滚动,
     *
     * @return 创建基本风格的选项卡
     */
    public static JTabbedPane createBaseTab() {
        JTabbedPane tabbedPane = new JTabbedPane();
        // 可关闭
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSABLE, true);
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK, ((IntConsumer) tabIndex -> {
            tabbedPane.removeTabAt(tabIndex);
        }));
        //隐藏上方滚动条
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_SHOW_CONTENT_SEPARATOR, false);
        // 滚动风格
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.putClientProperty(FlatClientProperties.STYLE, "arrowType:triangle");
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_SCROLL_BUTTONS_POLICY, FlatClientProperties.TABBED_PANE_POLICY_AS_NEEDED);
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_SCROLL_BUTTONS_PLACEMENT, FlatClientProperties.TABBED_PANE_PLACEMENT_BOTH);
        return tabbedPane;
    }





}
