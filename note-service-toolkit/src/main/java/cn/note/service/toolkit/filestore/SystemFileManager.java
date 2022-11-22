package cn.note.service.toolkit.filestore;

import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * @description: 系统文件信息
 * @author: jee
 */
@Slf4j
public class SystemFileManager {

    /* 桌面目录*/
    public static final File DESKTOP_HOME = FileSystemView.getFileSystemView().getHomeDirectory();

    /**
     * 主用户目录
     */
    public static final String USER_HOME = SystemUtil.getUserInfo().getHomeDir();

    /**
     * 临时存储目录
     */
    public static final String TEMP_HOME = SystemUtil.getUserInfo().getTempDir();


    /**
     * 是否windows
     */
    public static final boolean IS_WINDOWS = SystemUtil.getOsInfo().isWindows();


    /**
     * 运行ID
     */
    public static final long PROCESS_ID = SystemUtil.getCurrentPID();


    /**
     * 系统存储目录
     */
    public static String SYSTEM_DIR = USER_HOME;


    /**
     * 主目录
     */
    private static FileStore HOME_DIR = null;

    /**
     * 主目录存储
     */
    public static FileStore getSysStore() {
        if (HOME_DIR == null) {
            HOME_DIR = new RelativeFileStore(SYSTEM_DIR);
        }
        return HOME_DIR;
    }


    /**
     * @param filePath 更改系统路径
     */
    public static void updateSystemDir(@Nonnull String filePath) {
        log.info("system store dir change to:{}", filePath);
        File file = FileUtils.getFile(filePath);
        if (!file.exists()) {
            try {
                FileUtils.forceMkdir(file);
                SYSTEM_DIR = filePath;
            } catch (Exception e) {
                log.error("变更系统目录异常:{}", e.getMessage());
            }

        } else {
            SYSTEM_DIR = filePath;

        }
    }

    /**
     * 指定测试空间
     */
    public static void updateSystemDir2Default() {
        updateSystemDir("D:/note-helper");
    }

}
