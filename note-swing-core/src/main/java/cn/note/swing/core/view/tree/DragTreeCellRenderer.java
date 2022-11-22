package cn.note.swing.core.view.tree;

import cn.note.swing.core.view.theme.ThemeColor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import java.awt.*;

/**
 * @description: 拖拽的树样式
 * @author: jee
 */
@Slf4j
public class DragTreeCellRenderer extends DefaultTreeCellRenderer {
    private boolean isTargetNode;
    private boolean isTargetNodeLeaf;

    private DragTreeNode dragTreeNode;

    public DragTreeCellRenderer(DragTreeNode dragTreeNode) {
        this.dragTreeNode = dragTreeNode;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof TreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            isTargetNode = value.equals(dragTreeNode.getDropTargetNode());
            isTargetNodeLeaf = isTargetNode && !((TreeNode) value).getAllowsChildren();
        }
        return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isTargetNode && dragTreeNode.getDropTargetNode() != null) {
            g.setColor(ThemeColor.dragColor);
            if (isTargetNodeLeaf) {
                g.fillRect(0, 2, getSize().width, 2);
            } else {
                g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
            }
        }
    }
}
