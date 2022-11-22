package cn.note.service.toolkit.filestore;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @description: 配置文件存储
 * @author: jee
 */
@Slf4j
public class SettingFileStore implements FileStore {

    public static final String SUFFIX = ".setting";

    private File mainDir;

    private String setting;

    public SettingFileStore(String mainDir, String settingName) {
        this.mainDir = FileUtils.getFile(mainDir);
        this.setting = settingName.concat(SUFFIX);
    }

    /**
     * @return 主目录
     */
    @Override
    public File homeDir() {
        return mainDir;
    }


    /**
     * @param content 文件内容
     * @throws IOException 写入异常
     */
    public void write(String content) throws IOException {
        delete(setting);
        writeFile(setting, content);
        log.debug("update setting: {}", setting);
    }

    /**
     * @return 文件内容
     * @throws IOException 读取异常
     */
    public String read() throws IOException {
        return readFile(setting);
    }

    /**
     * @return 是否存在
     */
    public boolean exists() {
        return exists(setting);
    }
}
