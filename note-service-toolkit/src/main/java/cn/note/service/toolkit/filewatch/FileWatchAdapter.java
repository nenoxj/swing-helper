package cn.note.service.toolkit.filewatch;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * @author jee
 * @description: 仅仅作为FileAlterationListener的适配器
 * + 增加默认的debug消息支持 new FileWatchAdapter(true)
 * + 默认不开启debug
 *
 * <pre>
 *   // 只监听文件删除动作
 *    FileWatchAdapter fileWatchAdapter = new FileWatchAdapter() {
 *    @Override
 *    public void onFileDelete(File file) {
 *       //do something
 *    }
 *  };
 * </pre>
 * <pre>
 *   // 只监听目录变更动作
 *    FileWatchAdapter fileWatchAdapter = new FileWatchAdapter() {
 *    @Override
 *    public void onDirectoryChange(File file) {
 *        //do something
 *    }
 *
 *  };
 * </pre>
 * @see org.apache.commons.io.monitor.FileAlterationListener
 * @see org.apache.commons.io.monitor.FileAlterationListenerAdaptor
 */
public class FileWatchAdapter implements FileAlterationListener {
    private boolean debug;

    public FileWatchAdapter() {
        this(false);
    }

    public FileWatchAdapter(boolean debug) {
        this.debug = debug;
    }

    private void debugMsg(String str, Object... others) {
        if (debug) {
            StaticLog.debug(StrUtil.format(str, others));
        }
    }

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
        debugMsg("轮询目录:{} 是否变化", fileAlterationObserver.getDirectory().getAbsolutePath());
    }

    @Override
    public void onDirectoryCreate(File file) {
        debugMsg("创建目录:{},path:{}", file.getName(), file.getAbsolutePath());
    }

    @Override
    public void onDirectoryChange(File file) {
        debugMsg("目录内容变更:{},path:{}", file.getName(), file.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File file) {
        debugMsg("删除目录:{},path:{}", file.getName(), file.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        debugMsg("创建文件:{},path:{}", file.getName(), file.getAbsolutePath());
    }

    @Override
    public void onFileChange(File file) {
        debugMsg("文件内容变更:{},path:{}", file.getName(), file.getAbsolutePath());
    }

    @Override
    public void onFileDelete(File file) {
        debugMsg("删除文件:{},path:{}", file.getName(), file.getAbsolutePath());
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
    }


}
