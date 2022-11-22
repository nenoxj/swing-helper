package cn.note.swing.view.search;

import cn.hutool.core.lang.Console;
import cn.note.service.toolkit.filestore.FileStore;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import cn.note.service.toolkit.filestore.SystemFileManager;
import cn.note.swing.core.util.CmdUtil;
import cn.note.swing.store.NoteConstants;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.util.List;

public class HtmlContextIndexManagerTest {


    public static void main(String[] args) throws Exception {
        SystemFileManager.updateSystemDir2Default();
        FileStore fileStore = new RelativeFileStore(SystemFileManager.SYSTEM_DIR, NoteConstants.NOTE_NAME, NoteConstants.Category.ROOT_NAME);
        FileContextIndexManager fileContextIndexManager = new FileContextIndexManager(fileStore);
        fileContextIndexManager.initialize(FileFilterUtils.suffixFileFilter(NoteConstants.FILE_TYPE));
        CmdUtil.show((cmd) -> {
            if (cmd.equals("m1")) {
                fileContextIndexManager.setSearchType(SearchType.FileName);
            } else if (cmd.equals("m2")) {
                fileContextIndexManager.setSearchType(SearchType.FileContext);
            } else if (cmd.equals("m3")) {
                fileContextIndexManager.setSearchType(SearchType.CodeBlock);
            } else {
                List<FileContext> noteFileIndices = fileContextIndexManager.searchIndex(cmd);
                if (noteFileIndices.size() == 0) {
                    Console.log("未匹配到结果!");
                }
                int i = 1;
                for (FileContext fileContext : noteFileIndices) {
                    Console.log("r: {}, content: \n {}", i, fileContext.getHighMatchText());
                    i++;
                }

            }

        });
    }

}