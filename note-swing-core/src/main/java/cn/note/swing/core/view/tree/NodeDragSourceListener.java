package cn.note.swing.core.view.tree;

import java.awt.dnd.*;

/**
 * @description: 拖拽图标样式
 * from Java Swing Hacks
 * @author: jee
 */
public class NodeDragSourceListener implements DragSourceListener {
    @Override
    public void dragDropEnd(DragSourceDropEvent e) {
    }

    @Override
    public void dragEnter(DragSourceDragEvent e) {
        e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
    }

    @Override
    public void dragExit(DragSourceEvent e) {
        e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    @Override
    public void dragOver(DragSourceDragEvent e) {
        /* not needed */
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent e) {
        /* not needed */
    }
}