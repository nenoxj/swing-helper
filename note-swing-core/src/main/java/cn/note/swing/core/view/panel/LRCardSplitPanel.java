package cn.note.swing.core.view.panel;

import cn.hutool.core.util.StrUtil;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 左右卡片分割面板
 */
public class LRCardSplitPanel extends JXMultiSplitPane {

    public static final float MAX_SCALE = 1.0f;

    private final String leftViewName = "leftViewName";

    private final String rightViewName = "rightViewName";

    /*布局样式*/
    private MultiSplitLayout multiSplitLayout;

    /* 左侧卡片*/
    private JPanel leftCard;
    private CardLayout leftCardLayout;
    private JScrollPane leftScrollPane;

    /* 右侧卡片*/
    private JPanel rightCard;
    private CardLayout rightCardLayout;

    /* card 集合*/
    private List<LRCard> cardList;

    /* 左侧比例*/
    private Float leftScale;


    public LRCardSplitPanel(float leftScale) {
        this(leftScale, true);
    }

    public LRCardSplitPanel() {
        this(0.2f, false);
    }

    public LRCardSplitPanel(float leftScale, boolean leftScroll) {
        this.leftScale = leftScale;
        initCard();
        super.setLayout(multiSplitLayout);
        if (leftScroll) {
            leftScrollPane = new JScrollPane(leftCard);
            super.add(leftViewName, leftScrollPane);
        } else {
            super.add(leftViewName, leftCard);
        }
        super.add(rightViewName, rightCard);
        layoutWeightByLeft();

    }


    private void initCard() {
        this.leftCardLayout = new CardLayout();
        this.leftCard = new JPanel(leftCardLayout);
        this.rightCardLayout = new CardLayout();
        this.rightCard = new JPanel(rightCardLayout);
        cardList = new ArrayList<>();
        float rightScale = MAX_SCALE - leftScale;
        /* 布局样式*/
        String spiltLayout = "(ROW (LEAF name={} weight={} ) (LEAF name={} weight={}))";
        spiltLayout = StrUtil.format(spiltLayout, leftViewName, leftScale, rightViewName, rightScale);
        multiSplitLayout = new MultiSplitLayout(MultiSplitLayout.parseModel(spiltLayout));
    }


    private void layoutWeightByLeft() {
        MultiSplitLayout.Node node = multiSplitLayout.getNodeForName(leftViewName);
        JComponent leftComponent = (JComponent) multiSplitLayout.getComponentForNode(node);
        multiSplitLayout.layoutByWeight(leftComponent);
    }


    /**
     * 隐藏左侧卡片
     * 如果右侧为滚动面板时, 会触发一直闪烁
     * 右侧可以设置为 自适应面板
     */
    public void displayLeftCard(boolean visible) {
        multiSplitLayout.displayNode(leftViewName, visible);
        this.revalidate();
    }

    /**
     * 隐藏右侧卡片
     */
    public void displayRightCard(boolean visible) {
        multiSplitLayout.displayNode(rightViewName, visible);
        this.revalidate();
    }


    /**
     * 设置左侧最小宽度
     */
    public void setLeftMinSize(int width) {
        Dimension minSize = new Dimension(width, super.getHeight());
        if (leftScrollPane != null) {
            leftScrollPane.setMinimumSize(minSize);
        } else {
            leftCard.setMinimumSize(minSize);
        }
    }

    /**
     * 设置后侧最小宽度
     */
    public void setRightMinSize(int width) {
        Dimension minSize = new Dimension(width, super.getHeight());
        rightCard.setMinimumSize(minSize);
    }


    public void addCard(@Nonnull LRCard lrCard) {
        addCard(lrCard, false);
    }

    /**
     * 添加卡片
     */
    public void addCard(@Nonnull LRCard lrCard, boolean active) {
        JComponent left = lrCard.getLeft();
        JComponent right = lrCard.getRight();
        leftCard.add(lrCard.getLeft(), lrCard.getLeftKey());

        // 右侧面板可为null
        if (lrCard.getRight() != null) {
            rightCard.add(lrCard.getRight(), lrCard.getRightKey());
        }

        // 激活面板
        if (active) {
            activeCard(lrCard);
        }
        cardList.add(lrCard);
    }


    /**
     * 根据key激活面板
     */
    public void activeCard(int index) {
        activeCard(cardList.get(index));
    }


    /**
     * 激活card
     */
    public void activeCard(LRCard lrCard) {
        if (lrCard != null) {
            String leftKey = lrCard.getLeftKey();
            leftCardLayout.show(leftCard, leftKey);
            String rightKey = lrCard.getRightKey();
            JComponent right = lrCard.getRight();
            if (right != null) {
                rightCardLayout.show(rightCard, rightKey);
                displayRightCard(true);
            } else {
                displayRightCard(false);
            }

        }
    }

}
