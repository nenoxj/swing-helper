package cn.note.swing;

import cn.note.swing.config.SpringBeanConfig;
import cn.note.swing.core.util.LoggerUtil;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.item.ItemNode;
import cn.note.swing.core.view.item.ItemScanner;
import cn.note.swing.core.view.item.MenuItemView;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.application.SingleFrameApplication;
import org.springframework.util.StopWatch;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author jee
 * 不使用spring框架,创建swing视图 使用默认Jframe 构建
 */
@Slf4j
public class SwingSingleViewApplication extends SingleFrameApplication {
    /**
     *
     */
    private static JFrame frame;

    /**
     * 主视图
     */
    private static MenuItemView menuItemView;

    public static void main(String[] args) throws Exception {
        StopWatch sw = new StopWatch();
        sw.start();
        log.info("start time");
        LoggerUtil.setLogLevelInfo(SwingSingleViewApplication.class);
        launch(SwingSingleViewApplication.class, args);
        sw.stop();
        log.info("gui launch milli time:{}", sw.getTotalTimeMillis());
    }


    @Override
    protected void startup() {
        ThemeFlatLaf.install();
        frame = getMainFrame();
        frame.setMinimumSize(new Dimension(1024, 600));
        frame.setEnabled(false);
        show(frame);
        List<ItemNode> menuItems = ItemScanner.scanAllItemView(SpringBeanConfig.PACKAGE_NAME);
        menuItemView = new MenuItemView(menuItems);
        frame.add(menuItemView.create());
        menuItemView.setDefaultSelectedItem();
    }

    @Override
    protected void configureWindow(Window root) {
        super.configureWindow(root);
        if (root == getMainFrame()) {
            root.pack();
            root.setBounds(SwingCoreUtil.centerScreenRectangle(0.8D, 0.8D));
            root.validate();
        }
    }


    @Override
    protected void ready() {
        log.info("系统加载完成!");
        frame.setEnabled(true);
    }

}
