package cn.note.swing.view.qacard;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.event.key.KeyActionStatus;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.lifecycle.InitListener;
import cn.note.swing.core.view.base.FontBuilder;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.theme.LightThemeUI;
import cn.note.swing.view.qacard.bean.QACategory;
import cn.note.swing.view.qacard.service.QACategoryService;
import cn.note.swing.view.qacard.service.impl.QACategoryServiceImpl;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 垂直滚动盒子
 */
public class QACategoryBoxList extends JScrollPane implements LightThemeUI, InitListener {

    private Box box;

    private Component glue;

    /* 元素集合*/
    private List<QACategoryBoxItem> itemCacheList;

    /* 搜索框*/
    private JTextField search;

    /*激活选中的item*/
    private QACategoryBoxItem activeBoxItem;

    /*内部面板*/
    private JPanel qaCardPanel;

    /*qacard容器 */
    private JPanel qaCardContainer;

    /*分类*/
    private JButton tag;

    /*描述*/
    private JPanel descriptionPanel;

    /* question service*/
    @Getter
    private QACategoryService qaCategoryService;

    public JPanel getQaCardPanel() {
        return qaCardPanel;
    }

    public QACategoryBoxList() {
        qaCategoryService = new QACategoryServiceImpl();
        box = Box.createVerticalBox();
        glue = Box.createVerticalGlue();
        itemCacheList = new ArrayList<>();
        setJScrollPaneUI(this);
        getVerticalScrollBar().setUnitIncrement(25);
        setViewportView(box);
        createSearchAddPanel();
        initQACardPanel();
        initData();
        bindEvents();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        List<QACategory> categoryList = qaCategoryService.categoryList();
        categoryList.forEach(qaCategory -> {
            QACategoryBoxItem boxItem = new QACategoryBoxItem(QACategoryBoxList.this, qaCategory);
            addBoxItem(boxItem);
        });
        if (itemCacheList.size() > 0) {
            setActiveBoxItem(itemCacheList.get(0));
        } else {
            setActiveBoxItem(null);
        }

    }

    /**
     * 创建容器面板
     */
    private void initQACardPanel() {
        qaCardPanel = new JPanel(new MigLayout("insets 0,gap 0", "[grow]", "[25][grow]"));
        createDescription();
        JPanel statusOptPanel = new JPanel();
        tag = tagBtn("");
        tag.setVisible(false);
        statusOptPanel.add(tag);
        qaCardContainer = new JPanel(new GridLayout(1, 1));
        qaCardContainer.add(descriptionPanel);
        qaCardPanel.add(statusOptPanel, "right,cell 0 0");
        qaCardPanel.add(qaCardContainer, "grow,cell 0 1");
    }


    private void createDescription() {
        descriptionPanel = new JPanel(new MigLayout("center", "[grow]", "[grow]"));
        Font font = FontBuilder.increaseLabelFont(2);
        JLabel description = new JLabel("点击添加创建Q&A");
        description.setFont(font);
        description.setBackground(foregroundColor());
        descriptionPanel.add(description, "center");

    }

    /**
     * 设置激活节点
     */
    public void setActiveBoxItem(QACategoryBoxItem activeBoxItem) {
        if (activeBoxItem == null) {
            tag.setVisible(false);
            qaCardContainer.removeAll();
            qaCardContainer.add(descriptionPanel);
            qaCardContainer.revalidate();
            qaCardContainer.repaint();
            this.activeBoxItem = null;
        } else {
            if (!activeBoxItem.equals(this.activeBoxItem)) {
                if (this.activeBoxItem != null) {
                    this.activeBoxItem.putClientProperty("active", false);
                }
                qaCardContainer.removeAll();
                qaCardContainer.add(activeBoxItem.getQaCardPanelList());
                qaCardContainer.revalidate();
                qaCardContainer.repaint();
                tag.setText(activeBoxItem.getItemText());
                tag.setVisible(true);
                activeBoxItem.putClientProperty("active", true);
                this.activeBoxItem = activeBoxItem;
            }
        }


    }

    public void addBox(JComponent component) {
        component.setMaximumSize(new Dimension(Short.MAX_VALUE, component.getPreferredSize().height));
        box.remove(glue);
        box.add(component);
        box.add(glue);
        box.revalidate();
        EventQueue.invokeLater(() -> component.scrollRectToVisible(component.getBounds()));
    }

    /**
     * @param QACategoryBoxItem box分类
     */
    public void addBoxItem(QACategoryBoxItem QACategoryBoxItem) {
        addBox(QACategoryBoxItem);
        itemCacheList.add(QACategoryBoxItem);
    }


    public void deleteBoxItem(QACategoryBoxItem boxItem) {
        itemCacheList.remove(boxItem);
        box.remove(glue);
        box.remove(boxItem);
        box.add(glue);
        box.revalidate();
        super.repaint();
    }


    protected void createSearchAddPanel() {
        // 创建外层容器
        JPanel searchContainer = new JPanel(new GridLayout(1, 1));
        JPanel innerContainer = new JPanel(new MigLayout("", "[grow][30]"));
        searchContainer.add(innerContainer);

        // 内部容器
        search = lineSearchTextField();
        KeyActionFactory.bindEnterAction(search, new ConsumerAction(e -> doSearch()));

        // 添加
        JButton add = optIconAdd(e -> doCreate());
        innerContainer.add(search, "growx");
        innerContainer.add(add);
        addBox(searchContainer);

    }

    /**
     * 搜索面板
     */
    private void doSearch() {
        String searchText = search.getText();
        if (StrUtil.isBlank(searchText)) {
            itemCacheList.forEach(boxItem -> boxItem.setVisible(true));
        } else {
            itemCacheList.forEach(boxItem -> {
                String itemText = boxItem.getItemText();
                if (itemText.contains(searchText)) {
                    boxItem.setVisible(true);
                } else {
                    boxItem.setVisible(false);
                }
            });
        }
    }


    /**
     * 创建操作
     */
    private void doCreate() {
        QACategoryBoxItem QACategoryBoxItem = new QACategoryBoxItem(QACategoryBoxList.this, new QACategory());
        QACategoryBoxItem.setAddStatus();
        addBoxItem(QACategoryBoxItem);
    }


    /**
     * 是否存在分类
     *
     * @param categoryName 分类名称
     * @return true/false
     */
    public boolean containsCategory(String categoryName) {
        return qaCategoryService.contains(categoryName);
    }

    /**
     * 保存分类
     *
     * @param qaCategory 分类元素
     * @return 保存结果
     */
    public void saveCategory(QACategoryBoxItem qaCategoryBoxItem, QACategory qaCategory) {
        try {
            if (!itemCacheList.contains(qaCategoryBoxItem)) {
                itemCacheList.add(qaCategoryBoxItem);
            }
            boolean saveResult = qaCategoryService.save(qaCategory);
            if (saveResult) {
                setActiveBoxItem(qaCategoryBoxItem);
                tag.setText(qaCategory.getCategoryName());
                qaCategoryBoxItem.setEditStatus(false);
            }
        } catch (Exception e) {
            MessageBuilder.error(this, "保存失败:" + e.getMessage());
        }
    }


    /**
     * 删除分类
     *
     * @param qaCategoryBoxItem 节点元素
     * @param qaCategory        分类信息
     */
    public void deleteCategory(QACategoryBoxItem qaCategoryBoxItem, QACategory qaCategory) {
        try {
            boolean deleteResult = qaCategoryService.delete(qaCategory);
            if (deleteResult) {
                deleteBoxItem(qaCategoryBoxItem);
                if (itemCacheList.size() > 0) {
                    setActiveBoxItem(itemCacheList.get(0));
                } else {
                    setActiveBoxItem(null);
                }

            }
        } catch (Exception e) {
            MessageBuilder.error(this, "删除失败:" + e.getMessage());
        }
    }


    @InitAction("F2快捷修改")
    private void bindF2Action() {
        KeyStroke updateKey = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        KeyActionFactory.bindKeyAction(this, "updateName", updateKey, new ConsumerAction(e -> {
            if (this.activeBoxItem != null) {
                this.activeBoxItem.setEditStatus(true);
            }
        }), KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);
    }
}