package cn.note.swing.store;

import cn.hutool.core.thread.ThreadUtil;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.swing.core.lifecycle.DestroyManager;
import cn.note.swing.view.search.FileContextIndexManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * 笔记索引管理器
 */
@Slf4j
public class NoteIndexManager {


    @Getter
    private FileContextIndexManager fileContextIndexManager;

    public NoteIndexManager(RelativeFileStore noteFileStore) {
        fileContextIndexManager = new FileContextIndexManager(noteFileStore.getRelativeFileStore(NoteConstants.Category.ROOT_NAME));
        initialize();
    }


    /**
     * 异步创建文件索引
     */
    private void initialize() {
        ThreadUtil.execute(() -> {
            try {
                fileContextIndexManager.initialize(FileFilterUtils.suffixFileFilter(NoteConstants.FILE_TYPE));
                log.debug("创建文件索引完成!");
            } catch (Exception e) {
                log.error("初始化文件索引异常==>{}", e);
            }
        });


        // 关闭时销毁
        DestroyManager.addDestroyEvent(() -> {
            fileContextIndexManager.getFileWatchService().destroy();
        });
    }


}
