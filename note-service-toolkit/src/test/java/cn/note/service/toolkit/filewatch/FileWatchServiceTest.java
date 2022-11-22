package cn.note.service.toolkit.filewatch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.note.service.toolkit.filestore.SystemFileManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class FileWatchServiceTest {
    private String dirPath;

    @Before
    public void before() {
        SystemFileManager.updateSystemDir2Default();
        dirPath = SystemFileManager.SYSTEM_DIR;
    }


    @Test
    public void simple() throws Exception {

        // 自定义文件监听事件
        FileWatchAdapter fileWatchAdapter = new FileWatchAdapter(true);
        List<FileAlterationListener> listeners = CollUtil.newArrayList(fileWatchAdapter);
        // 创建文件监听
        FileWatchService monitorFile = new FileWatchService.WatchBuilder().dirPath(dirPath)
                .listeners(listeners).build();
        monitorFile.start();

        ThreadUtil.safeSleep(300000);
    }


    @Test
    public void filterSuffix() throws Exception {
        // 自定义文件监听事件
        FileWatchAdapter fileWatchAdapter = new FileWatchAdapter(true);
        List<FileAlterationListener> listeners = CollUtil.newArrayList(fileWatchAdapter);
        // txt过滤器
        IOFileFilter filefilter = FileFilterUtils.suffixFileFilter(".txt");
        // 子目录的txt后缀
        IOFileFilter subFilefilter = FileFilterUtils.or(FileFilterUtils.directoryFileFilter(), filefilter);
        //根目录和子目录变化
        IOFileFilter filter = FileFilterUtils.or(filefilter, subFilefilter);

        // 创建文件监听
        FileWatchService monitorFile = new FileWatchService.WatchBuilder(5, filter).dirPath(dirPath)
                .listeners(listeners).build();
        monitorFile.start();
        ThreadUtil.safeSleep(300000);
    }
}