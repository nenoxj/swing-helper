package cn.note.swing.view.note.category;

import cn.hutool.core.util.ArrayUtil;
import cn.note.service.toolkit.filestore.FileStore;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.wrapper.HtmlBuilder;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.modal.ModalOptButton;
import cn.note.swing.core.view.tree.DragJXTree;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.store.NoteContext;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.IOException;

@Slf4j
public class CategoryDeleteView extends AbstractMigView {
    /* 删除内容提示*/
    private JXLabel deleteContent;

    /* 当前选择节点*/
    private DefaultMutableTreeNode selectNode;
    /* 树节点*/
    private DefaultTreeModel treeModel;
    /* 树容器*/
    private DragJXTree jxTree;

    /* 文件存储*/
    private FileStore fileStore;
    /* 是否目录 */
    private boolean isDir;
    /* note上下文存储*/
    private NoteContext noteContext;

    /*按钮操作*/
    private ModalOptButton modalOptButton;

    public CategoryDeleteView() {
        super(true);
        this.noteContext = NoteContext.getInstance();
        this.jxTree = noteContext.getCategoryView().getTree();
        this.fileStore = noteContext.getNoteFileStore();
        this.selectNode = (DefaultMutableTreeNode) jxTree.getLastSelectedPathComponent();
        this.treeModel = (DefaultTreeModel) jxTree.getModel();
        this.isDir = selectNode.getAllowsChildren();
        super.display();
    }

    /**
     * 定义migLayout布局
     *
     * @return migLayout布局
     */
    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("", "[grow]", "[grow]");
    }

    /**
     * 初始化成员对象
     */
    @Override
    protected void init() {
        modalOptButton = new ModalOptButton();
        createDeleteContent();
    }

    private void createDeleteContent() {
        String nodeName = selectNode.getUserObject().toString();
        boolean isLeaf = selectNode.getAllowsChildren();
        int childCount = selectNode.getChildCount();
        String context = "";
        if (isLeaf) {
            if (childCount > 0) {
                context = HtmlBuilder.html("删除当前目录：{},包含{}个子笔记？", HtmlBuilder.danger(nodeName), HtmlBuilder.danger(childCount));
            } else {
                context = HtmlBuilder.html("删除当前目录：{}？", HtmlBuilder.danger(nodeName));
            }
        } else {
            context = HtmlBuilder.html("删除当前笔记：{}？", HtmlBuilder.danger(nodeName));
        }
        deleteContent = new JXLabel(context);
        deleteContent.setLineWrap(true);
    }

    /**
     * render视图
     */
    @Override
    protected void render() {

        view.add(deleteContent, "grow,gapleft 10,gapright 10,wrap");
        view.add(modalOptButton, "grow");
    }


    @InitAction
    private void confirmAction() {
        modalOptButton.okCall(new ConsumerAction(e -> {
            String[] nodePaths;
            if (isDir) {
                nodePaths = jxTree.getPaths(selectNode);
            } else {
                nodePaths = jxTree.getPaths(selectNode.getParent());
                nodePaths = ArrayUtil.append(nodePaths, selectNode.getUserObject().toString() + NoteConstants.FILE_TYPE);
            }
            try {
                fileStore.delete(nodePaths);
                treeModel.removeNodeFromParent(selectNode);
                // 同时关闭选项卡
                noteContext.getNoteTabView().deleteTab(fileStore.getFile(nodePaths));
                // 获取当前组件所在窗口
                SwingUtilities.getWindowAncestor(this).dispose();
                jxTree.requestFocus();
            } catch (IOException e1) {
                log.error("删除节点失败", e1);
                MessageBuilder.error(jxTree, "删除节点失败!");
            }
        }));
    }


}
