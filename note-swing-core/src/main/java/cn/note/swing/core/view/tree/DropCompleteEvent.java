package cn.note.swing.core.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * 拖拽完成事件
 */
@FunctionalInterface
public interface DropCompleteEvent {
    /**
     * @param draggingNode 拖拽对象
     * @param targetNode   目标对象
     */
    void accept(MutableTreeNode draggingNode, DefaultMutableTreeNode targetNode);
}