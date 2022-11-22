package cn.note.swing.core.util;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.hutool.core.util.StrUtil;
import com.thoughtworks.xstream.XStream;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * swing工具类
 * 封装常用的一些函数
 *
 * @author jee
 */
@Slf4j
public class SwingCoreUtil {

    /**
     * 常用的序列化包管理
     */
    private static final String[] SERIALIZE_PACKAGES = {
            "cn.note.swing.**",
            "javax.swing.**",
            "java.**",
            "sun.awt.**",
            "org.jdesktop.swingx.**",
            "com.formdev.flatlaf.**",
            "com.kitfox.svg.**",
    };


    /**
     * 创建相对屏幕的点
     *
     * @param xScale x轴比例 <=1
     * @param yScale y轴比例
     * @return 点信息
     */
    public static Point relativeScreenPoint(double xScale, double yScale) {
        Dimension screen = validAndGetScreen(xScale, yScale);
        int width = Double.valueOf(screen.getSize().getWidth() * xScale).intValue();
        int height = Double.valueOf(screen.getSize().getHeight() * yScale).intValue();
        return new Point(width, height);
    }


    /**
     * 相对屏幕居中
     *
     * @param xScale x轴比例
     * @param yScale y轴比例
     * @return 创建相对屏幕居中的区域
     */
    public static Rectangle centerScreenRectangle(double xScale, double yScale) {
        Dimension screen = validAndGetScreen(xScale, yScale);
        double screenWidth = screen.getSize().getWidth();
        double screenHeight = screen.getSize().getHeight();
        int width = Double.valueOf(screenWidth * xScale).intValue();
        int height = Double.valueOf(screenHeight * yScale).intValue();
        int x = Double.valueOf((screenWidth - width) / 2).intValue();
        int y = Double.valueOf((screenHeight - height) / 2).intValue();
        return new Rectangle(x, y, width, height);
    }


    private static Dimension validAndGetScreen(double xScale, double yScale) {
        if (xScale > 1) {
            throw new IllegalStateException("x轴比例小于等于1");
        }
        if (yScale > 1) {
            throw new IllegalStateException("x轴比例小于等于1");
        }
        return Toolkit.getDefaultToolkit().getScreenSize();
    }


    /**
     * 定时运行工具
     *
     * @param milliseconds 时间
     * @param runnable     运行事件
     */
    public static void onceTimer(int milliseconds, Runnable runnable) {
        Timer timer = new Timer(milliseconds, (e) -> SwingUtilities.invokeLater(runnable));
        timer.setRepeats(false);
        timer.start();
    }


    /**
     * 使用xStream实现反序列化
     *
     * @param xmlContent xml内容
     * @return 反序列化对象类
     */
    public static Object deserialize(String xmlContent) {
        XStream xStream = new XStream();
        xStream.allowTypesByWildcard(SERIALIZE_PACKAGES);
        return xStream.fromXML(xmlContent);
    }


    /**
     * 序列化
     *
     * @param component swing组件/ 或java对象
     * @return 序列化xmlContent
     */
    public static String serialize(Object component) {
        XStream xStream = new XStream();
        return xStream.toXML(component);
    }


    /**
     * 删除组件
     *
     * @param component 组件
     */
    public static void removeFromParent(JComponent component) {
        Container oldParent = component.getParent();
        if (oldParent != null) {
            oldParent.remove(component);
            if (oldParent instanceof JComponent) {
                oldParent.revalidate();
            } else {
                // not sure... never have non-j comps
                oldParent.invalidate();
                oldParent.validate();
            }
        }
    }

    /**
     * 查询默认事件
     *
     * @param component 组件
     */
    public static void debugActions(JComponent component) {
        ActionMap map = component.getActionMap();
        Object[] keys = map.allKeys();
        if (keys != null)
            for (Object key : keys) {
                Console.log("{} action key==>{}", component.getClass().getSimpleName(), key);
            }
    }


    /**
     * 转换按键输出
     * 如: ctrl released 1 ==> ctrl+1
     *
     * @param keyStroke 按键绑定
     * @return 转换按键输出
     */
    public static String keyStroke2Str(KeyStroke keyStroke) {
        String acceleratorText = "";
        if (keyStroke != null) {
            int modifiers = keyStroke.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText += "+";
            }
            acceleratorText += KeyEvent.getKeyText(keyStroke.getKeyCode());
        }
        return acceleratorText;
    }


    /**
     * 导出组件为粘贴板图片
     *
     * @param component 组件
     * @param call      回调方法
     */
    public static void exportComp2ClipImage(JComponent component, Runnable call) {
        ClipboardUtil.setImage(exportComp2Image(component));
        SwingUtilities.invokeLater(call);
    }

    /**
     * 将组件转换为图片
     *
     * @param component 组件
     * @return 图片信息
     */
    public static BufferedImage exportComp2Image(JComponent component) {
        Rectangle rec = SwingUtilities.getLocalBounds(component);
        BufferedImage img = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = img.getGraphics();
        component.paint(graphics);
        return img;
    }


    /**
     * 设置渐变颜色
     * Derives a color by adding the specified offsets to the base color's
     * hue, saturation, and brightness values.   The resulting hue, saturation,
     * and brightness values will be constrained to be between 0 and 1.
     *
     * @param base the color to which the HSV offsets will be added
     * @param dH   the offset for hue
     * @param dS   the offset for saturation
     * @param dB   the offset for brightness
     * @return Color with modified HSV values
     */
    public static Color deriveColorHSB(Color base, float dH, float dS, float dB) {
        float[] hsb = Color.RGBtoHSB(
                base.getRed(), base.getGreen(), base.getBlue(), null);

        hsb[0] += dH;
        hsb[1] += dS;
        hsb[2] += dB;
        return Color.getHSBColor(
                hsb[0] < 0 ? 0 : (hsb[0] > 1 ? 1 : hsb[0]),
                hsb[1] < 0 ? 0 : (hsb[1] > 1 ? 1 : hsb[1]),
                hsb[2] < 0 ? 0 : (hsb[2] > 1 ? 1 : hsb[2]));

    }


    /**
     * 计算秒表时间
     *
     * @param work 包裹操作
     */
    public static void simpleStopWatch(String operation, Runnable work) {
        StopWatch sw = new StopWatch();
        sw.start();
        log.info("{} start!!!", operation);
        work.run();
        sw.stop();
        log.info("{} stop!!!,toast time:{}", operation, sw.getTotalTimeMillis());
    }


    /**
     * 模拟长时间操作
     *
     * @param ms 毫秒数
     */
    public static void mockLongTime(long ms) {
        final long l = System.currentTimeMillis();
        while (System.currentTimeMillis() <= l + ms) {
        }
    }


    /**
     * 中文字符串长度
     */
    public static int chineseTextLength(String str) {
        if (StrUtil.isBlank(str)) {
            return 0;
        }

        int strLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese)) {
                strLength += 2;
            } else {
                strLength += 1;
            }
        }
        return strLength;
    }
}
