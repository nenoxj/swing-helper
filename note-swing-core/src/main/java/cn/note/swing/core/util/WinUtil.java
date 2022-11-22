package cn.note.swing.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.view.icon.PngIconFactory;
import cn.note.swing.core.view.loading.MaskWindowLoading;
import org.jdesktop.swingx.icon.EmptyIcon;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URI;

/**
 * 窗口小工具
 * 文件路径转换
 * 消息弹窗
 *
 * @author jee
 */
public class WinUtil {
    public static String convertFilePath(File path) {
        return path.getAbsolutePath().replaceAll("\\\\", "/");
    }

    /**
     * 转换文件路径
     * \\ -->/
     *
     * @param path 转换文件路径
     * @return \\ -->/
     */
    public static String convertFilePath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    /**
     * 使用JOptionPane 实现弹窗消息
     * 固定的消息标题 : 提示
     * 动态的消息内容
     *
     * @param msg     主消息
     * @param formats 扩展消息,最后被拼接于主消息上
     */
    public static void alert(String msg, Object... formats) {
        msg = StrUtil.format(msg, formats);
        JOptionPane.showMessageDialog(null, msg, "提示", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 使用html br替换 回车字符串
     * 实现可以多行展示的消息弹框
     * alert方法的拓展
     *
     * @param msg     主消息
     * @param formats 扩展消息,最后被拼接于主消息上
     */
    public static void alertMulti(String msg, Object... formats) {
        msg = StrUtil.format(msg, formats);
        msg = StrUtil.replace(msg, "\n", "<br/>");
        msg = StrUtil.format("<html>{}</html>", msg);
        alert(msg);
    }


    /**
     * 默认2s
     *
     * @param frame frame框架
     * @param msg   消息
     */
    public static void layerMsg(JFrame frame, String msg) {
        layerMsg(frame, msg, 2000);
    }

    /**
     * 弹框消息
     *
     * @param frame 窗口
     * @param msg   消息
     * @param time  关闭时间
     */
    public static void layerMsg(JFrame frame, String msg, int time) {
        /*至少500ms*/
        final int showTime = time < 500 ? 500 : time;
        SwingUtilities.invokeLater(() -> {
            MaskWindowLoading loading = new MaskWindowLoading(frame);
            loading.showLoading(msg, PngIconFactory.ICON_LOADING);
            Timer timer = new Timer(showTime, (event) -> loading.hideLoading());
            timer.setRepeats(false);
            timer.start();
        });
    }


    /**
     * 浏览器打开地址
     *
     * @param url 文件地址
     */
    public static void browserUrl(String url) {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                URI uri = new URI(url);
                StaticLog.debug("desktop open url:{}", uri);
                desktop.browse(uri);
            } catch (Exception e) {
                alert("打开异常:" + e.getMessage());
            }
        } else {
            alert("无法打开默认浏览器");
        }
    }

    /**
     * 根据目录位置
     * 调用系统API,进入文件目录
     *
     * @param dirPath 目录
     */
    public static void openDir(String dirPath) {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.open(new File(dirPath));
            } catch (Exception e) {
                alert("打开异常:" + e.getMessage());
            }
        } else {
            alert("无法进入该目录:" + dirPath);
        }
    }

    /**
     * 提示模态框.
     * 用于帮助弹窗
     * 不关注按钮的触发事件
     *
     * @param context 弹窗内容
     * @param title   弹窗标题
     */
    public static void showDialogForHelper(String title, JComponent context) {
        JOptionPane.showOptionDialog(null, context, title, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{}, null);
    }


    /**
     * 自定义消息弹窗
     * 不使用JOptionPane的默认按钮
     * 可以用来实现自定义的更新和添加操作
     * -- 需要自定义按钮,在合适的时候释放JOptionPane
     * JOptionPane.getRootFrame().dispose();
     *
     * @param title   弹窗标题
     * @param context 弹窗内容
     */
    public static void showDialogForCustom(String title, JComponent context) {
        JOptionPane.showOptionDialog(null, context, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
    }

    /**
     * 设置dialog 跟随窗体
     * SwingUtilities.getWindowAncestor(this).dispose();
     *
     * @param component 组件
     * @param title     标题
     * @param context   内容
     */
    public static void showDialogForCustom(JComponent component, String title, JComponent context) {
        JOptionPane optionPane = new JOptionPane(context, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
        JDialog dialog = optionPane.createDialog(SwingUtilities.getWindowAncestor(component), title);
        dialog.setIconImage(PngIconFactory.IMAGE_NOTE);
//       MODELESS 可失去焦点的模态框
//        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setVisible(true);
//      默认方法 无法自定义窗口图标
//        JOptionPane.showOptionDialog(SwingUtilities.getWindowAncestor(component), context, title, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);

    }

    /**
     * 显示模态框 并且执行确认回调事件
     * 用于删除操作
     * 可以在确定按钮进行部分的confirmAction操作
     *
     * @param title         弹窗标题
     * @param context       弹窗内容
     * @param messageType   消息类型 JOptionPane
     * @param confirmAction 成功回调
     */
    public static void showDialogOnConfirm(String title, Object context, int messageType, Runnable confirmAction) {
        Icon icon=new EmptyIcon();
        int result = JOptionPane.showConfirmDialog(null, context, title, JOptionPane.OK_CANCEL_OPTION, messageType, icon);
        if (result == JOptionPane.YES_OPTION) {
            if (confirmAction != null) {
                confirmAction.run();
            }
        }
    }


    /**
     * 为dialog 绑定esc 关闭窗口,需要提供窗口内组件
     *
     * @param component dialog 内的组件
     */
    public static void bindEscDialog(@Nonnull JComponent component) {
        KeyActionFactory.bindEscAction(component, new ConsumerAction(e -> {
            Window window = SwingUtilities.getWindowAncestor(component);
            if (window instanceof JDialog) {
                window.dispose();
            }
        }));
    }


    /**
     * 获取当前活跃窗口
     */
    public static Window getActiveWindow() {
        return FocusManager.getCurrentManager().getActiveWindow();
    }


}
