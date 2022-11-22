package cn.note.swing.core.view.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * tree 选择节点事件
 *
 * @author jee
 * @since 0.0.1
 */
public interface SelectNodeEvent {

    /**
     * @param selectNode 选择节点
     * @param treeModel  树节点模型
     */
    void accept(DefaultMutableTreeNode selectNode, DefaultTreeModel treeModel);

}
