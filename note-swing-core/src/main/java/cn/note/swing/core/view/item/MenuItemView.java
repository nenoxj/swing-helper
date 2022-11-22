package cn.note.swing.core.view.item;

import cn.note.swing.core.view.base.BorderBuilder;
import cn.note.swing.core.view.loading.SpinLoading;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.MultiSplitLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jee
 */
@Slf4j
public class MenuItemView {

    private static final String MAIN_VIEW_ITEM_PROPERTY = "mainview.item";

    /**
     * 分割样式
     */
    private MultiSplitLayout multiSplitLayout;

    /**
     * 左侧区域
     */
    @Getter
    private ItemSelector itemSelector;

    /**
     * 右侧区域
     */
    @Getter
    private JXPanel itemContainer;

    /**
     * 主区域
     */
    private static final String AREA_CONTAINER = "areaContainer";

    /**
     * 菜单区域
     */
    private static final String AREA_MENU = "areaMenu";

    /**
     * 运行过的item
     */
    private Map<String, JComponent> runningItemCache;

    /**
     * item selected变化监听器
     */
    private PropertyChangeListener itemPropertyChangeLister;

    /**
     * 实例化菜单数量
     */
    private int menuSize;

    public MenuItemView(List<ItemNode> menuItems) {
        this.initProperties(menuItems);
    }

    /*初始化配置项*/
    private void initProperties(List<ItemNode> menuItems) {
        runningItemCache = new HashMap<>();
        for (ItemNode itemNode : menuItems) {
            itemNode.addPropertyChangeListener(getItemPropertyChangeListener());
        }
        menuSize = menuItems.size();
        itemSelector = new ItemSelector(menuItems);
        itemSelector.addPropertyChangeListener(new ItemSelectionListener());
        itemContainer = new JXPanel(new GridLayout(1, 1));
    }


    /**
     * 设置默认选择面板
     */
    public void setDefaultSelectedItem() {
        if (menuSize > 0) {
            itemSelector.setDefaultSelectedItem();
        } else {
            log.warn("加载menu数量为0!!!");
        }
    }


    /**
     * 初始化主布局
     *
     * @return 创建分割面板
     */
    public JXMultiSplitPane create() {
        String layout = "(ROW " + "(LEAF name=" + AREA_MENU + " weight=0.16)" + "(LEAF name=" + AREA_CONTAINER + " weight=0.84)" + ")";
        multiSplitLayout = new MultiSplitLayout(MultiSplitLayout.parseModel(layout));
        JXMultiSplitPane splitPane = new JXMultiSplitPane();
        splitPane.setLayout(multiSplitLayout);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        JComponent menuSelector = itemSelector.getSelectorComponent();
        menuSelector.setBorder(BorderBuilder.rightBorder(1, Color.black));
        splitPane.add(menuSelector, AREA_MENU);
        menuSelector.setDoubleBuffered(true);
        // Create panel to contain currently running demo
        splitPane.add(itemContainer, AREA_CONTAINER);
        multiSplitLayout.layoutByWeight(itemContainer);
        return splitPane;
    }


    /**
     * 注册item点击事件
     */
    private class ItemSelectionListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (event.getPropertyName().equals(ItemSelector.ITEM_SELECTOR_PROPERTY)) {
                updateSelectedItemChanged();
            }
        }
    }

    /**
     * 更新item变更事件
     */
    private void updateSelectedItemChanged() {
        ItemNode item = itemSelector.getSelectedItem();
        if (item != null) {
            String itemName = item.getName();

            JComponent itemPanel = runningItemCache.get(itemName);
            if (itemPanel == null || item.getNodeComponent() == null) {
                item.startInitializing();
                itemPanel = createLoadingItem(item);
//                itemPanel = createSimpleItem(item);
                if (itemPanel != null) {
                    runningItemCache.put(itemName, itemPanel);
                } else {
                    log.error("itemPanel无法创建!");
                }
            }
            if (itemPanel != null) {
                itemContainer.removeAll();
                itemContainer.add(itemPanel, BorderLayout.CENTER);
                itemContainer.revalidate();
                itemContainer.repaint();
                // 计算
//                multiSplitLayout.layoutByWeight(getComponentByConstraint(AREA_MENU));
            }
        }
    }

    /**
     * @param item ItemNode
     * @return 创建带滚动的item
     */
    private JComponent createSimpleItem(ItemNode item) {
        try {
            Component comp = item.createNodeComponent();
            return new JScrollPane(comp);
        } catch (Exception e) {
            log.error("创建originItem 异常", e);
        }
        return null;
    }

    private JComponent createLoadingItem(ItemNode item) {
        try {
            Component comp = item.createNodeComponent();
            // 不考虑加滚动条
            return new SpinLoading<Component>(comp);
        } catch (Exception e) {
            log.error("创建loadingItem 异常", e);
        }
        return null;
    }

    private PropertyChangeListener getItemPropertyChangeListener() {
        if (itemPropertyChangeLister == null) {
            itemPropertyChangeLister = new ItemNotePropertyChangeListener();
        }
        return itemPropertyChangeLister;
    }

    /**
     * item 事件监听器
     */
    private class ItemNotePropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (propertyName.equals(ItemNode.ITEM_PROPERTY)) {
                ItemNode itemNode = (ItemNode) e.getSource();
                JComponent itemCompoent = (JComponent) e.getNewValue();
                if (itemCompoent != null) {
                    itemCompoent.putClientProperty(MAIN_VIEW_ITEM_PROPERTY, itemNode);
                    itemCompoent.addHierarchyListener(new ItemNoteVisibilityListener());
                }
            }
        }
    }

    /**
     * item 显示隐藏监听器
     */
    private class ItemNoteVisibilityListener implements HierarchyListener {
        @Override
        public void hierarchyChanged(HierarchyEvent event) {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) > 0) {
                JComponent component = (JComponent) event.getComponent();
                final ItemNode itemNode = (ItemNode) component.getClientProperty(MAIN_VIEW_ITEM_PROPERTY);
                if (!component.isShowing()) {
                    itemNode.stop();
                } else {
                    itemContainer.revalidate();
                    EventQueue.invokeLater(itemNode::start);
                }
            }
        }
    }


}
