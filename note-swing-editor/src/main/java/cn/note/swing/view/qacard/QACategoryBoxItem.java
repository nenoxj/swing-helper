package cn.note.swing.view.qacard;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.lifecycle.InitListener;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.theme.LightThemeUI;
import cn.note.swing.view.qacard.bean.QACategory;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * item操作视图
 */
public class QACategoryBoxItem extends JPanel implements LightThemeUI, InitListener {

    private JPanel view;

    /*item*/
    private JTextField item;

    /* item 容器*/
    private JPanel itemContainer;

    /* opt容器 */
    private JPanel optContainer;

    /*跟新*/
    private JButton optEdit;

    /*确定*/
    private JButton optConfirm;

    /*取消*/
    private JButton optCancel;

    /*删除*/
    private JButton optDelete;

    /* 父组件列表 */
    private QACategoryBoxList qACategoryBoxList;

    /* 面板列表*/
    private QACardPanelList qaCardPanelList;

    /* 分类对象*/
    private QACategory qaCategory;

    public QACategoryBoxItem(QACategoryBoxList qACategoryBoxList, QACategory qaCategory) {
        this.qaCategory = qaCategory;
        this.qACategoryBoxList = qACategoryBoxList;
        this.qaCardPanelList = new QACardPanelList(qaCategory, 3, 200);
        init();
    }


    public String getItemText() {
        return item.getText();
    }


    public QACardPanelList getQaCardPanelList() {
        return qaCardPanelList;
    }


    protected void init() {
        view = new JPanel(new MigLayout("gap 0,insets 2 5 2 5", "[grow,fill][60]", ""));
        initItemContainer();
        initItemOptContainer();
        view.add(itemContainer, "grow,w ::120");
        view.add(optContainer);
        super.setLayout(new GridLayout(1, 1));
        super.add(view);
        bindEvents();
        this.putClientProperty("active", false);
    }

    /**
     * 创建item样式
     */
    protected void initItemContainer() {
        /*item样式*/
        item = lineInnerTextField(qaCategory.getCategoryName());
        KeyActionFactory.bindEnterAction(item, new ConsumerAction(e -> setConfirmStatus(true)));
        KeyActionFactory.bindEscAction(item, new ConsumerAction(e -> setConfirmStatus(false)));

        /* item 容器*/
        itemContainer = new JPanel(new GridLayout(1, 1));
        itemContainer.setOpaque(false);
        itemContainer.add(item);
    }


    /**
     * 创建item操作容器
     */
    protected void initItemOptContainer() {
        optContainer = new JPanel();
        optContainer.setOpaque(false);
        optEdit = optIconEdit(e -> setEditStatus(true));
        optDelete = optIconDelete(e -> deleteAction());
        optConfirm = optIconConfirm(e -> setConfirmStatus(true));
        optCancel = optIconCancel(e -> setConfirmStatus(false));

        optContainer.add(optDelete);
        optContainer.add(optEdit);
        optContainer.add(optConfirm);
        optContainer.add(optCancel);
    }

    /**
     * 设置新增状态
     */
    public void setAddStatus() {
        setEditStatus(true);
        optCancel.setVisible(false);
        SwingCoreUtil.onceTimer(30, () -> item.requestFocusInWindow());
    }

    /**
     * 设置确认状态
     */
    private void setConfirmStatus(boolean confirmStatus) {
        String editItemText = item.getText();
        if (confirmStatus && StrUtil.isBlank(editItemText)) {
            MessageBuilder.error(this, "分类不能为空!");
            item.requestFocusInWindow();
            return;
        }
        if (confirmStatus) {
            // 未修改
            if (StrUtil.equals(qaCategory.getCategoryName(), editItemText)) {
                setEditStatus(false);
            } else {
                if (qACategoryBoxList.containsCategory(editItemText)) {
                    MessageBuilder.error(this, "分类已存在!");
                    item.requestFocusInWindow();
                    return;
                }
                // 保存分类
                qaCategory.setCategoryName(editItemText);
                qACategoryBoxList.saveCategory(this, qaCategory);
            }
        } else {
            item.setText(qaCategory.getCategoryName());
            setEditStatus(false);
        }

    }


    /**
     * 设置编辑状态
     */
    public void setEditStatus(boolean editStatus) {
        optConfirm.setVisible(editStatus);
        optCancel.setVisible(editStatus);
        optEdit.setVisible(!editStatus);
        item.setEnabled(editStatus);
        if (editStatus) {
            item.requestFocusInWindow();
            item.setBackground(backgroundColor());
        } else {
            item.setBackground(blankColor());
        }
    }


    @InitAction("容器悬浮颜色")
    private void itemContainerAction() {
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 双击提示
                if (!item.isEnabled() && e.getClickCount() == 1) {
                    doubleClicked();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boolean active = (boolean) QACategoryBoxItem.this.getClientProperty("active");
                if (!active) {
                    view.setBackground(backgroundColor());
                }

            }
        });

        item.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!item.isEnabled()) {
                    view.setBackground(primaryColor());
                }
            }
        });
    }


    @InitAction("激活背景颜色")
    private void activeBackgroundListener() {
        this.addPropertyChangeListener(evt -> {
            boolean active = (boolean) QACategoryBoxItem.this.getClientProperty("active");
            if (active) {
                view.setBackground(primaryColor());
            } else {
                view.setBackground(backgroundColor());
            }
        });
    }

    /**
     * 双击事件
     */
    protected void doubleClicked() {
        System.out.println("active " + qaCategory.getCategoryName());
        qACategoryBoxList.setActiveBoxItem(this);
    }


    protected void deleteAction() {
        String itemText = item.getText();
        if (StrUtil.isBlank(itemText)) {
            qACategoryBoxList.deleteBoxItem(this);
        } else {
            deleteDialogWindow(this, "删除分类", itemText, () ->
                    qACategoryBoxList.deleteCategory(this, qaCategory));
        }

    }
}
