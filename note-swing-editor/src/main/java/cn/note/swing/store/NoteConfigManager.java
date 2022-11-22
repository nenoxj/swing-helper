package cn.note.swing.store;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.service.toolkit.git.GitTemplate;
import cn.note.service.toolkit.git.GitUtil;
import cn.note.swing.core.lifecycle.DestroyManager;
import cn.note.swing.core.util.WinUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author jee
 * @version 1.0
 */
@Slf4j
public class NoteConfigManager {

    /*配置对象*/
    private NoteConfig noteConfig;

    /*配置信息*/
    private String configText;

    /*配置文件*/
    private File configFile = FileUtils.getFile(SystemFileManager.SYSTEM_DIR, NoteConstants.NOTE_CONFIG);

    /*git操作对象*/
    @Getter
    @Setter
    private GitTemplate gitTemplate;


    public NoteConfigManager() {
        initialize();
    }


    public void initialize() {

        ThreadUtil.execute(() -> {
            readNoteConfig();
            invalidGitTemplate();
        });

        DestroyManager.addDestroyEvent(this::saveNoteConfig);
    }

    /**
     * 获取系统配置
     */
    public void readNoteConfig() {
        try {
            FileUtils.touch(configFile);
            configText = FileUtils.readFileToString(configFile);
            if (StrUtil.isBlank(configText)) {
                configText = "{}";
            }
            noteConfig = JSONUtil.toBean(configText, NoteConfig.class);
        } catch (IOException e) {
            log.error("读取配置异常!!", e);
            WinUtil.alert("读取配置异常", e.getMessage());
            noteConfig = new NoteConfig();
        }
    }

    /**
     * 保存系统配置
     */
    public void saveNoteConfig() {
        String newConfigText = JSONUtil.toJsonPrettyStr(noteConfig);
        if (!configText.equals(newConfigText)) {
            log.debug("配置信息更新==>\n{}", newConfigText);
            try {
                FileUtils.write(configFile, newConfigText);
            } catch (IOException e) {
                log.error("保存配置异常!!", e);
            }
        }

    }


    /**
     * 校验并创建git对象
     */
    private void invalidGitTemplate() {

        String gitUrl = getGitUrl();
        if (gitUrl == null) {
            log.debug("未设置git,reason:未找到git地址!");
            return;
        }

        File noteDir = FileUtils.getFile(SystemFileManager.SYSTEM_DIR, NoteConstants.NOTE_NAME);
        if (!noteDir.exists()) {
            log.debug("未设置git,reason:丢失note主目录");
            return;
        }

        try {
            gitTemplate = GitUtil.loadGitDirCheckCredit(noteDir);
            // 通知git 已建立
            EventBusManager.getInstance().postNotice(gitTemplate);
            log.debug("git地址:" + gitUrl);
        } catch (Exception e) {
            log.debug("未设置git,reason:" + e.getMessage());
        }


    }


    /**
     * 设置git地址信息
     *
     * @param gitUrl giturl
     */
    public void setGitUrl(String gitUrl) {
        this.noteConfig.setGitUrl(gitUrl);
    }


    /**
     * 获取git地址信息
     */
    public String getGitUrl() {
        return this.noteConfig.getGitUrl();
    }


    @Setter
    @Getter
    private class NoteConfig {

        /**
         * gitUrl 地址
         */
        private String gitUrl;


    }
}
