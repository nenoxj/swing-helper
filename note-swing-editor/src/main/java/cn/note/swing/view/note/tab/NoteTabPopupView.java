package cn.note.swing.view.note.tab;

import cn.note.swing.core.event.right.RightPopupListener;
import cn.note.swing.core.view.base.MenuFactory;
import cn.note.swing.view.note.tab.event.NoteTabActions;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * 选项卡右键操作视图
 * 关闭事件
 */
public class NoteTabPopupView implements RightPopupListener {

    /* 选项卡视图*/
    private NoteTabView tabView;

    /* 关闭其他 */
    private JMenuItem closeOther;

    /* 关闭左侧*/
    private JMenuItem closeLeft;

    /*关闭右侧*/
    private JMenuItem closeRight;

    /* 右键视图*/
    private JPopupMenu popupMenu;

    public NoteTabPopupView(NoteTabView tabView) {
        this.tabView = tabView;
    }

    /**
     * 选项卡默认菜单项
     * 默认关闭当前 /其他/所有
     *
     * @return 选项卡的右键菜单视图
     */
    @Override
    public JPopupMenu getPopup(MouseEvent e) {

        if (tabView.isRightClicked()) {
            if (popupMenu == null) {
                JMenuItem closeNow = MenuFactory.createKeyStrokeMenuItem(tabView, NoteTabActions.CLOSE_NOW, null);
                closeOther = MenuFactory.createKeyStrokeMenuItem(tabView, NoteTabActions.CLOSE_OTHER, null);
                JMenuItem closeAll = MenuFactory.createKeyStrokeMenuItem(tabView, NoteTabActions.CLOSE_ALL, null);
                closeLeft = MenuFactory.createKeyStrokeMenuItem(tabView, NoteTabActions.CLOSE_LEFT, null);
                closeRight = MenuFactory.createKeyStrokeMenuItem(tabView, NoteTabActions.CLOSE_RIGHT, null);
                popupMenu = MenuFactory.createPopupMenu(closeNow, closeOther, closeAll);
            }

            if (tabView.isOnlyOne()) {
                // 仅有1个tab页时, 关闭其他不可用,移除关闭左侧和关闭右侧
                closeOther.setEnabled(false);
                popupMenu.remove(closeLeft);
                popupMenu.remove(closeRight);
            } else {
                // 左侧只有 关闭右侧,右侧只有关闭左侧
                closeOther.setEnabled(true);
                if (tabView.isOnLeft()) {
                    popupMenu.remove(closeLeft);
                    popupMenu.add(closeRight);
                } else if (tabView.isOnRight()) {
                    popupMenu.remove(closeRight);
                    popupMenu.add(closeLeft);
                } else {
                    popupMenu.add(closeLeft);
                    popupMenu.add(closeRight);
                }
            }
            return popupMenu;
        } else {
            return null;
        }
    }

    @Override
    public JComponent getComponent() {
        return this.tabView;
    }

}
