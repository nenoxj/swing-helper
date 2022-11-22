package cn.note.swing.view.note.category;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import cn.note.service.toolkit.filestore.FileStore;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.form.InputFormItem;
import cn.note.swing.core.view.form.ValidateStatus;
import cn.note.swing.core.view.modal.ModalOptButton;
import cn.note.swing.core.view.tree.DragJXTree;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.store.NoteContext;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.IOException;

/**
 * 添加视图
 */
@Slf4j
public class CategoryAddView extends AbstractMigView {
    /*添加文件输入狂*/
    private InputFormItem addFormItem;
    /*选择节点*/
    private DefaultMutableTreeNode selectNode;
    /*树模型*/
    private DefaultTreeModel treeModel;
    /*树结构*/
    private DragJXTree jxTree;
    /*是否目录*/
    private boolean isDir;
    /* 是否创建目录*/
    private boolean isCreateDir;
    /*文件存储*/
    private FileStore fileStore;
    /*设置存储*/
    private NoteContext noteContext;
    /*按钮操作*/
    private ModalOptButton modalOptButton;


    public CategoryAddView(boolean isCreateDir) {
        super(true);
        this.noteContext = NoteContext.getInstance();
        this.jxTree = noteContext.getCategoryView().getTree();
        this.fileStore = noteContext.getNoteFileStore();
        this.selectNode = (DefaultMutableTreeNode) jxTree.getLastSelectedPathComponent();
        this.treeModel = (DefaultTreeModel) jxTree.getModel();
        this.isDir = selectNode.getAllowsChildren();
        this.isCreateDir = isCreateDir;
        super.display();
    }

    /**
     * 定义migLayout布局
     *
     * @return migLayout布局
     */
    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("w 300::", "[grow]", "[grow]");
    }

    /**
     * 初始化成员对象
     */
    @Override
    protected void init() {
        modalOptButton = new ModalOptButton();
    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        addFormItem = new InputFormItem("名称");
        addFormItem.validEmpty();

        view.add(addFormItem, "grow,wrap");
        view.add(modalOptButton, "span 2,grow");
    }


    /**
     * 确认操作
     * 确定按钮和输入框的回车事件
     */
    @InitAction
    private void confirmAction() {
        ConsumerAction confirmAction = new ConsumerAction(e -> {
            if (!addFormItem.valid()) {
                SwingUtilities.invokeLater(() -> createCategoryAndOpenTab());
            }
        });

        modalOptButton.okCall(confirmAction);
        addFormItem.addEnterEvent(confirmAction);
    }

    /**
     * 重命名操作
     */
    @InitAction
    private void renameAction() {

        // 自定义文件夹校验
        addFormItem.addValidateForm(() -> {
            String[] selectPaths = jxTree.getPaths(selectNode);
            String newFileName = addFormItem.getFieldValue();
            if (!isCreateDir) {
                newFileName += NoteConstants.FILE_TYPE;
            }
            selectPaths = ArrayUtil.append(selectPaths, newFileName);
            if (fileStore.exists(selectPaths)) {
                return ValidateStatus.fail("{}已存在!", isCreateDir ? "目录" : "文件");
            }
            return ValidateStatus.ok();
        });
    }


    /**
     * 创建分类并且打开编辑
     */
    private void createCategoryAndOpenTab() {
        try {
            String newName = addFormItem.getFieldValue();
            createFileStoreAndOpen(newName);
            addCategoryView(newName);
            // 获取当前组件所在窗口
            SwingUtilities.getWindowAncestor(this).dispose();
        } catch (IOException e) {
            log.error("添加节点失败:", e);
            MessageBuilder.error(jxTree, "添加节点失败!");
        }

    }


    /**
     * 创建文件存储,并在选项卡打开
     */
    private void createFileStoreAndOpen(String newName) throws IOException {
        String[] parentPaths = jxTree.getPaths(selectNode);
        if (!selectNode.getAllowsChildren()) {
            parentPaths = jxTree.getPaths(selectNode.getParent());
        }

        if (isCreateDir) {
            // 创建目录
            parentPaths = ArrayUtil.append(parentPaths, newName);
            fileStore.createDir(parentPaths);
            log.info("create dir path:{}", JSONUtil.toJsonStr(parentPaths));
        } else {
            // 创建文件
            parentPaths = ArrayUtil.append(parentPaths, newName + NoteConstants.FILE_TYPE);
            File createFile = fileStore.createFile(parentPaths);
            log.info("create file path:{}", JSONUtil.toJsonStr(parentPaths));

            // 打开选项卡
            SwingUtilities.invokeLater(() -> {
                noteContext.getNoteTabView().editNote(createFile);
            });
        }
    }


    /**
     * 添加分类视图
     *
     * @param newName 添加名称
     */
    private void addCategoryView(String newName) {
        DefaultMutableTreeNode pNode;
        int addIndex;
        // 如果为根节点
        if (selectNode.isRoot()) {
            pNode = selectNode;
            addIndex = pNode.getChildCount();
        } else {
            pNode = (DefaultMutableTreeNode) selectNode.getParent();
            addIndex = pNode.getIndex(selectNode);
        }

        // 创建节点
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newName);
        newNode.setAllowsChildren(isCreateDir);
        // 根节点添加
        if (selectNode.isRoot()) {
            treeModel.insertNodeInto(newNode, selectNode, selectNode.getChildCount());
            jxTree.expandPath(jxTree.getTreePath(selectNode));
            // 普通目录节点添加
        } else if (selectNode.getAllowsChildren()) {
            // 添加至选择目录的某尾
            treeModel.insertNodeInto(newNode, selectNode, selectNode.getChildCount());
            jxTree.expandPath(jxTree.getTreePath(selectNode));
        } else {
            //  添加至选择文件的next
            treeModel.insertNodeInto(newNode, pNode, addIndex + 1);
        }

    }
}
