package cn.note.swing.view.note.category;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.core.view.base.MessageBuilder;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.core.view.tree.DragJXTree;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.view.note.category.event.CategoryDbClickListener;
import cn.note.swing.view.note.category.style.CategoryTreeRendererUI;
import cn.note.swing.view.note.category.style.CategoryViewUI;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;

/**
 * @description: jTree测试
 */
@Slf4j
public class CategoryView extends AbstractMigView {

    @Getter
    private DragJXTree tree;

    /**
     * 配置存储
     */
    private RelativeFileStore fileStore;

    /**
     * tree容器
     */
    private JScrollPane treeContainer;

    /**
     * 渲染
     */
    private CategoryTreeRendererUI treeRenderer;


    /* 工具栏视图*/
    @Getter
    private CategoryToolbarView categoryToolbarView;


    public CategoryView(RelativeFileStore fileStore) {
        this(fileStore, null);
    }

    public CategoryView(RelativeFileStore fileStore, DragJXTree dragJXTree) {
        super(true);
        this.fileStore = fileStore;
        if (dragJXTree == null) {
            dragJXTree = new DragJXTree();
            DefaultMutableTreeNode nodeData = new DefaultMutableTreeNode(NoteConstants.Category.ROOT_NAME);
            dragJXTree.setModel(new DefaultTreeModel(nodeData));
        }
        this.tree = dragJXTree;
        super.display();
    }

    /**
     * 定义migLayout布局
     *
     * @return migLayout布局
     */
    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("insets 0,gapy 2,wrap 1");
    }

    /**
     * 初始化成员对象
     */
    @Override
    protected void init() {
        createTreeStyleAndAction();
        categoryToolbarView = new CategoryToolbarView(this);
    }


    /**
     * 创建树样式和动作
     */
    private void createTreeStyleAndAction() {
        CategoryViewUI.addThemeStyle(tree);
        treeRenderer = new CategoryTreeRendererUI(tree);
        treeRenderer.setLeafIcon(SvgIconFactory.icon(SvgIconFactory.Editor.note, ThemeColor.themeColor));
        tree.setCellRenderer(treeRenderer);
        tree.addMouseListener(new CategoryPopupView(tree));
    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        treeContainer = new JScrollPane(tree);
        FlatWrapper.decorativeScrollPane(treeContainer, ThemeColor.fontColor, ThemeColor.grayColor);
        treeContainer.setBorder(BorderFactory.createEmptyBorder());
        int tabHeight = UIManager.getInt("TabbedPane.tabHeight");
        int tabSelectionHeight = UIManager.getInt("TabbedPane.tabSelectionHeight");
        int selectedLabelShift = UIManager.getInt("TabbedPane.selectedLabelShift");

        int realTabHeight = tabHeight - tabSelectionHeight - selectedLabelShift;
        view.add(categoryToolbarView, "w 50:100%,h " + realTabHeight + "!");
        view.add(treeContainer, "w 50:100%:,h 100%");
    }


    /**
     * 拖拽树事件
     * <p>
     * 拖拽为目录的话, 有点复杂, 需要递归文件夹反向生成tree
     * 暂时没有很好算法
     */
    @InitAction
    private void treeDragEvent() {
        tree.onDropCompleteEvent((draggingNode, targetNode) -> {
            // 是否为根目录
            boolean isRoot = targetNode.isRoot();
            // 目标是否为目录
            boolean isTargetDir = targetNode.getAllowsChildren();
            // 拖拽是否为目录
            boolean isDragDir = draggingNode.getAllowsChildren();
            // 描述
            String targetDesc = "目标目录";

            String[] dragPaths = tree.getPaths(draggingNode);
            String[] targetPaths = tree.getPaths(targetNode);
            // 如果拖拽为文件
            if (!isDragDir) {
                dragPaths[dragPaths.length - 1] = dragPaths[dragPaths.length - 1] + NoteConstants.FILE_TYPE;
            }
            // 如果目标是文件
            if (!isTargetDir) {
                // 获取父目录
                targetPaths = ArrayUtil.remove(targetPaths, targetPaths.length - 1);
                targetDesc = "目标文件";
            }
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

            // 比较draggingNode 和targetNode 是否在同一路径
            String existsName = dragPaths[dragPaths.length - 1];
            String[] existsPaths = ArrayUtil.append(targetPaths, existsName);
            String existsPath = ArrayUtil.toString(existsPaths);
            String dragPath = ArrayUtil.toString(dragPaths);
            boolean isSameParent = StrUtil.equals(dragPath, existsPath);
            log.debug("是否同一路径:{}=={}", dragPath, existsPath);
            // 拖拽路径==目标路径
            if (!isSameParent && fileStore.exists(existsPaths)) {
                String body = StrUtil.format("{}:{}已存在,是否覆盖 ?", targetDesc, existsName);
                int result = JOptionPane.showConfirmDialog(null, body, "移动文件", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    model.removeNodeFromParent(draggingNode);
                    try {
                        fileStore.move(fileStore.getFile(dragPaths), fileStore.getFile(targetPaths));
                        if (isDragDir) {
                            // 查找子节点
                            DefaultMutableTreeNode overTargetNode = findChildNodeByName(targetNode, existsName);
                            int overIndex = targetNode.getIndex(overTargetNode);
                            model.removeNodeFromParent(overTargetNode);
                            overTargetNode.removeAllChildren();
                            syncDir2TreeNodes(fileStore.getFile(existsPaths), overTargetNode);
                            model.insertNodeInto(overTargetNode, targetNode, overIndex);
                            tree.expandPath(tree.getTreePath(overTargetNode));
                        }
                    } catch (IOException e) {
                        log.error("覆盖失败", e);
                        MessageBuilder.error(tree, "移动文件失败!");
                    }
                }
            } else {
                try {
                    // 开始拖拽路径 和结束路径在同一目录
                    if (!isSameParent) {
                        log.info("移动路径:{} to: {}", ArrayUtil.toString(dragPaths), ArrayUtil.toString(targetPaths));
                        fileStore.move(fileStore.getFile(dragPaths), fileStore.getFile(targetPaths));

                    }
                    // 删除旧的node节点,插入移动新的位置
                    model.removeNodeFromParent(draggingNode);
                    TreeNode parent;
                    if (isRoot) {
                        parent = targetNode;
                    } else {
                        parent = targetNode.getParent();
                    }
                    // 拖拽进目录
                    if (parent instanceof MutableTreeNode && isTargetDir) {
                        model.insertNodeInto(draggingNode, targetNode, targetNode.getChildCount());
                    } else {
                        // 拖拽进文件
                        model.insertNodeInto(draggingNode, (MutableTreeNode) parent, parent.getIndex(targetNode));
                    }

                } catch (IOException e) {
                    log.error("移动失败", e);
                    MessageBuilder.error(tree, "移动文件失败!");
                }
            }

        });

    }

    /**
     * 先目录后文件
     *
     * @param dir        目录
     * @param targetNode 目标target
     */
    public void syncDir2TreeNodes(File dir, DefaultMutableTreeNode targetNode) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        File[] dirs = dir.listFiles((FileFilter) FileFilterUtils.directoryFileFilter());
        Collection<File> files = FileUtils.listFiles(dir, FileFilterUtils.suffixFileFilter(NoteConstants.FILE_TYPE),
                FileFilterUtils.falseFileFilter());
        for (File children : dirs) {
            DefaultMutableTreeNode childrenNode = new DefaultMutableTreeNode(children.getName());
            model.insertNodeInto(childrenNode, targetNode, targetNode.getChildCount());
            targetNode.add(childrenNode);
            syncDir2TreeNodes(children, childrenNode);
        }
        for (File children : files) {
            DefaultMutableTreeNode childrenNode = new DefaultMutableTreeNode(children.getName().replace(NoteConstants.FILE_TYPE, ""));
            childrenNode.setAllowsChildren(false);
            targetNode.add(childrenNode);
        }
    }


    /**
     * 更新根节点
     */
    public void reloadRoot() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        File rootDir = fileStore.getFile(tree.getPaths(root));
        if (rootDir.exists()) {
            root.removeAllChildren();
            syncDir2TreeNodes(rootDir, root);
            model.reload();
        }


    }


    /**
     * 根据名称查找子节点元素
     *
     * @param pNode     父节点
     * @param childName 子节点名称
     * @return 子节点
     */
    public DefaultMutableTreeNode findChildNodeByName(TreeNode pNode, String childName) {
        for (int i = 0; i < pNode.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) pNode.getChildAt(i);
            if (StrUtil.equals(childNode.getUserObject().toString(), childName)) {
                return childNode;
            }
        }
        return null;

    }


    @InitAction
    private void registerToolBar() {
        categoryToolbarView.getCollapseAll().addActionListener((e) -> getTree().collapseAll());
    }


    /**
     * 双击目录事件
     *
     * @param dbClickListener 双击事件
     */
    public void onDbClick(CategoryDbClickListener dbClickListener) {
        tree.addMouseListener(dbClickListener);
    }


    /**
     * @param treeNode tree节点
     * @return tree节点对应的真实文件
     */
    public File getTreeNodeAtStoreFile(DefaultMutableTreeNode treeNode) {
        String[] paths = tree.getPaths(treeNode);
        if (!treeNode.getAllowsChildren()) {
            int index = paths.length - 1;
            paths[index] = paths[index].concat(NoteConstants.FILE_TYPE);
        }
        return fileStore.getFile(paths);
    }


    /**
     * 未移除根节点的相对路径
     * paths = ArrayUtil.remove(paths, 0);
     *
     * @param treeNode 树节点
     * @return 获取节点在tree的全路径
     */
    public String getRelativePath(DefaultMutableTreeNode treeNode) {
        String[] paths = tree.getPaths(treeNode);
        return ArrayUtil.join(paths, "/") + NoteConstants.FILE_TYPE;
    }


    /**
     * 令tree 获取焦点
     */
    public void requestFocus2Tree() {
        tree.requestFocusInWindow();
    }


    public static void main(String[] args) {
        ThemeFlatLaf.install();
        SystemFileManager.updateSystemDir2Default();
        FrameUtil.launchTest(CategoryView.class);
    }
}
