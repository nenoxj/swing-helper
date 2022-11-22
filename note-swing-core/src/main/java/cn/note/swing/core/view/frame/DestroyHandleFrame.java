package cn.note.swing.core.view.frame;

import cn.note.swing.core.lifecycle.DestroyManager;
import cn.note.swing.core.view.loading.LoadingUtil;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * 关闭拦截Frame
 */
public class DestroyHandleFrame extends JFrame {

    private static boolean closing = false;

    /**
     * @param e 重写关闭事件
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (!closing) {
                closing = true;
                if (DestroyManager.getDestroyEventCount() > 0) {
                    LoadingUtil.progressLoading("保存配置", () -> {
                        DestroyManager.destroyAll();
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
