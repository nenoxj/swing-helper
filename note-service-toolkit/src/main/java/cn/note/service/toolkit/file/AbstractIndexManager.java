package cn.note.service.toolkit.file;

import cn.hutool.core.collection.CollUtil;
import cn.note.service.toolkit.filestore.FileStore;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.service.toolkit.filewatch.FileWatchService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件索引管理器
 * 可以用来管理文件基本信息的索引
 *
 * @param <T> 某种文件性质类型的存储管理器
 * @see FileIndexManager
 */
@Getter
@Setter
public abstract class AbstractIndexManager<T> {

    /*文件存储对象*/
    private FileStore fileStore;

    /*文件索引*/
    private Map<String, T> indexes;

    /*是否初始化完成*/
    private boolean initCompleted;


    /* 文件监控*/
    private FileWatchService fileWatchService;


    public AbstractIndexManager(String homeDir) {
        this(new RelativeFileStore(homeDir));
    }


    public AbstractIndexManager(FileStore fileStore) {
        this.fileStore = fileStore;
        this.indexes = new ConcurrentHashMap<>();
    }


    /**
     * 移除文件
     *
     * @param file 文件
     */
    protected void deleteIndex(File file) {
        indexes.remove(file.getAbsolutePath());
    }


    /**
     * 添加文件
     *
     * @param file 文件
     */
    protected void addIndex(File file) {
        String filePath = file.getAbsolutePath();
        indexes.put(filePath, fileToIndexObject(file));
    }


    /**
     * 递归遍历文件和子目录生成索引文件 ,默认所有文件和目录
     */
    public void initialize() throws Exception {
        initialize(FileFilterUtils.trueFileFilter(), null);
    }

    /**
     * 指定文件过滤器,递归遍历文件和子目录生成索引文件
     *
     * @param fileFilter 指定文件过滤器
     */
    public void initialize(IOFileFilter fileFilter) throws Exception {
        initialize(fileFilter, null);
    }


    /**
     * 遍历文件至目录
     *
     * @param fileFilter 文件过滤器
     * @param ignoreDirs 忽略目录
     * @throws Exception 遍历文件时发生IO异常, 创建监听器时发生异常
     */
    public void initialize(IOFileFilter fileFilter, List<String> ignoreDirs) throws Exception {
        AbstractTreeFileVisitor fileVisitor = new AbstractTreeFileVisitor() {
            @Override
            public void addNode(Path path, boolean isDir) {
                File file = path.toFile();
//                if (!file.equals(fileStore.homeDir())) {
                if (!isDir) {
                    addIndex(file);
                }
            }
        };
        fileVisitor.setFileFilter(fileFilter);
        if (ignoreDirs != null) {
            fileVisitor.setIgnoreDirs(ignoreDirs);
        }
        Files.walkFileTree(fileStore.homeDir().toPath(), fileVisitor);

        createFileWatch(fileFilter);
    }


    /**
     * 创建文件监听
     */
    protected void createFileWatch(IOFileFilter fileFilter) throws Exception {
        // 定义拦截动作
        FileAlterationListenerAdaptor fileAlterationListenerAdaptor = new FileAlterationListenerAdaptor() {

            @Override
            public void onFileCreate(final File file) {
                addIndex(file);
            }

            @Override
            public void onFileDelete(final File file) {
                deleteIndex(file);
            }

            @Override
            public void onFileChange(File file) {
                addIndex(file);
            }
        };

        List<FileAlterationListener> listeners = CollUtil.newArrayList(fileAlterationListenerAdaptor);
        // 文件过滤器
        IOFileFilter subFileFilter = FileFilterUtils.or(FileFilterUtils.directoryFileFilter(), fileFilter);
        //根目录和子目录变化
        IOFileFilter filter = FileFilterUtils.or(fileFilter, subFileFilter);

        // 创建文件监听
        fileWatchService = new FileWatchService.WatchBuilder(3, filter).dirPath(fileStore.homeDir().getAbsolutePath())
                .listeners(listeners).build();
        fileWatchService.start();

        this.initCompleted = true;

    }


    /**
     * 销毁
     */
    public void destroy() {
        fileWatchService.destroy();
    }


    /**
     * @param indexContext 索引内容
     * @return 索引集合
     */
    public abstract List<T> searchIndex(String indexContext);


    /**
     * 将文件转文件索引信息
     *
     * @param file 文件
     * @return 文件索引信息
     */
    protected abstract T fileToIndexObject(File file);


    /**
     * 索引对象反向转文件对象
     *
     * @param index 索引对象
     * @return 文件对象
     */
    public abstract File indexToFileObject(T index);


}
