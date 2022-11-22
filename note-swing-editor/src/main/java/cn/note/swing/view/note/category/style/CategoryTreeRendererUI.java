package cn.note.swing.view.note.category.style;

import cn.note.swing.core.view.tree.DragTreeCellRenderer;
import cn.note.swing.core.view.tree.DragTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * @description: 自定义tree风格
 * @author: jee
 */
public class CategoryTreeRendererUI extends DragTreeCellRenderer {

    public CategoryTreeRendererUI(DragTreeNode dragTreeNode) {
        super(dragTreeNode);
    }


    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        // 目录节点特殊处理
        if (node.getAllowsChildren() && node.getChildCount() == 0 ) {
            super.setIcon(getDefaultClosedIcon());
        }

        return this;
    }


}
