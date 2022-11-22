package cn.note.service.toolkit.file;

import cn.hutool.core.util.StrUtil;
import cn.note.service.toolkit.filestore.FileStore;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 目录索引管理器
 * @author: jee
 */
public class FileIndexManager extends AbstractIndexManager<FileIndex> {


    public FileIndexManager(String homeDir) {
        super(homeDir);
    }

    public FileIndexManager(FileStore fileStore) {
        super(fileStore);
    }

    @Override
    public List<FileIndex> searchIndex(String indexContext) {
        if (!isInitCompleted()) {
            throw new IllegalStateException("调用是否isInitCompleted 检查初始化是否完成!!!");
        }

        if (StrUtil.isBlank(indexContext)) {
            return Collections.emptyList();
        }
        final String matchName = indexContext.toLowerCase();
        return getIndexes().values().stream().filter(index -> !index.isDir() && index.getSearchName().contains(matchName)).collect(Collectors.toList());

    }

    @Override
    protected FileIndex fileToIndexObject(File file) {
        String fileName = file.getName();
        String relativePath = getFileStore().getRelativePath(file);
        String modifiedDate = getFileStore().getModifiedDate(file);
        FileIndex fileIndex = new FileIndex();
        fileIndex.setFileName(fileName);
        fileIndex.setRelativePath(relativePath);
        fileIndex.setModifiedDate(modifiedDate);
        fileIndex.setDir(file.isDirectory());
        fileIndex.setSearchName(fileName.toLowerCase());
        return fileIndex;
    }

    @Override
    public File indexToFileObject(FileIndex index) {
        return getFileStore().getFile(index.getRelativePath());
    }


}
