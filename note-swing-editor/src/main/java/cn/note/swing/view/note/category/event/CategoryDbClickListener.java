package cn.note.swing.view.note.category.event;

import cn.note.swing.core.view.tree.DragJXTree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 双击事件
 */
@FunctionalInterface
public interface CategoryDbClickListener extends MouseListener {


    void onDbClick(DefaultMutableTreeNode clickTreeNode);

    default void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Object sourceComp = e.getSource();
            if (sourceComp instanceof DragJXTree) {
                DragJXTree tree = (DragJXTree) sourceComp;
                TreePath path = tree.getClosestPathForLocation(e.getX(), e.getY());
                DefaultMutableTreeNode clickNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (!clickNode.getAllowsChildren()) {
                    SwingUtilities.invokeLater(() -> onDbClick(clickNode));
                }
            }
        }
    }

    default void mousePressed(MouseEvent e) {

    }

    default void mouseReleased(MouseEvent e) {

    }

    default void mouseEntered(MouseEvent e) {

    }

    default void mouseExited(MouseEvent e) {

    }

}
