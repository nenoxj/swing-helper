package cn.note.swing.core.view.frame;

import cn.hutool.core.lang.Console;
import cn.note.swing.core.lifecycle.DestroyManager;
import cn.note.swing.core.view.loading.SimpleLoading;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * 自定义jFrame
 */
public class CustomJFrame extends JFrame {


    private static boolean closing = false;


    public CustomJFrame() {
    }

    /**
     * @param e 重写关闭事件
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (!closing) {
                closing = true;
                this.setGlassPane(new SimpleLoading("正在保存"));
                // 不允许操作
                this.setEnabled(false);
                Console.log("关闭前保存其他.....");
                if (DestroyManager.getDestroyEventCount() > 0) {
                    // 显示关闭loading, 执行回调, 关闭窗口
                    this.getGlassPane().setVisible(true);
                    DestroyManager.destroyAll(() -> {
                        super.processWindowEvent(e);
                    });
                } else {
                    super.processWindowEvent(e);
                }
            }

        } else {
            super.processWindowEvent(e);
        }
    }

}
