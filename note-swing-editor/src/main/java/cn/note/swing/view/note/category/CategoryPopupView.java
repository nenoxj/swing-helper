package cn.note.swing.view.note.category;

import cn.note.swing.core.event.right.RightPopupListener;
import cn.note.swing.core.view.base.MenuFactory;
import cn.note.swing.core.view.tree.DragJXTree;
import cn.note.swing.view.note.category.event.CategoryActionEnum;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

/**
 * 分类菜单右键视图
 */
public class CategoryPopupView implements RightPopupListener {

    /*分类树菜单*/
    private DragJXTree categoryTree;

    /*普通节点右键视图*/
    private JPopupMenu popupMenu;

    /* 根节点右键视图*/
    private JPopupMenu rootPopupMenu;


    public CategoryPopupView(DragJXTree categoryTree) {
        this.categoryTree = categoryTree;
    }


    @Override
    public JComponent getComponent() {
        return categoryTree;
    }

    /**
     * @return 在showPopup 中使用自定义校验
     */
    @Override
    public JPopupMenu getPopup(MouseEvent e) {

        if (popupMenu == null) {
            JMenuItem delete = MenuFactory.createKeyStrokeMenuItem(categoryTree, CategoryActionEnum.DELETE, null);
            JMenuItem update = MenuFactory.createKeyStrokeMenuItem(categoryTree, CategoryActionEnum.UPDATE, null);
            JMenuItem createDir = MenuFactory.createKeyStrokeMenuItem(categoryTree, CategoryActionEnum.CREATE_DIR, null);
            JMenuItem createFile = MenuFactory.createKeyStrokeMenuItem(categoryTree, CategoryActionEnum.CREATE_FILE, null);
            popupMenu = MenuFactory.createPopupMenu(delete, update, createDir, createFile);
        }

        if (rootPopupMenu == null) {
            JMenuItem createDir = MenuFactory.createKeyStrokeMenuItem(categoryTree, CategoryActionEnum.CREATE_DIR, null);
            JMenuItem createFile = MenuFactory.createKeyStrokeMenuItem(categoryTree, CategoryActionEnum.CREATE_FILE, null);
            JMenuItem reload = MenuFactory.createKeyStrokeMenuItem(categoryTree, CategoryActionEnum.RELOAD, null);
            rootPopupMenu = MenuFactory.createPopupMenu(createDir, createFile, reload);
        }

        // 根节点和其他节点右键弹窗进行区分
        TreePath treePath = categoryTree.getClosestPathForLocation(e.getX(), e.getY());
        if (treePath != null) {
            categoryTree.setSelectionPath(treePath);
            DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) categoryTree.getLastSelectedPathComponent();
            if (selectNode.isRoot()) {
                return rootPopupMenu;
            } else {
                return popupMenu;
            }
        } else {
            return rootPopupMenu;
        }
    }
}
