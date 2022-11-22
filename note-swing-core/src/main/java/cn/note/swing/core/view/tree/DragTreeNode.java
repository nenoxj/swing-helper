package cn.note.swing.core.view.tree;

import javax.swing.tree.TreeNode;

/**
 * 拖拽树节点
 */
public interface DragTreeNode {

    /**
     * @return 拖拽的树节点对象
     */
    TreeNode getDropTargetNode();
}
