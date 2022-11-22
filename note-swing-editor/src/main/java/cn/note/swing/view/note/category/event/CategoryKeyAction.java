package cn.note.swing.view.note.category.event;

import cn.note.service.toolkit.filestore.FileStore;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyAction;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.tree.DragJXTree;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.store.NoteContext;
import cn.note.swing.view.note.category.CategoryAddView;
import cn.note.swing.view.note.category.CategoryDeleteView;
import cn.note.swing.view.note.category.CategoryRenameView;
import cn.note.swing.view.note.category.CategoryView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @description: 扫描@ActionKey实现注册
 * @author: jee
 */
@Slf4j
public class CategoryKeyAction implements RegisterKeyAction {

    /**
     * 分类主组件 tree
     */
    private DragJXTree tree;


    /* 文件存储目录*/
    private FileStore fileStore;


    /*目录视图*/
    private CategoryView categoryView;

    /**
     * 存储
     */
    private NoteContext noteContext;

    public CategoryKeyAction(NoteContext noteContext) {
        this.fileStore = noteContext.getNoteFileStore();
        this.categoryView = noteContext.getCategoryView();
        this.tree = categoryView.getTree();
    }

    @Override
    public JComponent getComponent() {
        return tree;
    }


    /**
     * 分类菜单删除
     */
    @KeyAction
    public void delete() {
        registerKeyAction(CategoryActionEnum.DELETE, new ConsumerAction((e) -> {
            if (!isSelectRoot()) {
                tree.onSelectNode(() ->
                        WinUtil.showDialogForCustom(tree, "删除", new CategoryDeleteView())
                );
            }
        }
        ));
    }


    /**
     * 分类菜单更改
     */
    @KeyAction
    public void update() {
        registerKeyAction(CategoryActionEnum.UPDATE, new ConsumerAction((e) -> {
            if (!isSelectRoot()) {
                tree.onSelectNode(() ->
                        WinUtil.showDialogForCustom(tree, "重命名", new CategoryRenameView())
                );
            }
        }
        ));
    }

    /**
     * 分类菜单 新建目录
     */
    @KeyAction
    public void createDir() {
        registerKeyAction(CategoryActionEnum.CREATE_DIR, create(CategoryActionEnum.CREATE_DIR, true));
    }

    /**
     * 分类菜单 新建文件
     */
    @KeyAction
    public void createFile() {
        registerKeyAction(CategoryActionEnum.CREATE_FILE, create(CategoryActionEnum.CREATE_FILE, false));
    }


    /**
     * 分类菜单 新建
     *
     * @param categoryActionEnum 动作类型集合
     * @param isDir              是否目录
     * @return 动作
     */
    private Action create(CategoryActionEnum categoryActionEnum, boolean isDir) {
        return new ConsumerAction((e) ->
                tree.onSelectNode(() ->
                        WinUtil.showDialogForCustom(tree, categoryActionEnum.getDescription(), new CategoryAddView(isDir)))
        );
    }


    /**
     * 展开所有
     */
    @KeyAction
    public void expandAll() {
        registerKeyAction(CategoryActionEnum.EXPAND_ALL, new ConsumerAction((e) -> tree.expandAll()));
    }

    /**
     * 折叠所有
     */
    @KeyAction
    public void collapseAll() {
        registerKeyAction(CategoryActionEnum.COLLAPSE_ALL, new ConsumerAction((e) -> tree.collapseAll()));
    }

    /**
     * 删除所有
     */
    @KeyAction
    public void deleteAll() {
        registerKeyAction(CategoryActionEnum.DELETE_ALL, new ConsumerAction((e) -> {
            noteContext.getNoteFileStore().clean();
            DefaultMutableTreeNode nodeData = new DefaultMutableTreeNode(NoteConstants.Category.ROOT_NAME);
            tree.setModel(new DefaultTreeModel(nodeData));
        }
        ));
    }


    /* 重载*/
    @KeyAction
    private void reloadTree() {
        registerKeyAction(CategoryActionEnum.RELOAD, new ConsumerAction((e) -> {
            categoryView.reloadRoot();
        }));
    }


    /**
     * 是否选择根节点
     */
    private boolean isSelectRoot() {
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        return selectNode.isRoot();
    }
}
