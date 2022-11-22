package cn.note.swing.core.view.theme;

import cn.note.swing.core.util.LoggerUtil;
import cn.note.swing.core.view.filechooser.FileChooserBuilder;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * 对整体框架UI的管理
 */
@Slf4j
public class ThemeFlatLaf {


    static {
        // 是否使用默认的win10 风格
        System.setProperty("flatlaf.useWindowDecorations", "false");
    }

    public static void install() {
        // 禁止动态布局 降低闪烁
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        FlatLaf.registerCustomDefaultsSource("cn.note.themes");
        FlatLightLaf.setup();
        FlatUIDefaultsInspector.install("alt D");
        LoggerUtil.adjustJavaLogLevel();
        // 加载文件
        FileChooserBuilder.install();
    }

}
