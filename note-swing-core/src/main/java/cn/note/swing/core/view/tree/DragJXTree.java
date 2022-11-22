package cn.note.swing.core.view.tree;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXTree;

import javax.swing.tree.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.util.List;
import java.util.*;

/**
 * @description: from Java Swing Hacks
 * 可拖拽的树
 * <p>
 * @author: jee
 */
@Slf4j
public class DragJXTree extends JXTree implements DragTreeNode {

    private transient DropTarget treeDropTarget;
    protected transient TreeNode dropTargetNode;
    protected transient TreeNode draggedNode;


    private transient TreePath dragStartPath;


    private boolean isAllowParent;

    public DragJXTree() {
        this(true);
    }

    public DragJXTree(boolean isAllowParent) {
        this.isAllowParent = isAllowParent;
    }

    @Override
    public TreeNode getDropTargetNode() {
        return dropTargetNode;
    }

    public TreePath getDragStartPath() {
        return dragStartPath;
    }

    @Override
    public void updateUI() {
        super.setCellRenderer(null);
        super.updateUI();
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(
                this, DnDConstants.ACTION_MOVE, new NodeDragGestureListener());
        if (Objects.isNull(treeDropTarget)) {
            treeDropTarget = new DropTarget(this, new NodeDropTargetListener());
        }
    }


    private class NodeDragGestureListener implements DragGestureListener {
        @Override
        public void dragGestureRecognized(DragGestureEvent e) {
            Point pt = e.getDragOrigin();
//            TreePath path = getPathForLocation(pt.x, pt.y);
            TreePath path = getClosestPathForLocation(pt.x, pt.y);
            if (Objects.isNull(path) || Objects.isNull(path.getParentPath())) {
                return;
            }
            log.debug("start: {}", path.toString());
            dragStartPath = path;
            draggedNode = (TreeNode) path.getLastPathComponent();
            Transferable trans = new TreeNodeTransferable(draggedNode);
            DragSource.getDefaultDragSource().startDrag(e, Cursor.getDefaultCursor(), trans, new NodeDragSourceListener());
        }
    }

    private class NodeDropTargetListener implements DropTargetListener {
        @Override
        public void dropActionChanged(DropTargetDragEvent e) {
            /* not needed */
        }

        @Override
        public void dragEnter(DropTargetDragEvent e) {
            /* not needed */
        }

        @Override
        public void dragExit(DropTargetEvent e) {
            /* not needed */
        }

        @Override
        public void dragOver(DropTargetDragEvent e) {
            DataFlavor[] f = e.getCurrentDataFlavors();
            boolean isSupported = TreeNodeTransferable.NAME.equals(f[0].getHumanPresentableName());
            if (!isSupported) {
                // This DataFlavor is not supported(e.g. files from the desktop)
                rejectDrag(e);
                return;
            }

            // figure out which cell it's over, no drag to self
            Point pt = e.getLocation();
//            TreePath path = getPathForLocation(pt.x, pt.y);
            // 最近的tree path 不会出现无法拖拽
            TreePath path = getClosestPathForLocation(pt.x, pt.y);

            if (Objects.isNull(path)) {
                rejectDrag(e);
                return;
            }
            Object draggingNode = Optional.ofNullable(getSelectionPath())
                    .map(TreePath::getLastPathComponent).orElse(null);

            // 不允许拖拽包含子节点的节点
            if (!isAllowParent) {
                if (draggingNode instanceof TreeNode) {
                    TreeNode dragTreeNode = (TreeNode) draggingNode;
                    if (dragTreeNode.getAllowsChildren()) {
                        rejectDrag(e);
                        return;
                    }
                }
            }
            DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            TreeNode parent = targetNode.getParent();
            if (parent instanceof DefaultMutableTreeNode && draggingNode instanceof TreeNode) {
                DefaultMutableTreeNode ancestor = (DefaultMutableTreeNode) parent;
                if (Arrays.asList(ancestor.getPath()).contains(draggingNode)) {
                    // Trying to drop a parent node to a child node
                    rejectDrag(e);
                    return;
                }
            }
            dropTargetNode = targetNode; // (TreeNode) path.getLastPathComponent();
            e.acceptDrag(e.getDropAction());
            repaint();
        }

        @Override
        public void drop(DropTargetDropEvent e) {
            Object draggingObject = Optional.ofNullable(getSelectionPath())
                    .map(TreePath::getLastPathComponent).orElse(null);
            Point pt = e.getLocation();
//            TreePath path = getPathForLocation(pt.x, pt.y);
            TreePath path = getClosestPathForLocation(pt.x, pt.y);
            if (Objects.isNull(path) || !(draggingObject instanceof MutableTreeNode)) {
                e.dropComplete(false);
                return;
            }
            log.debug("drop path: {}", path.toString());
            MutableTreeNode draggingNode = (MutableTreeNode) draggingObject;
            DefaultMutableTreeNode targetNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (targetNode.equals(draggingNode)) {
                // Cannot move the node to the node itself
                e.dropComplete(false);
                // 刷新当前node
                dropTargetNode = null;
                DefaultTreeModel model = (DefaultTreeModel) getModel();
                model.reload(draggingNode);
                return;
            }
            e.acceptDrop(DnDConstants.ACTION_MOVE);
            // 拖拽完成事件
            dropComplete(draggingNode, targetNode);
            e.dropComplete(true);
            dropTargetNode = null;
            draggedNode = null;
            repaint();
        }

        private void rejectDrag(DropTargetDragEvent e) {
            e.rejectDrag();
            dropTargetNode = null; // dropTargetNode as null,
            repaint();             // and repaint the JTree(turn off the Rectangle2D and Line2D)
        }
    }


    /**
     * 拖拽完成事件
     *
     * @param draggingNode 拖拽对象
     * @param targetNode   目标对象
     */
    protected void dropComplete(MutableTreeNode draggingNode, DefaultMutableTreeNode targetNode) {

        if (dropCompleteEvent != null) {
            dropCompleteEvent.accept(draggingNode, targetNode);
        } else {
            log.warn("未指定拖拽完成事件");
        }
    }


    private transient DropCompleteEvent dropCompleteEvent;

    /**
     * 默认注入
     * 正在拖拽对象draggingNode 和目标对象targetNode
     *
     * @param dropCompleteEvent tree拖拽完成事件
     */
    public void onDropCompleteEvent(DropCompleteEvent dropCompleteEvent) {
        this.dropCompleteEvent = dropCompleteEvent;
    }


    /**
     * 默认注入
     * 选择节点对象 selectNode
     * 和 树模型对象 treeModel
     *
     * @param selectNodeEvent tree选择事件
     */
    public void onSelectNode(SelectNodeEvent selectNodeEvent) {
        DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
        DefaultTreeModel treeModel = (DefaultTreeModel) this.getModel();
        selectNodeEvent.accept(selectNode, treeModel);
    }


    /**
     * @param runnable 直接回调不关心参数
     */
    public void onSelectNode(Runnable runnable) {
        runnable.run();
    }

    /**
     * 获取节点路径
     *
     * @param treeNode tree节点
     * @return 返回节点路径
     */
    public TreePath getTreePath(TreeNode treeNode) {
        List<Object> nodes = new ArrayList<Object>();
        if (treeNode != null) {
            nodes.add(treeNode);
            treeNode = treeNode.getParent();
            while (treeNode != null) {
                nodes.add(0, treeNode);
                treeNode = treeNode.getParent();
            }
        }
        return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
    }


    /**
     * 搜索时展开树
     *
     * @param path 路径
     * @param q    内容
     */
    public void searchTree(TreePath path, String q) {
        Object o = path.getLastPathComponent();
        if (o instanceof TreeNode) {
            TreeNode node = (TreeNode) o;
            if (Objects.toString(node).startsWith(q)) {
                this.expandPath(path.getParentPath());
            }
            if (!node.isLeaf()) {
                // Java 9: Collections.list(node.children())
                Collections.list((Enumeration<?>) node.children())
                        .forEach(n -> searchTree(path.pathByAddingChild(n), q));
            }
        }
    }

    /**
     * 获取string类型的路径
     *
     * @param treeNode 节点对象
     * @return 路径集合
     */
    public String[] getPaths(TreeNode treeNode) {
        TreePath treePath = getTreePath(treeNode);
        // 存放的是DefaultMutableTreeNode 对象
        Object[] originPaths = treePath.getPath();
        int length = originPaths.length;
        String[] newPaths = new String[length];
        for (int i = 0; i < length; i++) {
            newPaths[i] = originPaths[i].toString();
        }
        //        return Arrays.stream(originPaths).toArray(String[]::new);
        return newPaths;
    }


    /**
     * 以广度优先查找
     *
     * @param paths 查找路径集合
     * @return 查找路径path
     */
    public TreePath getTreePath(String[] paths) {
        String findPath = ArrayUtil.toString(paths);

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getModel().getRoot();
        Enumeration enumeration = rootNode.breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            // 获取当前节点
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) enumeration
                    .nextElement();
            int nodeLength=treeNode.getPath().length;
            if (!treeNode.getAllowsChildren()&&nodeLength==paths.length) {
                TreePath treePath = getTreePath(treeNode);
                String path = ArrayUtil.toString(treePath.getPath());
                if (path.equals(findPath)) {
                    return treePath;
                }
            }
        }
        return null;
    }
}



