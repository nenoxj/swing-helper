package cn.note.service.toolkit.filewatch;

import cn.hutool.log.StaticLog;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jee
 * @description: 文件观察服务
 * <p>
 * + 定义轮询时间 和文件过滤器
 * + 指定监听目录
 * + 自定义文件监听动作
 * <pre>
 * // 自定义文件监听事件
 * FileWatchAdapter fileWatchAdapter = new FileWatchAdapter(true);
 * List<FileAlterationListener> listeners = CollUtil.newArrayList(fileWatchAdapter);
 *
 * // 创建文件监听
 * FileWatchService monitorFile = new FileWatchService.WatchBuilder().dirPath(dirPath)
 * .listeners(listeners).build();
 *
 * monitorFile.start();
 *
 * // 自定义频率和过滤器
 * * FileWatchService monitorFile = new FileWatchService.WatchBuilder(3,FileFilterUtils.suffixFileFilter(".txt")).dirPath(dirPath)
 *  * .listeners(listeners).build();
 *
 *
 * </pre>
 * <p>
 * 指定文件过滤监听示例
 * <pre>
 *
 *         // txt过滤器
 *         IOFileFilter filefilter = FileFilterUtils.suffixFileFilter(".txt");
 *         // 子目录的txt后缀
 *         IOFileFilter subFilefilter = FileFilterUtils.or(FileFilterUtils.directoryFileFilter(), filefilter);
 *         //根目录和子目录变化
 *         IOFileFilter filter = FileFilterUtils.or(filefilter, subFilefilter);
 *
 *         // 创建文件监听
 *         FileWatchService monitorFile = new FileWatchService.WatchBuilder(5, filter).dirPath(dirPath)
 *                 .listeners(listeners).build();
 *         monitorFile.start();
 *
 * </pre>
 */
public class FileWatchService {

    /*监听目录*/
    private final File fileDir;

    /*循环周期*/
    private long cycleTime;

    /*监听操作*/
    private final List<FileAlterationListener> listeners;

    /*文件过滤器*/
    private final IOFileFilter fileFilter;

    /*监听器*/
    private final FileAlterationMonitor monitor;

    /*监听适配器*/
    private final FileAlterationObserver observer;


    public FileWatchService(WatchBuilder builder) {
        this.fileDir = builder.fileDir;
        this.cycleTime = builder.cycleTime;
        StaticLog.debug("监听目录: {}, 轮询时间: {}ms", fileDir, cycleTime);
        this.listeners = builder.listeners;
        this.fileFilter = builder.fileFilter;
        observer = new FileAlterationObserver(fileDir, fileFilter);
        listeners.forEach(observer::addListener);

        monitor = new FileAlterationMonitor(cycleTime, observer);
    }

    /**
     * 启动目录观察器
     *
     * @throws Exception 实例化observer失败异常
     */
    public void start() throws Exception {
        StaticLog.debug("启动目录:{} 观察器", fileDir);
        monitor.start();
    }

    /**
     * 停止监控
     */
    public void destroy() {
        try {
            if (monitor != null) {
                monitor.stop();
                StaticLog.info("停止目录:{} 观察器", fileDir);
            }
        } catch (Exception e) {
            StaticLog.error(e, "文件停止监控失败");
        }

    }

    /**
     * @author jee
     * @description: builder模式
     */
    public static class WatchBuilder {

        /*轮询时间*/
        private final long cycleTime;

        /*文件拦截器*/
        private final IOFileFilter fileFilter;

        /*文件目录*/
        private File fileDir;

        /*自定义文件监听器*/
        private List<FileAlterationListener> listeners;

        /**
         * 默认3s轮询一次 ,监听整个目录
         */
        public WatchBuilder() {
            this(3);
        }

        /**
         * 自定义轮询时间
         *
         * @param cycleTime 轮询时间
         */
        public WatchBuilder(long cycleTime) {
            this(cycleTime, FileFilterUtils.trueFileFilter());
        }


        /**
         * 自定义轮询时间和文件过滤器
         *
         * @param cycleTime  默认轮询时间,单位秒
         * @param fileFilter 轮询文件过滤器
         */
        public WatchBuilder(long cycleTime, IOFileFilter fileFilter) {
            this.cycleTime = TimeUnit.SECONDS.toMillis(cycleTime);
            this.fileFilter = fileFilter;
            this.listeners = new ArrayList<>();
        }

        /**
         * 设置监听文件目录
         *
         * @param dirPath 目录绝对路径
         * @return WatchBuilder
         * @throws IOException 非目录或目录不存在异常!
         */
        public WatchBuilder dirPath(String dirPath) throws IOException {
            this.fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                throw new FileNotFoundException("not found: " + fileDir.getAbsolutePath());
            }
            if (!fileDir.isDirectory()) {
                throw new IOException("not a directory: " + fileDir.getAbsolutePath());
            }
            return this;
        }

        /**
         * 设置监听器
         *
         * @param listeners 文件监听器集合
         * @return WatchBuilder
         */
        public WatchBuilder listeners(@Nonnull List<FileAlterationListener> listeners) {
            this.listeners.addAll(listeners);
            return this;
        }

        public FileWatchService build() {
            return new FileWatchService(this);
        }
    }
}
