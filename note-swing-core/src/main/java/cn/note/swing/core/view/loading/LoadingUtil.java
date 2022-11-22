package cn.note.swing.core.view.loading;

import cn.note.swing.core.lifecycle.BackgroundWorker;

import javax.swing.*;

public class LoadingUtil {

    /* 进度条loading*/
    private static ProgressLoading progressLoading;

    /* 遮罩loading*/
    private static MaskLoading maskLoading;

    /**
     * 进度条模态框
     *
     * @param title    窗口名称
     * @param runnable 执行事件
     */
    public static void progressLoading(String title, Runnable runnable) {
        if (progressLoading == null) {
            progressLoading = new ProgressLoading();
        }
        BackgroundWorker backgroundWorker = new BackgroundWorker() {
            @Override
            protected Void doInBackground() {
                SwingUtilities.invokeLater(() -> progressLoading.showLoading(title));
                runnable.run();
                return null;
            }

            @Override
            protected void done() {
                progressLoading.hideLoading();
            }
        };
        SwingUtilities.invokeLater(backgroundWorker::execute);

    }


    /**
     * 模态加载框
     *
     * @param content  加载内容
     * @param runnable 执行事件
     */
    public static void maskLoading(String content, Runnable runnable) {
        if (maskLoading == null) {
            maskLoading = new MaskLoading();
        }
        BackgroundWorker backgroundWorker = new BackgroundWorker() {
            @Override
            protected Void doInBackground() {
                SwingUtilities.invokeLater(() -> maskLoading.showLoading(content));
                runnable.run();
                return null;
            }

            @Override
            protected void done() {
                maskLoading.hideLoading();
            }
        };
        SwingUtilities.invokeLater(backgroundWorker::execute);

    }

}
