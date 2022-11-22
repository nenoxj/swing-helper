package cn.note.swing.core.view.panel;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;

import javax.swing.*;

/**
 * 简单得左右分割面板
 */
public class LRSplitPanel extends JXMultiSplitPane {
    public static final float MAX_SCALE = 1.0f;
    private MultiSplitLayout multiSplitLayout;
    private final String leftViewName = "leftViewName";
    private final String rightViewName = "rightViewName";


    public LRSplitPanel(JComponent leftComp, JComponent rightComp) {
        this(0.2f, leftComp, rightComp);
    }

    public LRSplitPanel(float leftScale, JComponent leftComp, JComponent rightComp) {
        float rightScale = MAX_SCALE - leftScale;
        if (rightComp instanceof JScrollPane) {
            StaticLog.warn("在LRSplitPane 中需要谨慎使用JScrollPane!!!!");
        }
        String layout = "(ROW (LEAF name={} weight={} ) (LEAF name={} weight={}))";
        layout = StrUtil.format(layout, leftViewName, leftScale, rightViewName, rightScale);
        multiSplitLayout = new MultiSplitLayout(MultiSplitLayout.parseModel(layout));


        super.setLayout(multiSplitLayout);
        super.add(leftViewName, leftComp);
        super.add(rightViewName, rightComp);
        // 取消推拽重绘
//        super.setContinuousLayout(false);
        //根据权重计算
        multiSplitLayout.layoutByWeight(getLeftComponent());
        //取消权重计算,按默认大小分配  setFloatingDividers与setLayoutByWeight 互斥
//        multiSplitLayout.setFloatingDividers(false);
//        multiSplitLayout.setLayoutByWeight(true);
    }

    private JComponent getLeftComponent() {
        MultiSplitLayout.Node node = multiSplitLayout.getNodeForName(leftViewName);
        return (JComponent) multiSplitLayout.getComponentForNode(node);
    }


    /**
     * 如果右侧为滚动面板时, 会触发一直闪烁
     * 右侧可以设置为 自适应面板
     */
    public void displayLeftNode(boolean visible) {
        multiSplitLayout.displayNode(leftViewName, visible);
        this.revalidate();
    }

    public void displayRightNode(boolean visible) {
        multiSplitLayout.displayNode(rightViewName, visible);
        this.revalidate();
    }


}
