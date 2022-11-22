package cn.note.swing.view.remotegit;

import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.service.toolkit.git.GitTemplate;
import cn.note.service.toolkit.git.GitUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.form.CheckBoxFormItem;
import cn.note.swing.core.view.form.InputFormItem;
import cn.note.swing.core.view.loading.LoadingUtil;
import cn.note.swing.core.view.modal.ModalOptButton;
import cn.note.swing.store.NoteConfigManager;
import cn.note.swing.store.NoteConstants;
import cn.note.swing.store.NoteContext;
import cn.note.swing.view.note.category.CategoryView;
import cn.note.swing.view.note.tab.NoteTabView;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class RemoteSettingView extends AbstractMigView {

    /* git地址 */
    private InputFormItem gitInput;

    /* 是否保留本地 */
    private CheckBoxFormItem retain;

    /*按钮操作*/
    private ModalOptButton modalOptButton;

    private NoteConfigManager noteConfigManager;

    private CategoryView categoryView;

    private NoteTabView noteTabView;

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
        modalOptButton.setOkText("clone");
        NoteContext noteContext = NoteContext.getInstance();
        noteConfigManager = noteContext.getNoteConfigManager();
        categoryView = noteContext.getCategoryView();
        noteTabView = noteContext.getNoteTabView();

    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        gitInput = new InputFormItem("git地址");
        gitInput.setFieldValue(noteConfigManager.getGitUrl());
        gitInput.validEmpty();
        retain = new CheckBoxFormItem("保留本地修改");
        retain.setFieldValue(true);
        view.add(gitInput, "grow,wrap,w ::300");
        view.add(retain, "grow,wrap");
        view.add(modalOptButton, "span 2,grow");
    }


    /**
     * 确认
     */
    @InitAction
    private void confirmAction() {
        ConsumerAction cloneAction = new ConsumerAction(e -> {
            if (!gitInput.valid()) {
                LoadingUtil.progressLoading("clone ...", () -> {
                    String gitUrl = gitInput.getFieldValue().trim();
                    noteConfigManager.setGitUrl(gitUrl);
                    File noteDir = FileUtils.getFile(SystemFileManager.SYSTEM_DIR, NoteConstants.NOTE_NAME);
                    File noteGitDir = FileUtils.getFile(noteDir, ".git");
                    File bakDir = FileUtils.getFile(SystemFileManager.SYSTEM_DIR, NoteConstants.NOTE_BAK_NAME);
                    try {
                        // 强制删除历史.git
                        FileUtils.forceDelete(noteGitDir);
                        // 备份目录
                        if (bakDir.exists()) {
                            FileUtils.cleanDirectory(bakDir);
                        } else {
                            FileUtils.forceMkdir(bakDir);
                        }
                        FileUtils.copyDirectory(noteDir, bakDir);
                        FileUtils.deleteDirectory(noteDir);
                        GitTemplate gitTemplate = GitUtil.cloneCheckCredit(gitUrl, noteDir);
                        noteConfigManager.setGitTemplate(gitTemplate);

                        // 恢复文件
                        if (retain.getFieldValue()) {
                            FileUtils.copyDirectory(bakDir, noteDir);
                        } else {
                            // 关闭所有打开文件
                            noteTabView.closeAll();
                        }
                        // 重载分类面板
                        categoryView.reloadRoot();
                        SwingUtilities.getWindowAncestor(modalOptButton).dispose();
                    } catch (IOException e1) {
                        log.error("clone 异常:", e1);
                        gitInput.setError("clone 失败:" + e1.getMessage());
                        // 还原备份
                        if (bakDir.exists()) {
                            try {
                                FileUtils.copyDirectory(bakDir, noteDir);
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        modalOptButton.okCall(cloneAction);
    }


}
