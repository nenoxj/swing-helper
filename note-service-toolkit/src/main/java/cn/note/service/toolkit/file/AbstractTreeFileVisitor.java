package cn.note.service.toolkit.file;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.filefilter.IOFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * @author jee
 * 文件目录树遍历
 *
 * <pre>
 *         AbstractTreeFileVisitor fileVisitor = new AbstractTreeFileVisitor() {
 *             @Override
 *             public void addNode(Path path, boolean isDir) {
 *               // 处理遍历得path;
 *             }
 *         };
 *         fileVisitor.setFileFilter(fileFilter);
 *         if (ignoreDirs != null) {
 *             fileVisitor.setIgnoreDirs(ignoreDirs);
 *         }
 *         Files.walkFileTree(fileStore.homeDir().toPath(), fileVisitor);
 *
 * </pre>
 */
@Setter
@Getter
public abstract class AbstractTreeFileVisitor extends SimpleFileVisitor<Path> {
    private int tid = 0;
    private int pid = 0;
    private StackList<Integer> stackList = new StackList<>();
    /**
     * 文件过滤器
     * 多条件过滤应该使用
     * IOFileFilter suffixFilter = FileFilterUtils.suffixFileFilter("java");
     * IOFileFilter notFilter = FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("target"));
     * <p>
     * IOFileFilter concatFilters=  FileFilterUtils.and(suffixFilter, notFilter)
     */
    private IOFileFilter fileFilter;
    /*忽略目录*/
    private List<String> ignoreDirs;
    private int fileCount = 0;
    private int dirCount = 0;

    /**
     * @see SimpleFileVisitor#preVisitDirectory(Object,
     * BasicFileAttributes) 遍历目录
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

        if (ignoreDirs != null && ignoreDirs.size() > 0) {
            // 目录过滤
            ignoreDirs.forEach(ignore -> {
                String ignorePath = File.separator + ignore;

                if (!dir.toString().contains(ignorePath)) {
                    addDirectoryInTreeNodes(dir);
                    dirCount++;
                }
            });
        } else {
            // 目录不过滤
            addDirectoryInTreeNodes(dir);
            dirCount++;
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * @see SimpleFileVisitor#visitFile(Object, BasicFileAttributes)
     * @see org.apache.commons.io.filefilter.FileFilterUtils
     * <p>
     * <p>
     * 文件过滤
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        // 文件过滤
        if (fileFilter != null) {
            if (fileFilter.accept(file.toFile())) {
                addFileInTreeNodes(file);
                fileCount++;
            }
        } else {
            addFileInTreeNodes(file);
            fileCount++;
        }

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return super.visitFileFailed(file, exc);
    }

    /**
     * (non-Javadoc)
     *
     * @see SimpleFileVisitor#postVisitDirectory(Object,
     * IOException) 遍历目录后操作
     */
    @Override
    public FileVisitResult postVisitDirectory(Path outDir, IOException exc) throws IOException {
        visitDirectory(outDir);
        return super.postVisitDirectory(outDir, exc);
    }

    private void addFileInTreeNodes(Path file) {
        tid++;
        addFileNodes(file);
    }

    private void addDirectoryInTreeNodes(Path dir) {
        tid++;
        addDirNodes(dir);
        pid = stackList.push(tid);
    }

    private void visitDirectory(Path outDir) {
        Integer pop = stackList.popPrev();
        if (pop != null) {
            pid = pop;
        }
    }

    /**
     * 提供被复写  的添加文件信息
     * tid  当前文件ID  pid父目录ID   文件名和文件路径
     *
     * @param file 文件
     */
    public void addFileNodes(Path file) {
        addNode(file, false);
    }

    /**
     * 提供被复写  的添加目录信息
     * tid  当前文件ID  pid父目录ID   文件名和文件路径
     *
     * @param dir 文件目录
     */
    public void addDirNodes(Path dir) {
        addNode(dir, true);
    }

    /**
     * @param path  文件路径
     * @param isDir 是否目录
     */
    public abstract void addNode(Path path, boolean isDir);


}
