package cn.note.swing.core.event.right;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * 右键菜单事件
 * 简化了右键菜单相关操作
 *
 * @author jee
 */
public interface RightPopupListener extends MouseListener, MouseMotionListener {

    /**
     * @return 绑定组件
     */
    JComponent getComponent();

    /**
     * 实现右键菜单
     *
     * @return 右键菜单视图
     */
    JPopupMenu getPopup(MouseEvent e);


    /**
     * 设置右键组件显示
     *
     * @param e 鼠标事件
     */
    default void show(MouseEvent e) {
        if (getPopup(e) != null) {
            getPopup(e).setVisible(true);
            getPopup(e).show(getComponent(), e.getX(), e.getY());
        }
    }

    default void hide(MouseEvent e) {
        if (getPopup(e) != null)
            getPopup(e).setVisible(false);
    }


    /**
     * 显示右键菜单
     */
    default void showPopup(MouseEvent e) {
        show(e);
    }


    /**
     * 隐藏右键菜单
     */
    default void hidePopup(MouseEvent e) {
        hide(e);
    }


    @Override
    default void mouseClicked(MouseEvent e) {

    }

    @Override
    default void mousePressed(MouseEvent e) {

    }

    @Override
    default void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            showPopup(e);
        } else {
            hidePopup(e);
        }

    }

    @Override
    default void mouseEntered(MouseEvent e) {

    }

    @Override
    default void mouseExited(MouseEvent e) {

    }

    @Override
    default void mouseDragged(MouseEvent e) {

    }

    @Override
    default void mouseMoved(MouseEvent e) {

    }
}
