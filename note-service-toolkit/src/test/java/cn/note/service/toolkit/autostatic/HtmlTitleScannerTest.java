package cn.note.service.toolkit.autostatic;

import cn.hutool.core.lang.Console;
import org.junit.Test;

import java.io.IOException;

public class HtmlTitleScannerTest {

    @Test
    public void buildData() throws IOException {
        String dir = "F:\\Person_workspace\\note-single-html";
        SingleData singleData = HtmlTitleScanner.buildData(dir);
        Console.log("构建结果==>\n {}", singleData.getFormatData());
//        singleData.restore();
    }
}