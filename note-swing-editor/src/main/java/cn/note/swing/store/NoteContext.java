package cn.note.swing.store;

import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.service.toolkit.filestore.SettingFileStore;
import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.swing.core.lifecycle.DestroyManager;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.tree.DragJXTree;
import cn.note.swing.view.note.ResourcesView;
import cn.note.swing.view.note.category.CategoryView;
import cn.note.swing.view.note.category.event.CategoryKeyAction;
import cn.note.swing.view.remotegit.RemoteGitView;
import cn.note.swing.view.note.tab.NoteTabView;
import cn.note.swing.view.note.tab.bean.NoteTabBean;
import cn.note.swing.view.note.tab.event.NoteTabActionSupport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.FocusManager;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Note笔记 上下文
 */
@Slf4j
public class NoteContext {

    /*单例对象*/
    private static NoteContext noteContext;

    /**
     * 文件存储
     */
    @Getter
    private RelativeFileStore noteFileStore;

    /**
     * 图片存储
     */
    @Getter
    private RelativeFileStore noteImageStore;
    /**
     * 分类序列化存储
     */
    private SettingFileStore categorySettingStore;
    /**
     * 选项卡序列化存储
     */
    private SettingFileStore noteTabSettingStore;
    /**
     * 目录视图
     */
    @Getter
    private CategoryView categoryView;
    /**
     * 选项卡视图
     */
    @Getter
    private NoteTabView noteTabView;


    @Getter
    private ResourcesView resourcesView;
    /**
     * 索引管理器
     */
    @Getter
    private NoteIndexManager noteIndexManager;
    /**
     * 配置信息
     */
    @Getter
    private NoteConfigManager noteConfigManager;


    private NoteContext() {
        noteFileStore = new RelativeFileStore(SystemFileManager.SYSTEM_DIR, NoteConstants.NOTE_NAME);
        categorySettingStore = new SettingFileStore(SystemFileManager.SYSTEM_DIR, NoteConstants.Category.SETTING_NAME);
        noteTabSettingStore = new SettingFileStore(SystemFileManager.SYSTEM_DIR, NoteConstants.NoteTab.SETTING_NAME);

        // 不可以在view中引用NoteContext实例 ,否则会造成循环引用!!!!
        initViewList();
        initActionList();
        initDestroyListener();

        // 构建索引管理
        noteIndexManager = new NoteIndexManager(noteFileStore);
        // 构建配置管理
        noteConfigManager = new NoteConfigManager();

    }


    /**
     * 获取当前获得焦点的窗口
     */
    public Window getActiveWindow() {
        return FocusManager.getCurrentManager().getActiveWindow();
    }


    public static NoteContext getInstance() {
        if (noteContext == null) {
            synchronized (NoteContext.class) {
                noteContext = new NoteContext();
            }
        }
        return noteContext;
    }


    /**
     * 初始化视图
     */
    public void initViewList() {
        initCategoryView();
        initNoteTabView();
        initResourcesView();
    }


    /**
     * 初始化视图 动作
     */
    public void initActionList() {
        new CategoryKeyAction(this).addKeyActions();
        new NoteTabActionSupport(this).addKeyActions();
    }

    /**
     * 加载序列化的树结构
     * 不存在返回默认树结构
     */
    private void initCategoryView() {
        // 尝试读取序列化视图
        try {
            if (categorySettingStore.exists()) {
                DragJXTree dragJXTree = null;
                dragJXTree = (DragJXTree) SwingCoreUtil.deserialize(categorySettingStore.read());

                categoryView = new CategoryView(noteFileStore, dragJXTree);
                log.debug("加载目录结构");
            }
        } catch (IOException e) {
            log.error("读取序列化目录异常", e);
        }

        if (categoryView == null) {
            categoryView = new CategoryView(noteFileStore);
            categoryView.reloadRoot();
        }
    }


    private void initResourcesView() {
        RemoteGitView remoteGitView = new RemoteGitView();
        resourcesView = new ResourcesView(categoryView, remoteGitView);
        EventBusManager.getInstance().register(remoteGitView);
    }


    /**
     * 选项卡视图
     */
    private void initNoteTabView() {
        // 尝试读取序列化选项卡
        try {
            if (noteTabSettingStore.exists()) {
                NoteTabBean noteTabBean = (NoteTabBean) SwingCoreUtil.deserialize(noteTabSettingStore.read());
                noteTabView = new NoteTabView(noteFileStore, noteTabBean);
                log.debug("加载选项卡视图");
            }
        } catch (IOException e) {
            log.error("读取选项卡异常", e);
        }
        if (noteTabView == null) {
            noteTabView = new NoteTabView(noteFileStore);
        }
    }




    /**
     * 关闭时存储tree
     */
    public void initDestroyListener() {
        DestroyManager.addDestroyEvent(() -> {
            try {
                // 保存所有打开文件
                noteTabView.saveAllTabEditors();

                // 序列化目录树
                CountDownLatch cdl = new CountDownLatch(1);
                SwingUtilities.invokeLater(() -> {
                    try {
                        categorySettingStore.write(SwingCoreUtil.serialize(categoryView.getTree()));
                    } catch (IOException e) {
                        log.error("序列化目录树异常:", e);
                    } finally {
                        cdl.countDown();
                    }
                });

                // 序列化文件存储
                noteTabSettingStore.write(SwingCoreUtil.serialize(noteTabView.getNoteTabBean()));
                cdl.await();
            } catch (Exception e) {
                log.error("序列化配置异常:", e);
            }
        });

    }

}
