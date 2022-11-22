package cn.note.swing.view.note;

import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.event.key.KeyActionStatus;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.view.AbstractMigCard;
import cn.note.swing.core.view.panel.LRCard;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.core.view.tree.DragJXTree;
import cn.note.swing.store.NoteContext;
import cn.note.swing.view.note.category.CategoryView;
import cn.note.swing.view.note.category.event.CategoryActionEnum;
import cn.note.swing.view.note.tab.NoteTabView;
import cn.note.swing.view.search.FileContextSearchModalView;
import cn.note.swing.view.search.FileNameSearchModalView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * @description: 主编辑视图
 * @author: jee
 */
@Slf4j
public class NoteEditorView extends AbstractMigCard {
    /**
     * 左侧区域
     */
    @Getter
    private CategoryView categoryView;

    /**
     * 笔记选项卡
     */
    @Getter
    private NoteTabView noteTabView;

    /**
     * 笔记上下文对象
     */
    private NoteContext noteContext;


    private ResourcesView resourcesView;

    public NoteEditorView() {
        this(false);
    }

    public NoteEditorView(boolean card) {
        super(card);
    }

    @Override
    public LRCard getCardView() {
        return new LRCard(this.getClass(), resourcesView, noteTabView);

    }

    @Override
    protected void init() {
        noteContext = NoteContext.getInstance();
        // 建立文件索引信息
        this.noteTabView = noteContext.getNoteTabView();
        this.resourcesView = noteContext.getResourcesView();
        this.categoryView = noteContext.getCategoryView();
    }

    @Override
    protected void render() {
        String CATEGORY_AREA = "categoryArea";
        String NOTE_TAB_AREA = "noteTabView";
        String layout = "(ROW " + "(LEAF name=" + CATEGORY_AREA + " weight=0.2)" + "(LEAF name=" + NOTE_TAB_AREA + " weight=0.8)" + ")";
        MultiSplitLayout multiSplitLayout = new MultiSplitLayout(MultiSplitLayout.parseModel(layout));
        // 拖拽面板
        JXMultiSplitPane splitPane = new JXMultiSplitPane();
        splitPane.setLayout(multiSplitLayout);
//        splitPane.setDividerSize(2);

        splitPane.add(resourcesView, CATEGORY_AREA);
        splitPane.add(noteTabView, NOTE_TAB_AREA);
//        splitPane.setBackground(ThemeColor.panelColor);
        multiSplitLayout.layoutByWeight(resourcesView);
        view.add(splitPane, "w 100%,h 100%,id main");
//        splitPane.dispatchEvent(new FocusEvent(noteTabView, FocusEvent.FOCUS_GAINED, true));
        noteTabView.requestFocus2Editor();
    }


    /* 全局文件搜索*/
    private FileNameSearchModalView fileNameSearchModalView;


    @InitAction("初始化文件搜索面板")
    private void initGlobalFileSearch() {
        KeyActionFactory.registerGlobalKey(ke -> {
            if (ke.getKeyCode() == KeyEvent.VK_R && ke.isShiftDown() && ke.isControlDown()) {
                if (fileNameSearchModalView == null) {
                    fileNameSearchModalView = new FileNameSearchModalView();
                }
                fileNameSearchModalView.getDialog().setVisible(true);
            }
        });
    }


    /* 全局内容搜索*/

    private FileContextSearchModalView fileContextSearchModalView;

    @InitAction("初始化内容搜索面板")
    private void initGlobalContextSearch() {
        KeyActionFactory.registerGlobalKey(ke -> {
            if (ke.getKeyCode() == KeyEvent.VK_H && ke.isControlDown()) {
                if (fileContextSearchModalView == null) {
                    fileContextSearchModalView = new FileContextSearchModalView();
                }
                fileContextSearchModalView.getDialog().setVisible(true);

            }
        });
    }

    @InitAction("双击打开笔记视图")
    private void activeCategory() {
        // 双击打开视图
        categoryView.onDbClick(treeNode -> editNoteTreeNode(treeNode, true));
        DragJXTree jxTree = categoryView.getTree();
        // 回车控制目录展开/收起  文件打开
        KeyActionFactory.bindEnterAction(jxTree, new ConsumerAction(e -> {

            DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) jxTree.getLastSelectedPathComponent();
            if (selectNode != null) {
                if (selectNode.getAllowsChildren()) {

                    TreePath treePath = jxTree.getTreePath(selectNode);
                    if (jxTree.isCollapsed(treePath)) {
                        jxTree.expandPath(treePath);
                    } else {
                        jxTree.collapsePath(treePath);
                    }
                } else {
                    editNoteTreeNode(selectNode, false);
                }
            }
        }), KeyActionStatus.WHEN_FOCUSED);

    }

    @InitAction("ESC失去焦点,编辑面板获得")
    private void lostGainEvent() {
        KeyActionFactory.bindEscAction(categoryView, new ConsumerAction(e -> noteTabView.requestFocus2Editor()));
    }


    @InitAction("定位当前编辑器的分类树位置")
    private void activeCategoryPath() {

        Action traceActiveTreePos = new ConsumerAction(e -> {
            // 查找当前选项卡打开的文件并激活
            String[] activePaths = noteTabView.getActivePaths();
            if (activePaths != null) {
                DragJXTree tree = categoryView.getTree();
                TreePath activePath = tree.getTreePath(activePaths);
                if (activePath != null) {
                    // 激活并展开,并定位
                    tree.setSelectionPath(activePath);
                    tree.expandPath(activePath);
                    tree.scrollPathToVisible(activePath);
                    // 默认让树获取焦点
                    tree.requestFocusInWindow();
                }
            }
        });

        KeyActionFactory.registerKeyAction(categoryView, CategoryActionEnum.ACTIVE_PATH, traceActiveTreePos, KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);
        categoryView.getCategoryToolbarView().getTraceView().addActionListener(traceActiveTreePos);
    }


    /**
     * 打开当前节点编辑器
     *
     * @param treeNode 当前节点
     * @param active   是否激活
     */
    private void editNoteTreeNode(DefaultMutableTreeNode treeNode, boolean active) {
        File treeStoreFile = categoryView.getTreeNodeAtStoreFile(treeNode);
        if (treeStoreFile != null) {
            SwingUtilities.invokeLater(() -> noteTabView.editNote(treeStoreFile));
        }
    }


    public static void main(String[] args) {
        ThemeFlatLaf.install();
        // 设置测试目录
        SystemFileManager.updateSystemDir2Default();
        FrameUtil.launchTime(NoteEditorView.class);
    }
}
