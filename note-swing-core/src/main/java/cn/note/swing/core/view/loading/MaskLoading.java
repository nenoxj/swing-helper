package cn.note.swing.core.view.loading;

import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.base.FontBuilder;
import cn.note.swing.core.view.modal.ModalFactory;
import cn.note.swing.core.view.icon.PngIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * 遮罩旋转提示
 *
 * @author jee
 */
public class MaskLoading {

    /* 遮罩区 */
    private JDialog maskLoading = null;

    /* 主窗口 */
    private Window window;

    /* 透明度 0-1*/
    private float opacityScale;

    /* 消息内容 */
    private JLabel message;


    public MaskLoading() {
        this(WinUtil.getActiveWindow());
    }

    public MaskLoading(Window window) {
        // 透明度
        this.opacityScale = 0.3F;
        this.window = window;
        createView();
    }


    public void showLoading() {
        showLoading("Loading...");
    }

    public void showLoading(String text) {
        message.setText(text);

        Rectangle bounds = window.getBounds();
        if (!window.isMaximumSizeSet()) {
            // 修复阴影边框距离
            bounds.setLocation(bounds.x + 8, bounds.y);
            bounds.setSize(bounds.width - 16, bounds.height - 8);
        }
        maskLoading.setBounds(bounds);
        maskLoading.setVisible(true);
    }


    public void hideLoading() {
        maskLoading.setVisible(false);
    }

    private void createView() {
        maskLoading = ModalFactory.dialogModal(window, window.getBounds());
        maskLoading.setOpacity(this.opacityScale);
        maskLoading.setModal(true);
        JPanel contentPanel = new JPanel(new MigLayout("insets 0,gap 0", "[grow]", "[grow]"));
        message = new JLabel(PngIconFactory.ICON_LOADING);
        message.setFont(FontBuilder.increaseLabelFont(3));
        message.setForeground(Color.WHITE);
        contentPanel.setBackground(ThemeColor.themeColor);
        contentPanel.add(message, "pos 0.5al 0.5al");
        maskLoading.setContentPane(contentPanel);
    }
}
