package cn.note.service.toolkit.filewatch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.system.SystemUtil;
import cn.note.service.toolkit.file.FileIndex;
import cn.note.service.toolkit.file.FileIndexManager;
import cn.note.swing.core.util.CmdUtil;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.util.List;

/**
 * 目录文件扫描测试
 */
public class FileIndexManagerTest {


    public static void main(String[] args) throws Exception {

        String home = SystemUtil.getUserInfo().getCurrentDir() + "/note-service-toolkit";
        FileIndexManager fileIndexManager = new FileIndexManager(home);
        fileIndexManager.initialize(FileFilterUtils.suffixFileFilter(".java"), CollUtil.newArrayList("target"));
        CmdUtil.show((cmd) -> {
            List<FileIndex> noteFileIndices = fileIndexManager.searchIndex(cmd);
            if (noteFileIndices.size() == 0) {
                Console.log("未匹配到结果!");
            }
            int i = 1;
            for (FileIndex fileIndex : noteFileIndices) {
                Console.log("r: {}, relativePath: {},fileName:{}", i, fileIndex.getRelativePath(), fileIndex.getFileName());
                i++;
            }
        });

    }
}