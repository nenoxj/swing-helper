package cn.note.swing.view.qacard;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.base.BorderBuilder;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.LightThemeUI;
import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.view.qacard.bean.QACard;
import cn.note.swing.view.qacard.bean.QACategory;
import cn.note.swing.view.qacard.service.QACardService;
import cn.note.swing.view.qacard.service.impl.QACardServiceImpl;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Q&A问答面板
 */
@Slf4j
public class QACardPanelList extends JScrollPane implements LightThemeUI {
    /* 内部面板*/
    private JPanel innerPanel;

    /* 内部问题列表*/
    private List<QACard> qaCardList;

    /* 高度*/
    private int unitHeight;

    /* 总行数*/
    private int totalLine;

    /* 空的添加按钮*/
    private JButton blankAdd;

    /* qaCard service */
    private QACardService qaCardService;


    public QACardPanelList(QACategory qaCategory, int colSize, int unitHeight) {
        if (colSize < 0 || unitHeight < 0) {
            throw new IllegalArgumentException("colSize || unitHeight 必须大于0");
        }
        setJScrollPaneUI(this);
        this.unitHeight = unitHeight;
        String colConstraints = StrUtil.repeat("[33%]", colSize);

        // 宽度与scrollPane一致,  依据colSize 动态换行
        this.innerPanel = new JPanel(new MigLayout("wrap " + colSize, colConstraints)) {
            @Override
            public Dimension getPreferredSize() {
                int totalCard = qaCardList.size();
                int verticalScrollWidth = QACardPanelList.this.getVerticalScrollBar().getWidth();
                if (Math.floorMod(totalCard, colSize) == 0) {
                    totalLine = Math.floorDiv(totalCard, colSize);
                } else {
                    totalLine = Math.floorDiv(totalCard, colSize) + 1;
                }
                int totalWidth = QACardPanelList.this.getWidth() - verticalScrollWidth - 20;
                return new Dimension(totalWidth, totalLine > 0 ? unitHeight * totalLine : unitHeight);
            }
        };
        addBlankAdd();
        this.setViewportView(innerPanel);
        this.setBorder(BorderBuilder.topBorder(1, grayColor()));
        this.qaCardService = new QACardServiceImpl(qaCategory);
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        this.qaCardList = qaCardService.queryQaCardList();
        qaCardList.forEach(qaCard -> {
            QACardPanelItem qaCardPanelItem = new QACardPanelItem(QACardPanelList.this, qaCard);
            innerPanel.add(qaCardPanelItem, "growx,h " + this.unitHeight);
        });
    }

    /**
     * 添加面板
     */
    protected void addBlankAdd() {
        int size = Double.valueOf(0.6 * unitHeight).intValue();
        FlatSVGIcon svgIcon = new FlatSVGIcon(SvgIconFactory.Common.add, size, size);
        FlatWrapper.icon(svgIcon, foregroundColor());
        blankAdd = new JButton(svgIcon);
        blankAdd.setBackground(backgroundColor());
        blankAdd.addActionListener(e -> {
            QACardPanelItem qaCardPanelItem = new QACardPanelItem(QACardPanelList.this, new QACard());
            qaCardPanelItem.setAddStatus();
            addQACardItem(qaCardPanelItem);
            innerPanel.revalidate();
            //自动滚动至底部
            SwingCoreUtil.onceTimer(100, () -> QACardPanelList.this.getVerticalScrollBar().setValue(totalLine * unitHeight));
        });
        innerPanel.add(blankAdd, "growx,h " + this.unitHeight);
    }


    /**
     * 添加问答卡片
     */
    public void addQACardItem(QACardPanelItem qaCardPanelItem) {
        qaCardList.add(qaCardPanelItem.getQACard());
        innerPanel.add(qaCardPanelItem, "growx,h " + this.unitHeight);
    }

    /**
     * 删除卡片
     */
    public void deleteQACardItem(QACardPanelItem qaCardPanelItem, boolean isSave) {
        qaCardList.remove(qaCardPanelItem.getQACard());
        if (isSave) {
            if (this.save()) {
                removeQACardItem(qaCardPanelItem);
            } else {
                // 删除失败时,保持引用
                qaCardList.add(qaCardPanelItem.getQACard());
            }
        } else {
            removeQACardItem(qaCardPanelItem);
        }
    }


    private void removeQACardItem(QACardPanelItem qaCardPanelItem) {
        innerPanel.remove(qaCardPanelItem);
        innerPanel.revalidate();
        innerPanel.repaint();
    }


    /**
     * 保存信息
     */
    public boolean save() {
        try {
            qaCardService.save(qaCardList);
            return true;
        } catch (Exception e) {
            MessageBuilder.error(this, "保存失败:" + e.getMessage());
        }
        return false;
    }

}
