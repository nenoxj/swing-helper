package cn.note.service.toolkit.filestore;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.log.StaticLog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * @description:
 * @author: jee
 */
public interface FileStore {

    /**
     * 通用的文件分隔符 /
     */
    String COMMON_SEPARATOR = "/";

    /**
     * window 文件分隔符 \\
     */
    String WINDOWS_SEPARATOR = "\\\\";

    /**
     * @return 主目录
     */
    File homeDir();


    default File getFile(String... paths) {
        return FileUtils.getFile(homeDir(), paths);
    }


    /**
     * @param paths 文件路径
     * @throws IOException 创建目录异常
     */
    default File createDir(String... paths) throws IOException {
        File file = getFile(paths);
        if (file.exists()) {
            throw new IOException("目录已存在!");
        }
        FileUtils.forceMkdir(file);
        return file;
    }

    /**
     * 创建文件
     *
     * @param paths 路径
     * @throws IOException 创建文件异常
     */
    default File createFile(String... paths) throws IOException {
        File file = getFile(paths);
        if (file.exists()) {
            throw new IOException("文件已存在!");
        }
        FileUtils.touch(file);
        return file;
    }

    /**
     * 删除文件 严格模式
     *
     * @param paths 路径
     */
    default void delete(String... paths) throws IOException {
        File file = getFile(paths);
        FileUtils.deleteQuietly(file);
    }




    default void deleteQuietly(File file) {
        FileUtils.deleteQuietly(file);
    }

    /**
     * 重命名文件
     *
     * @param targetFile 目标文件
     * @param newName    新名称 不影响后缀
     * @return 文件路径
     */
    default File rename(File targetFile, String newName) {
        return FileUtil.rename(targetFile, newName, true, true);
    }


    /**
     * 移动文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @throws IOException 文件错误
     */
    default void move(File srcFile, File destFile) throws IOException {
        if (!srcFile.exists()) {
            throw new IOException("源文件或目录不存在!" + srcFile.getAbsolutePath());
        }
        if (!destFile.exists()) {
            throw new IOException("目标文件或目录不存在!" + destFile.getAbsolutePath());
        }
        FileUtil.move(srcFile, destFile, true);
    }


    /**
     * 移动目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     * @throws IOException 移动目录异常
     */
    default void moveDir2Dir(File srcDir, File destDir) throws IOException {
        if (!srcDir.exists()) {
            throw new IOException("源目录不存在!" + srcDir.getAbsolutePath());
        }
        if (!destDir.exists()) {
            throw new IOException("目标目录不存在!" + destDir.getAbsolutePath());
        }
        FileUtil.move(srcDir, destDir, true);
        StaticLog.debug("move dir: {} to {}", srcDir.getAbsolutePath(), destDir.getAbsolutePath());

    }


    /**
     * 移动文件进目录
     *
     * @param srcFile 源文件
     * @param destDir 目标目录
     * @throws IOException 移动文件异常
     */
    default void moveFile2Dir(File srcFile, File destDir) throws IOException {
        if (!srcFile.exists()) {
            throw new IOException("源文件不存在!" + srcFile.getAbsolutePath());
        }
        if (!destDir.exists()) {
            throw new IOException("目标目录不存在!" + destDir.getAbsolutePath());
        }
        //删除存在的文件
        FileUtil.move(srcFile, destDir, true);
        StaticLog.debug("move file: {} to {}", srcFile.getAbsolutePath(), destDir.getAbsolutePath());
    }

    /**
     * 判断文件或目录是否存在
     *
     * @param paths 路径
     * @return 是否存在
     */
    default boolean exists(String... paths) {
        return getFile(paths).exists();
    }

    /**
     * 清理主目录空间
     */
    default void clean() {
        try {
            FileUtils.cleanDirectory(homeDir());
        } catch (IOException e) {
            StaticLog.error("clean directory:{} error:{}", homeDir(), e.getMessage());
        }
    }

    /**
     * @param fileName    文件名
     * @param fileContent 文件内容
     * @throws IOException 写入异常
     */
    default void writeFile(String fileName, String fileContent) throws IOException {
        File file = FileUtils.getFile(homeDir(), fileName);
        FileUtils.write(file, fileContent);
    }


    /**
     * 写入文件
     *
     * @param bao 字节流
     */
    default void writeFile(String fileName, ByteArrayOutputStream bao) throws IOException {
        File file = FileUtils.getFile(homeDir(), fileName);
        FileUtils.writeByteArrayToFile(file, bao.toByteArray());
    }

    /**
     * @param fileName 文件名
     * @return 文件内容
     * @throws IOException 读取异常
     */
    default String readFile(String fileName) throws IOException {
        File file = FileUtils.getFile(homeDir(), fileName);
        return FileUtils.readFileToString(file);
    }


    /**
     * 获取相对路径
     *
     * @param file 当前fileStore 存储的文件
     */
    default String getRelativePath(File file) {
        String relativePath = file.getAbsolutePath().replace(homeDir().getAbsolutePath(), "");
        relativePath = formatPath(relativePath);
        return relativePath.indexOf(COMMON_SEPARATOR) == 0 ? relativePath.substring(1) : relativePath;
    }

    default String[] getRelativePaths(File file) {
        String path = getRelativePath(file);
        if (path.indexOf(COMMON_SEPARATOR) == 0) {
            path = path.substring(1);
        }
        return path.split(COMMON_SEPARATOR);
    }


    /**
     * 格式化路径 \\ ->/
     *
     * @param path 路径
     */
    default String formatPath(String path) {
        return path.replaceAll(WINDOWS_SEPARATOR, COMMON_SEPARATOR);
    }

    default String formatPath(File file) {
        return formatPath(file.getAbsolutePath());
    }


    /**
     * @param file 文件路径
     * @return 获取文件最后修改时间
     */
    default Date getModifiedTime(File file) {
        return FileUtil.lastModifiedTime(file);
    }


    /**
     * @param file 文件路径
     * @return 文件修改日期
     */
    default String getModifiedDate(File file) {
        return DateUtil.formatDate(getModifiedTime(file));
    }

    /**
     * 遍历主目录
     *
     * @param consumer 自定义文件接收器
     * @throws IOException 遍历异常
     */
    default void lists(Consumer<Path> consumer) throws IOException {
        lists(homeDir(), FileFilterUtils.trueFileFilter(), null, consumer);
    }

    /**
     * 遍历主目录
     *
     * @param fileFilter 文件过滤器
     * @param ignoreDirs 忽略目录
     * @param consumer   自定义文件接收器
     * @throws IOException 遍历异常
     */
    default void lists(IOFileFilter fileFilter, List<String> ignoreDirs, Consumer<Path> consumer) throws IOException {
        lists(homeDir(), fileFilter, ignoreDirs, consumer);
    }


    /**
     * @param file       文件 属于homeDir 或其子目录
     * @param fileFilter 文件过滤器
     * @param ignoreDirs 忽略目录
     * @param consumer   自定义接收文件事件
     * @throws IOException 遍历异常
     */
    default void lists(@Nonnull File file, @Nonnull IOFileFilter fileFilter, List<String> ignoreDirs, @Nonnull Consumer<Path> consumer) throws IOException {
        Files.walkFileTree(file.toPath(), new FileStoreVisitor(file, fileFilter, ignoreDirs) {
            @Override
            public void resolve(Path path) {
                consumer.accept(path);
            }
        });
    }
}
