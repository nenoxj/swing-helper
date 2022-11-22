package cn.note.swing;

import cn.note.service.toolkit.spring.SpringContextUtil;
import cn.note.swing.config.SpringBeanConfig;
import cn.note.swing.core.util.LoggerUtil;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.item.ItemNode;
import cn.note.swing.core.view.item.ItemScanner;
import cn.note.swing.core.view.item.ItemView;
import cn.note.swing.core.view.item.MenuItemView;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import lombok.extern.slf4j.Slf4j;
import org.jdesktop.application.SingleFrameApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StopWatch;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 使用spring框架创建视图
 * @author jee
 */
@Slf4j
public class SpringViewApplication extends SingleFrameApplication {
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
        LoggerUtil.setLogLevelInfo(SpringViewApplication.class);
        launch(SpringViewApplication.class, args);
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringBeanConfig.class);
        String[] loadViews = applicationContext.getBeanNamesForAnnotation(ItemView.class);
        for (String viewName : loadViews) {
            log.debug("<==Spring Register ItemView==>: {}", viewName);
        }
        //  加载视图可能比扫描类慢
        while (menuItemView == null) {
            log.debug("Spring Load Faster,Wait menuItemView !!!");
        }
        if (loadViews.length > 0) {
            log.debug("active first view!");
            menuItemView.setDefaultSelectedItem();
        } else {
            log.warn("未发现任何视图!!!");
        }
        sw.stop();
        frame.setEnabled(true);
        log.info("gui launch milli time:{}", sw.getTotalTimeMillis());
    }


    @Override
    protected void startup() {
        log.debug("初始化系统UI");
        ThemeFlatLaf.install();
        frame = getMainFrame();
        frame.setMinimumSize(new Dimension(1024, 600));
        frame.setEnabled(false);
        show(frame);
        log.info("show gui application");

        List<ItemNode> menuItems = ItemScanner.scanAllItemView(SpringBeanConfig.PACKAGE_NAME, (i) -> {
            return (Component) SpringContextUtil.getBean(i);
        });
        menuItemView = new MenuItemView(menuItems);
        frame.add(menuItemView.create());
        log.info("slider menu load complete");

    }

    @Override
    protected void configureWindow(Window root) {
        super.configureWindow(root);
        log.debug("自定义主视图初始大小!");
        if (root == getMainFrame()) {
            root.pack();
            root.setBounds(SwingCoreUtil.centerScreenRectangle(0.8D, 0.8D));
            root.validate();
        }
    }


    @Override
    protected void ready() {
        log.info("系统加载完成!");
    }

}
