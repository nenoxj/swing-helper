package cn.note.swing.core.view.loading;

import javax.swing.*;
import java.awt.*;

/**
 * 遮罩窗口
 *
 * @author jee
 * @description: 在frame使用的dialog实现自定义的loading消息
 */
public class MaskWindowLoading {
    /**
     * 主显示
     */
    private JDialog loadingScreen = null;

    /**
     * 主区域
     */
    private JFrame frame;

    /**
     * 透明度 0-1
     */
    private float opacityScale;

    public MaskWindowLoading(JFrame frame) {
        // 透明度0.5
        opacityScale = 0.5F;
        this.frame = frame;
    }


    public void showLoading(String text, Icon icon) {
        SwingUtilities.invokeLater(() -> {
            loadingScreen = new JDialog(frame, Dialog.ModalityType.DOCUMENT_MODAL);
            loadingScreen.setUndecorated(true);
            Container screenContainer = loadingScreen.getContentPane();
            screenContainer.add(createShowPanel(text, icon));
            loadingScreen.setBounds(0, 0, getLoadingWidth(), getLoadingHeight());
            loadingScreen.setLocationRelativeTo(frame);
            java.awt.Point location = frame.getLocation();
            loadingScreen.setLocation(location.x + 10, location.y + 30);
            loadingScreen.setOpacity(getOpacityScale());
            loadingScreen.setVisible(true);
        });
    }

    /**
     * 透明度
     *
     * @return 透明度
     */
    public float getOpacityScale() {
        return opacityScale;
    }

    /**
     * 创建dialog 显示内容
     *
     * @param text 内容
     * @param icon 图标
     * @return 组合面板
     */
    public JPanel createShowPanel(String text, Icon icon) {
        JPanel splashPanel = new JPanel();
        splashPanel.setLayout(new BorderLayout());
        splashPanel.add(new JLabel(text, icon, JLabel.CENTER), BorderLayout.CENTER);
        return splashPanel;
    }

    /**
     * 获取frame宽度
     *
     * @return 返回frame的宽度
     */
    public int getLoadingWidth() {
        return frame.getContentPane().getWidth();
    }

    /**
     * @return 获取frame高度 -10
     */
    public int getLoadingHeight() {
        return frame.getContentPane().getHeight();
    }

    public void showLoading(String text) {
        this.showLoading(text, null);
    }

    public void showLoading(Icon icon) {
        this.showLoading(null, icon);
    }

    public void hideLoading() {
        if (loadingScreen != null) {
            loadingScreen.dispose();
        }
    }

    /**
     * @param text     文件
     * @param icon     内容
     * @param runnable loading事件
     */
    public void showLoadingCall(String text, Icon icon, Runnable runnable) {
        this.showLoading(text, icon);
        runnable.run();
    }
}
