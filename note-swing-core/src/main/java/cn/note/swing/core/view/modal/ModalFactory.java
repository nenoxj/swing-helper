package cn.note.swing.core.view.modal;

import cn.note.swing.core.util.SwingCoreUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 快速构建modal模态框
 */
public class ModalFactory {


    /**
     * 相对屏幕比例的模态框
     *
     * @param window      窗口
     * @param widthScale  相对屏幕 宽度比例
     * @param heightScale 相对屏幕 高度比例
     * @return 模态框
     */
    public static JDialog dialogModal(Window window, double widthScale, double heightScale) {
        return dialogModal(window, SwingCoreUtil.centerScreenRectangle(widthScale, heightScale));

    }


    /**
     * 点击frame任意位置可以关闭的模态框
     *
     * @param window 窗口
     * @param rec    区域信息
     * @return dialog模态框
     */
    public static JDialog dialogModal(Window window, Rectangle rec) {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int) screenSize.getWidth();
//        int height = (int) screenSize.getHeight();
        JDialog dialog = new JDialog(window);
        dialog.setModalityType(Dialog.ModalityType.MODELESS);
        dialog.setUndecorated(true);
        dialog.setLocationRelativeTo(window);
        dialog.setBounds(rec);
        return dialog;
    }


}
