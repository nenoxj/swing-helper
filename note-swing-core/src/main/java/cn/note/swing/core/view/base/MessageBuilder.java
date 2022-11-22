package cn.note.swing.core.view.base;

import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;

/**
 * 消息弹框组件
 *
 * @author jee
 */
public class MessageBuilder {

    private static int MESSAGE_MILLISECOND = 1200;

    private static boolean ALLOW_MULTI_ROW = true;

    /**
     * 设置消息消息时间
     *
     * @param millisecond 毫秒
     */
    public static synchronized void setMessageDestroyTime(int millisecond) {
        MESSAGE_MILLISECOND = millisecond;
    }

    /**
     * 是否允许多行文本
     *
     * @param allow 是否允许
     */
    public static synchronized void setMessageMulti(boolean allow) {
        ALLOW_MULTI_ROW = allow;
    }

    public static void ok(JComponent component, String message) {
        message(component, SvgIconFactory.Message.ok, message, ThemeColor.greenColor, MESSAGE_MILLISECOND);
    }

    public static void error(JComponent component, String message) {
        message(component, SvgIconFactory.Message.error, message, ThemeColor.redColor, MESSAGE_MILLISECOND);
    }

    public static void warn(JComponent component, String message) {
        message(component, SvgIconFactory.Message.warn, message, ThemeColor.yellowColor, MESSAGE_MILLISECOND);
    }

    public static void message(JComponent component, String iconPath, String messageBody, Color iconColor, int milliseconds) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(component);
        // 透明但是包含阴影边框
        JXPanel messagePanel = new JXPanel(new MigLayout("w 225::,h 35::,insets 0 10 0 0", "[grow]", "[grow]"));
        messagePanel.setBackground(ThemeColor.noColor);
        messagePanel.setBorder(BorderBuilder.createShadowBorder());

        // icon+message
        FlatSVGIcon svgIcon = SvgIconFactory.icon(iconPath, iconColor);
        JLabel message = new JLabel(messageBody, svgIcon, SwingConstants.LEFT);
        // 控制message距离
        JXPanel body = new JXPanel(new MigLayout("insets 0 10 0 0"));
        body.setBackground(Color.WHITE);
        body.add(message, "w 100%,gaptop 10");
        messagePanel.add(body, "grow");

        message(frame, messagePanel, "grow,pos 0.5al 50", milliseconds);
    }

    /**
     * 显示glasspane 提示
     *
     * @param frame        frame窗口
     * @param milliseconds 显示时长
     * @param message      消息窗口
     * @param pos          位置
     */
    public static void message(JFrame frame, JComponent message, String pos, int milliseconds) {
        JPanel messageContainer = new JPanel(new MigLayout("insets 0,gapy 0", "[grow]", ""));
        messageContainer.setOpaque(false);
        messageContainer.add(message, pos);
        SwingUtilities.invokeLater(() -> {
            frame.setGlassPane(messageContainer);
            frame.getGlassPane().setVisible(true);
            SwingCoreUtil.onceTimer(milliseconds, () -> {
                frame.getGlassPane().setVisible(false);
            });
        });
    }
}
