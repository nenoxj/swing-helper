package cn.note.service.toolkit.compress;

import cn.hutool.core.collection.CollUtil;
import cn.note.service.toolkit.filestore.RelativeFileStore;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ZipCompressTest {

    private File dir;
    private File zip;
    private File parent;

    @Before
    public void before() {
        parent = new File("F:\\Person_workspace\\sy-project-sets\\websocket");
        dir = FileUtils.getFile(parent, "spring-summary-demo");
        zip = FileUtils.getFile(parent, "spring-summary-demo.zip");
    }

    @Test
    public void simpleCompress() throws IOException {
        ZipCompress.pack(dir);
    }

    @Test
    public void filterCompress() throws IOException {
        RelativeFileStore relativeFileStore = new RelativeFileStore(dir);
        relativeFileStore.setIgnoreDirs(CollUtil.newArrayList("/src/main/resources", "/src/main/resources/**"));
        ZipCompress.pack(relativeFileStore);
    }

    @Test
    public void uncompress2Target() throws IOException {
        ZipCompress.unpack(zip, FileUtils.getFile(parent, "temp"), true);
    }
}