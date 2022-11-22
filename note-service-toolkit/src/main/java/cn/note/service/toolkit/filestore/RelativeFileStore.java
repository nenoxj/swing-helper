package cn.note.service.toolkit.filestore;

import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * @description: 相对存储
 * 在主目录下存储
 * @author: jee
 */
@Slf4j
public class RelativeFileStore implements FileStore {

    @Setter
    @Getter
    private IOFileFilter fileFilter;

    @Setter
    @Getter
    private List<String> ignoreDirs;

    private File mainDir;

    public RelativeFileStore(File mainDir) {
        init(mainDir);
    }

    public RelativeFileStore(String mainDir) {
        init(new File(mainDir));
    }

    public RelativeFileStore(String mainDir, String... secondDir) {
        File dir;
        if (secondDir != null) {
            String[] dirArray = ArrayUtil.insert(secondDir, 0, mainDir);
            dir = FileUtils.getFile(dirArray);
        } else {
            dir = FileUtils.getFile(mainDir);
        }
        init(dir);
    }

    private void init(File mainDir) {
        try {
            FileUtils.forceMkdir(mainDir);
        } catch (IOException e) {
            log.error("创建目录失败!!!", e);
            throw new RuntimeException("无法创建目录:" + mainDir.getAbsolutePath());
        }
        this.mainDir = mainDir;
        this.fileFilter = FileFilterUtils.trueFileFilter();
    }


    /**
     * @return 主目录
     */
    @Override
    public File homeDir() {
        return mainDir;
    }


    public Path path() {
        return mainDir.toPath();
    }


    @Override
    public void lists(Consumer<Path> consumer) throws IOException {
        lists(mainDir, fileFilter, ignoreDirs, consumer);
    }


    /**
     * 当前目录创建 相对文件存储
     */
    public RelativeFileStore getRelativeFileStore(String... paths) {
        return new RelativeFileStore(mainDir.getAbsolutePath(), paths);
    }
}
