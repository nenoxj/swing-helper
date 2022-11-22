package cn.note.swing.core.view.loading;

import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.base.FontBuilder;
import cn.note.swing.core.view.modal.ModalFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * 滚动条加载信息
 *
 * @author jee
 */
public class ProgressLoading {
    /*dialog 加载窗口*/
    private JDialog loadingDialog = null;

    /* 父窗口*/
    private Window window;

    private JLabel title;

    private JProgressBar progressBar;

    public ProgressLoading() {
        this(WinUtil.getActiveWindow());
    }

    public ProgressLoading(Window window) {
        this.window = window;
        createView();
    }

    public void showLoading() {
        showLoading("请稍候");
    }


    public void showLoading(String text) {
        title.setText(text);
        progressBar.setIndeterminate(true);
        loadingDialog.setVisible(true);
    }

    public void hideLoading() {
        loadingDialog.setVisible(false);
        progressBar.setIndeterminate(false);
    }


    private void createView() {
        loadingDialog = ModalFactory.dialogModal(window, 0.2f, 0.08f);
        loadingDialog.setVisible(false);
        loadingDialog.setModal(true);
        title = new JLabel();
        title.setFont(FontBuilder.increaseLabelFont(2));
        title.setForeground(ThemeColor.fontColor);
        progressBar = new JProgressBar();
        progressBar.setForeground(ThemeColor.primaryColor);
        JPanel contentPanel = new JPanel(new MigLayout("gap 0,insets 0 20 10 20", "[grow]", "[grow]"));
        contentPanel.add(title, "wrap");
        contentPanel.add(progressBar, "center,growx");
        contentPanel.setBackground(ThemeColor.themeColor);
        loadingDialog.setContentPane(contentPanel);

    }
}
