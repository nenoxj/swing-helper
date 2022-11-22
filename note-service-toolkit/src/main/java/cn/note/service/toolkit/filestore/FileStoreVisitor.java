package cn.note.service.toolkit.filestore;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    Files.walkFileTree(file.toPath(), new FileStoreVisitor(file, fileFilter, ignoreDirs) {
 *             @Override
 *             public void resolve(Path path) {
 *                 consumer.accept(path);
 *             }
 *         });
 * </pre>
 *
 * @author jee
 * 文件迭代器
 */
public abstract class FileStoreVisitor extends SimpleFileVisitor<Path> {

    /**
     * 根路径
     */
    private File root;

    /**
     * 文件过滤器
     */
    private IOFileFilter fileFilter;

    /**
     * 目录过滤集合
     * 应该使用/a/b 或者a ,b 得形式, 否则匹配不准确
     */
    private List<String> ignorePaths;

    public FileStoreVisitor(File root) {
        this(root, FileFilterUtils.trueFileFilter(), new ArrayList<>());
    }

    public FileStoreVisitor(File root, IOFileFilter fileFilter, List<String> ignorePaths) {
        this.root = root;
        this.fileFilter = fileFilter;
        this.ignorePaths = ignorePaths;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        filterPath(dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        filterPath(file);
        return FileVisitResult.CONTINUE;
    }

    /**
     * @param path 文件路径
     */
    public abstract void resolve(Path path);


    /**
     * 检查过滤文件
     *
     * @param path 文件路径
     */
    private void filterPath(Path path) {
        File file = path.toFile();
        if (ignorePaths != null && ignorePaths.size() > 0) {
            String relativePath = FilePathUtil.getRelativePath(file, root);
            for (String ignorePath : ignorePaths) {
                ignorePath = FilePathUtil.strictPath(ignorePath);
                if (FilePathUtil.containsPath(ignorePath, relativePath)) {
                    return;
                }
            }
        }
        if (file.isFile()) {
            if (fileFilter.accept(file)) {
                resolve(path);
            }
        } else {
            resolve(path);

        }

    }

}
