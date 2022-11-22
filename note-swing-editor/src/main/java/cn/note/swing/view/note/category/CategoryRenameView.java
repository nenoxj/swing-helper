package cn.note.swing.view.note.category;

import cn.hutool.core.util.ArrayUtil;
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
import cn.note.swing.view.note.tab.NoteTabView;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

@Slf4j
public class CategoryRenameView extends AbstractMigView {
    /* 重命名input */
    private InputFormItem renameFormItem;
    /* 当前选择节点*/
    private DefaultMutableTreeNode selectNode;
    /* 选择名称*/
    private String selectTitle;
    /* 分类树模型*/
    private DefaultTreeModel categoryTreeModel;
    /* 分类树*/
    private DragJXTree categoryTree;
    /* 文件存储*/
    private FileStore fileStore;
    /* 是否目录 */
    private boolean isDir;

    /* 选项卡视图*/
    private NoteTabView noteTabView;
    /*按钮操作*/
    private ModalOptButton modalOptButton;


    public CategoryRenameView() {
        super(true);
        NoteContext noteContext = NoteContext.getInstance();
        this.categoryTree = noteContext.getCategoryView().getTree();
        this.fileStore = noteContext.getNoteFileStore();
        this.selectNode = (DefaultMutableTreeNode) categoryTree.getLastSelectedPathComponent();
        this.categoryTreeModel = (DefaultTreeModel) categoryTree.getModel();
        this.selectTitle = selectNode.getUserObject().toString();
        this.isDir = selectNode.getAllowsChildren();
        this.noteTabView = noteContext.getNoteTabView();
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
        renameFormItem = new InputFormItem("名称");
        renameFormItem.setFieldValue(this.selectTitle);
        renameFormItem.validEmpty();
        view.add(renameFormItem, "grow,wrap");
        view.add(modalOptButton, "span 2,grow");
    }


    /**
     * 确认
     */
    @InitAction
    private void confirmAction() {

        ConsumerAction renameConfirmAction = new ConsumerAction(e -> {
            if (!renameFormItem.valid()) {
                SwingUtilities.invokeLater(() -> {
                    String newName = renameFormItem.getFieldValue();
                    String[] nodePaths;
                    try {
                        if (selectNode.getAllowsChildren()) {
                            nodePaths = categoryTree.getPaths(selectNode);
                        } else {
                            nodePaths = categoryTree.getPaths(selectNode.getParent());
                            nodePaths = ArrayUtil.append(nodePaths, selectTitle + NoteConstants.FILE_TYPE);
                        }
                        File targetFile = fileStore.getFile(nodePaths);
                        // 重命名文件
                        File renameFile = fileStore.rename(targetFile, newName);
                        // 重命名分类节点
                        selectNode.setUserObject(newName);
                        categoryTreeModel.reload(selectNode);

                        // 重命名选项卡
                        noteTabView.renameTab(targetFile, renameFile);
                        // 获取当前组件所在窗口
                        SwingUtilities.getWindowAncestor(this).dispose();
                    } catch (Exception e1) {
                        log.error("重命名节点失败:", e1);
                        MessageBuilder.error(categoryTree, "重命名节点失败!");
                    }


                });
            }
        });


        modalOptButton.okCall(renameConfirmAction);
        renameFormItem.addEnterEvent(renameConfirmAction);
    }

    /**
     * 重命名
     */
    @InitAction
    private void renameAction() {
        // 自定义重命名校验
        renameFormItem.addValidateForm(() -> {
            String[] parentPaths = categoryTree.getPaths(selectNode.getParent());
            String newFileName = renameFormItem.getFieldValue();
            if (!isDir) {
                newFileName += NoteConstants.FILE_TYPE;
            }
            parentPaths = ArrayUtil.append(parentPaths, newFileName);
            if (fileStore.exists(parentPaths)) {
                return ValidateStatus.fail("重命名失败,{}已存在!", isDir ? "目录" : "文件");
            }
            return ValidateStatus.ok();
        });
    }

}
