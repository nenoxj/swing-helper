package cn.note.swing.view.note.tab;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.StrUtil;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.TabbedPaneFactory;
import cn.note.swing.core.view.loading.SpinLoading;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.view.note.editor.RichFileEditorView;
import cn.note.swing.view.note.tab.bean.NoteTabBean;
import com.formdev.flatlaf.FlatClientProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 笔记面板
 */
@Slf4j
public class NoteTabView extends AbstractMigView {

    /* 选项卡*/
    @Getter
    private JTabbedPane openTabs;

    /* 文件存储*/
    private RelativeFileStore noteFileStore;


    /* 序列化对象*/
    private NoteTabBean noteTabBean;
    /**
     * 选项卡信息
     */
    private List<String> tabFiles;

    /* 激活选项卡*/
    @Getter
    private Integer activeIndex;

    /* 右键点击下标*/
    private Integer rightClickIndex;


    /**
     * 获取存储对象
     */
    public NoteTabBean getNoteTabBean() {
        noteTabBean.setActiveIndex(activeIndex);
        return noteTabBean;
    }


    public NoteTabView(RelativeFileStore noteFileStore) {
        this(noteFileStore, new NoteTabBean());
    }

    public NoteTabView(RelativeFileStore noteFileStore, NoteTabBean noteTabBean) {
        super(true);
        this.noteFileStore = noteFileStore;
        this.noteTabBean = noteTabBean;
        this.tabFiles = noteTabBean.getTabFiles();
        this.activeIndex = noteTabBean.getActiveIndex();
        super.display();

    }

    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("wrap 1,insets 0", "[grow]", "[grow]");
    }

    @Override
    protected void init() {
        openTabs = TabbedPaneFactory.createBaseTab();
        openTabs.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_CLOSE_CALLBACK, ((IntConsumer) this::closeTabIndex));
    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        // 加载序列化文件
        loadSerializeFile();
        view.add(openTabs, "grow,h 100%");
    }


    /**
     * 编辑笔记
     *
     * @param noteFile 文件
     */
    public void editNote(File noteFile) {
        String name = noteFile.getName();
        String filePath = noteFile.getAbsolutePath();
        if (tabFiles.contains(filePath)) {
            log.debug("文件已存在,激活选项卡面板!");
            activeIndex = tabFiles.indexOf(filePath);
            openTabs.setSelectedIndex(activeIndex);
            this.requestFocus2Editor();
        } else {
            // 如果tab名称已存在,但是属于不同文件
            createTab(noteFile, null);
            activeIndex = openTabs.getTabCount() - 1;
            tabFiles.add(filePath);
            openTabs.setSelectedIndex(activeIndex);
        }

    }


    /**
     * 尝试获得焦点
     * requestFocusInWindow 无法获取焦点
     */
    public void requestFocus2Editor() {
        if (activeIndex != null && activeIndex >= 0) {
            RichFileEditorView fileEditorView = getEditorView(activeIndex);
            if (fileEditorView == null) {
                log.error("error:activeIndex :{} 错误!请检查", activeIndex);
            } else {
                SwingUtilities.invokeLater(fileEditorView::focus2Editor);
            }

        }
    }


    /**
     * 重命名选项卡
     *
     * @param targetFile 旧文件
     * @param newFile    新文件
     */
    public void renameTab(File targetFile, File newFile) {

        if (targetFile.isFile()) {
            renameTabFile(targetFile.getAbsolutePath(), newFile.getAbsolutePath());

        } else {

            // 查找当前目录已经 打开的文件,更新目录
            List<String> hasOpenTabs = tabFiles.stream().filter((tabFile) -> tabFile.contains(targetFile.getAbsolutePath()))
                    .collect(Collectors.toList());
            hasOpenTabs.forEach((targetFilePath) -> {
                // 替换目录下的文件
                String newFilePath = targetFilePath.replace(targetFile.getAbsolutePath(), newFile.getAbsolutePath());
                renameTabFile(targetFilePath, newFilePath);
            });
        }
    }


    /**
     * 删除选项卡
     */
    public void deleteTab(File targetFile) {

        String targetFilePath = targetFile.getAbsolutePath();
        if (targetFile.isFile()) {
            closeTabPath(targetFilePath);
        } else {
            for (int i = tabFiles.size() - 1; i >= 0; i--) {
                String tabFile = tabFiles.get(i);
                if (tabFile.contains(targetFilePath)) {
                    closeTabIndex(i);
                }
            }

        }

    }


    /**
     * 重命名tab
     *
     * @param filePath    旧文件路径
     * @param newFilePath 新文件路径
     */
    private void renameTabFile(String filePath, String newFilePath) {

        int targetIndex = tabFiles.indexOf(filePath);
        if (targetIndex > -1) {
            tabFiles.remove(targetIndex);
            tabFiles.add(targetIndex, newFilePath);

            File newFile = new File(newFilePath);
            // 获取需要更新的组件 更新绑定文件
            RichFileEditorView richFileEditorView = getEditorView(targetIndex);
            if (richFileEditorView != null) {
                richFileEditorView.updateDiskFileStore(newFile);
            }
            // 更新选项卡
            String name = newFile.getName();
            String relativePath = noteFileStore.getRelativePath(newFile);
            openTabs.setTitleAt(targetIndex, existsTabName(name, relativePath));
            openTabs.setToolTipTextAt(targetIndex, relativePath);
        }
    }


    /**
     * @param targetIndex 选项卡下标
     * @return 返回编辑器视图
     */
    private RichFileEditorView getEditorView(Integer targetIndex) {
        RichFileEditorView richFileEditorView = null;
        if (targetIndex == null || targetIndex == -1) {
            return null;
        }
        Component component = openTabs.getComponentAt(targetIndex);
        if (component instanceof SpinLoading) {
            SpinLoading spinLoading = (SpinLoading) component;
            richFileEditorView = (RichFileEditorView) spinLoading.getComponent();
        } else {
            richFileEditorView = (RichFileEditorView) component;
        }
        return richFileEditorView;

    }


    /**
     * @param name         选项卡简称
     * @param relativePath 选项卡全称
     * @return 如果简称未存在 返回简称,否则返回全称;
     */
    private String existsTabName(String name, String relativePath) {
        int index = findIndexByTabName(name);
        if (index > -1) {
            String tooltipText = openTabs.getToolTipTextAt(index);
            openTabs.setTitleAt(index, tooltipText);
            return relativePath;
        }
        return name;
    }

    private static final Cache<String, RichFileEditorView> EDITOR_VIEW_CACHE = CacheUtil.newLFUCache(20);

    /**
     * 创建选项卡
     *
     * @param noteFile 文件
     * @param addIndex 添加下标
     */
    public void createTab(File noteFile, Integer addIndex) {
        String name = noteFile.getName();
        String relativePath = noteFileStore.getRelativePath(noteFile);
        //如果name 不存在返回 name, 否则返回relativePath
        String tabName = existsTabName(name, relativePath);
        RichFileEditorView richFileEditorView = null;
        if (EDITOR_VIEW_CACHE.containsKey(relativePath)) {
            log.debug("加载编辑视图!!! from cache");
            richFileEditorView = EDITOR_VIEW_CACHE.get(relativePath);
        } else {
            log.debug("加载编辑视图!!! from new");
            richFileEditorView = new RichFileEditorView(noteFileStore, noteFile);
            EDITOR_VIEW_CACHE.put(relativePath,richFileEditorView);
        }

        // 获取插入下标
        if (addIndex == null) {
            addIndex = openTabs.getTabCount();
        }

        if (activeIndex == null) {
//            SpinLoading<RichFileEditorView> spinLoading = new SpinLoading<>(richFileEditorView);
            openTabs.insertTab(tabName, NoteConstants.NOTE_ICON, richFileEditorView, relativePath, addIndex);
        } else {
            openTabs.insertTab(tabName, NoteConstants.NOTE_ICON, richFileEditorView, relativePath, addIndex);

        }
    }


    /**
     * 变化时修改激活index
     */
    @InitAction
    private void changeActiveIndex() {
        /*监听选择事件*/
        openTabs.addChangeListener(e -> {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            activeIndex = pane.getSelectedIndex();
            requestFocus2Editor();
        });

        /* 监听右键点击*/
        openTabs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    String relativePath = pane.getToolTipText(e);
                    if (relativePath != null) {
                        String filePath = noteFileStore.getFile(relativePath).getAbsolutePath();
                        rightClickIndex = tabFiles.indexOf(filePath);
                    } else {
                        rightClickIndex = null;
                    }

                }
            }
        });

        /* 添加右键菜单*/
        openTabs.addMouseListener(new NoteTabPopupView(this));

    }

    /**
     * 查找选项卡名称是否存在
     */
    private Integer findIndexByTabName(String tabName) {
        for (int i = 0; i < openTabs.getTabCount(); i++) {
            String tabTitle = openTabs.getTitleAt(i);
            if (StrUtil.equals(tabTitle, tabName)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * @param tooltipText tab提示文本
     * @return tab所在下标
     */
    private Integer findIndexByTooltipText(String tooltipText) {
        for (int i = 0; i < openTabs.getTabCount(); i++) {
            String tabsToolTipText = openTabs.getToolTipTextAt(i);
            if (StrUtil.equals(tabsToolTipText, tooltipText)) {
                return i;
            }
        }
        return null;
    }

    /**
     * 加载序列化文件
     */
    private void loadSerializeFile() {
        // 第一次序列化时,显示加载进度
        int index = activeIndex == null ? -1 : activeIndex;
        tabFiles.forEach((filePath) -> {
            File file = new File(filePath);
            createTab(file, null);
        });
        if (index > -1) {
            activeIndex = index;
            openTabs.setSelectedIndex(activeIndex);
        }
    }


    /**
     * 关闭当前
     */
    public void closeNow() {
        if (rightClickIndex == null) {
            rightClickIndex = openTabs.getSelectedIndex();
        }
        tabFiles.stream().filter(filePath -> tabFiles.indexOf(filePath) == rightClickIndex)
                .collect(Collectors.toList()).forEach(this::closeTabPath);
    }


    /**
     * 关闭其他
     */
    public void closeOther() {
        tabFiles.stream().filter(filePath -> tabFiles.indexOf(filePath) != rightClickIndex)
                .collect(Collectors.toList()).forEach(this::closeTabPath);
    }

    /**
     * 关闭所有
     */
    public void closeAll() {
//        openTabs.removeAll();
//        tabFiles.clear();
//        activeIndex = null;
//        rightClickIndex = null;
        int i = openTabs.getTabCount() - 1;
        for (; i >= 0; i--) {
            closeTabIndex(i);
        }
    }

    /**
     * 关闭左侧
     */
    public void closeLeft() {
        tabFiles.stream().filter(filePath -> tabFiles.indexOf(filePath) < rightClickIndex)
                .collect(Collectors.toList()).forEach(this::closeTabPath);
    }

    /**
     * 关闭右侧
     */
    public void closeRight() {
        tabFiles.stream().filter(filePath -> tabFiles.indexOf(filePath) > rightClickIndex)
                .collect(Collectors.toList()).forEach(this::closeTabPath);
    }


    /**
     * @param filePath 提供文件路径关闭选项卡
     */
    public void closeTabPath(String filePath) {
        closeTabIndex(tabFiles.indexOf(filePath));
    }

    /**
     * 关闭时自动保存打开文件内容,
     * 重置激活下标和右键下表
     *
     * @param closeIndex 关闭选项卡下标
     */
    private void closeTabIndex(int closeIndex) {
        RichFileEditorView richFileEditorView = getEditorView(closeIndex);
        if (richFileEditorView != null) {
            richFileEditorView.save();
        }

        tabFiles.remove(closeIndex);
        openTabs.removeTabAt(closeIndex);

        // 重新计算激活下标和右键下标
        activeIndex = openTabs.getTabCount() == 0 ? null : openTabs.getSelectedIndex();
        rightClickIndex = null;

    }


    /**
     * 保存所有已经打开的tab
     */
    public void saveAllTabEditors() {
        IntStream.range(0, openTabs.getTabCount()).forEach(i -> {
            RichFileEditorView richFileEditorView = getEditorView(i);
            if (richFileEditorView != null) {
                richFileEditorView.save();
            }
        });
    }

    /**
     * @return 是否最左侧
     */
    public boolean isOnLeft() {
        return rightClickIndex == 0;
    }

    /**
     * @return 是否最右侧
     */
    public boolean isOnRight() {
        return rightClickIndex == (tabFiles.size() - 1);
    }

    /**
     * @return 是否只有一个
     */
    public boolean isOnlyOne() {
        return tabFiles.size() == 1;
    }


    /**
     * 是否右键点击过
     */
    public boolean isRightClicked() {
        return rightClickIndex != null;
    }


    /**
     * 不返回文件后缀
     *
     * @return 获得激活的treePath
     */
    public String[] getActivePaths() {
        if (activeIndex != null) {
            String filePath = tabFiles.get(activeIndex);
            filePath = filePath.substring(0, filePath.length() - NoteConstants.FILE_TYPE.length());
            File activeFile = new File(filePath);
            return noteFileStore.getRelativePaths(activeFile);
        }
        return null;

    }
}
