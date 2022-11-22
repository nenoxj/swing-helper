package cn.note.swing.core.view.item;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.item.ui.FlatLafTaskPaneUI;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 节点选择器
 * @author: jee
 */
@Slf4j
public class ItemSelector extends AbstractBean {

    public static final String ITEM_SELECTOR_PROPERTY = "selectedItem";

    /**
     * 选择的item节点
     */
    private ItemNode selectedItem;
    /**
     * key: 面板(items)
     */
    private Map<String, JXTaskPane> categoryMap;
    /**
     * 所有的items
     */
    private List<ItemNode> itemSet;
    /**
     * 选择的组件
     */
    private JComponent selectorComponent;

    private ButtonGroup itemGroups;


    public ItemSelector() {
        this(Collections.emptyList());
    }

    public ItemSelector(List<ItemNode> items) {
        itemSet = items;
        itemGroups = new ButtonGroup();
    }

    public void setDefaultSelectedItem() {
        if (itemSet.size() > 0) {
            setSelectedItem(itemSet.get(0));
        }
    }

    /**
     * 是否默认选择的item
     *
     * @param item
     * @return
     */
    public boolean isDefaultSelectedItem(ItemNode item) {
        return itemSet.size() == 0 ? false : itemSet.get(0).equals(item);
    }

    /**
     * 获取可选择的item面板组件
     *
     * @return
     */
    public JComponent getSelectorComponent() {
        if (selectorComponent != null) {
            return selectorComponent;
        }
        selectorComponent = createSelectorComponent(itemSet);
        return selectorComponent;
    }

    /**
     * @param itemSet 节点结合
     * @return 节点jxtaskpane容器面板
     */
    private JComponent createSelectorComponent(List<ItemNode> itemSet) {
        JXTaskPaneContainer container = new JXTaskPaneContainer();
        container.setLayout(new MigLayout("wrap 1,insets 0,gap 0", "[grow]", "[]"));
        for (ItemNode item : itemSet) {
            // 单独节点
            if (StrUtil.isBlank(item.getCategory())) {
                container.add(createToggleItem(item, container.getBackground()), "grow");
            } else {
                // 折叠节点
                String category = item.getCategory();
                JXTaskPane taskPane = getTaskPane(category);
                if (taskPane == null) {
                    taskPane = createTaskPane(item);
                    this.addTaskPane(taskPane, category);
                    container.add(taskPane, "grow");
                }
                taskPane.add(createToggleItem(item, taskPane.getBackground()), "grow");
            }
        }
//        JScrollPane scrollPane = new JScrollPane(container);
        return container;
    }


    /**
     * @param taskPane 折叠面板
     * @param category 分类
     */
    private void addTaskPane(JXTaskPane taskPane, String category) {
        if (categoryMap == null) {
            categoryMap = new HashMap<String, JXTaskPane>();
        }
        categoryMap.put(category, taskPane);
    }

    /**
     * @param category 分类名称
     * @return 获得折叠面板
     */
    private JXTaskPane getTaskPane(String category) {
        if (categoryMap == null) {
            return null;
        }
        return categoryMap.get(category);
    }

    /**
     * @param item 设置选择节点
     */
    public void setSelectedItem(ItemNode item) {
        log.debug("selectItem 视图:{}", item);
        Object old = getSelectedItem();
        this.selectedItem = item;
        super.firePropertyChange(ITEM_SELECTOR_PROPERTY, old, this.getSelectedItem());
    }

    /**
     * @return 获得选择节点
     */
    public ItemNode getSelectedItem() {
        return this.selectedItem;
    }

    /**
     * 创建开关按钮
     *
     * @param itemNode item节点
     * @return
     */
    private JToggleButton createToggleItem(final ItemNode itemNode, Color bgColor) {
        String name = itemNode.getName();
        String iconPath = itemNode.getIcon();
        FlatSVGIcon itemIcon = null;

        if (StrUtil.isNotBlank(iconPath)) {
            itemIcon = SvgIconFactory.icon(itemNode.getIcon());
        }
        JToggleButton toggleButton = ButtonFactory.ghostTaskPaneButton(name, itemIcon, bgColor);
        toggleButton.setToolTipText(itemNode.getDescription());
        toggleButton.addActionListener(e -> {
            setSelectedItem(itemNode);
        });
        itemGroups.add(toggleButton);
        // 默认选中第一个
        if (itemGroups.getButtonCount() == 1) {
            itemGroups.setSelected(toggleButton.getModel(), true);
        }
        return toggleButton;
    }


    /**
     * 创建折叠面板
     *
     * @param item item节点
     * @return 折叠面板
     */
    private JXTaskPane createTaskPane(ItemNode item) {
        JXTaskPane taskPane = new JXTaskPane();
        taskPane.setLayout(new MigLayout("insets 0,gapy 5,wrap 1", "[grow]", "[]"));
        taskPane.setUI(new FlatLafTaskPaneUI());
        taskPane.setTitle(item.getCategory());
        taskPane.setName(item.getCategory());
        return taskPane;
    }

}
