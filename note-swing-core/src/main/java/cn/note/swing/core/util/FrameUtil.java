package cn.note.swing.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.note.swing.core.lifecycle.LazyEventManager;
import cn.note.swing.core.view.base.BorderBuilder;
import cn.note.swing.core.view.frame.DestroyHandleFrame;
import cn.note.swing.core.view.icon.PngIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import com.formdev.flatlaf.FlatClientProperties;
import org.springframework.util.StopWatch;

import javax.swing.*;
import java.awt.*;

/**
 * frame工具类
 *
 * @author jee
 */
public class FrameUtil {

    /**
     * 状态区域
     */
    private static JPanel statusArea = new JPanel();
    /**
     * 状态
     */
    private static JLabel status = new JLabel("status...");

    /**
     * 主窗口
     */
    private static JFrame mainFrame = createFrame();


    /**
     * 创建frame
     */
    private static JFrame createFrame() {
//        mainFrame = new CustomJFrame();
        mainFrame = new DestroyHandleFrame();
        mainFrame.setIconImage(PngIconFactory.IMAGE_NOTE);
        mainFrame.getRootPane().putClientProperty(FlatClientProperties.USE_WINDOW_DECORATIONS, Boolean.TRUE);
        return mainFrame;
    }


    /**
     * 默认加载状态区域
     *
     * @param comp 组件
     */
    public static void launchTest(final JComponent comp) {
        launchQuick(comp, "Frame Test", false);
    }

    /**
     * 加载class类运行
     *
     * @param clazz swing类
     */
    public static void launchTest(Class<? extends JComponent> clazz) {
        JComponent component = ReflectUtil.newInstance(clazz);
        launchTest(component);
    }

    /**
     * 在class实例化之前创建frame
     *
     * @param clazz
     */
    public static void launchBefore(Class<? extends JComponent> clazz) {
        launchTest(clazz);
    }

    /**
     * 快速运行
     *
     * @param comp       组件
     * @param title      名称
     * @param loadStatus 是否加载状态栏
     */
    public static void launchQuick(final JComponent comp, String title, boolean loadStatus) {
        Container container = mainFrame.getContentPane();
        container.add(comp, BorderLayout.CENTER);
        if (loadStatus) {
            statusArea.setBorder(BorderBuilder.topBorder(1, ThemeColor.themeColor));
            statusArea.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            statusArea.add(status);
            status.setHorizontalAlignment(JLabel.LEFT);
            container.add(statusArea, BorderLayout.SOUTH);
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.getSize().width * 80 / 100;
        int height = screen.getSize().height * 80 / 100;
        //去除任务栏
        launch(mainFrame, title, width, height);
    }

    /**
     * 状态栏5s刷新一次堆栈信息
     *
     * @param comp 组件
     */
    public static void launchDump(final JComponent comp) {
        Timer timer = new Timer(5000, (e) -> SwingUtilities.invokeLater(() -> showStatus(DumpUtil.getDumpInfo(DateUtil.now() + " refresh stack:"))));
        timer.setInitialDelay(200);
        timer.start();
        launchTest(comp);

    }


    /**
     * 加载运行时间
     *
     * @param clazz 运行类
     */
    public static void launchTime(Class<? extends JComponent> clazz) {
        StopWatch sw = new StopWatch();
        sw.start();
        JComponent component = ReflectUtil.newInstance(clazz);
        launchTest(component);
        sw.stop();
        System.out.println("耗时ms:" + sw.getTotalTimeMillis());
    }

    /**
     * 运行
     *
     * @param f     窗口
     * @param title 标题
     * @param w     宽度
     * @param h     高度
     */
    public static void launch(final JFrame f, String title, int w, int h) {
        SwingUtilities.invokeLater(() -> {
            f.setTitle(title);
            f.setSize(w, h);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            // 禁止拖拽并最大化
//            f.setResizable(false);
//            f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            // LazyManager do
            LazyEventManager.initAllLazyEvents();
        });

    }

    /**
     * 设置状态
     *
     * @param s 状态内容
     */
    public static void showStatus(String s) {
        status.setText(s);
    }

    /**
     * @return 返回frame窗口
     */
    public static JFrame getFrame() {
        return mainFrame;
    }


    /**
     * 关闭所有system.out
     * 发布运行
     *
     * @param comp  组件
     * @param title 标题
     */
    public static void launchDeploy(final JComponent comp, String title) {
        System.out.close();
        launchQuick(comp, title, false);
    }


}
