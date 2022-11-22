package cn.note.swing.core.lifecycle;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.log.StaticLog;
import cn.note.swing.core.util.SwingCoreUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 销毁管理器
 *
 * @author jee
 */
public class DestroyManager {

    private static List<Runnable> destroyRuns = new ArrayList<>();


    /**
     * 添加销毁事件
     *
     * @param destroyRun 销毁监听器
     */
    public static void addDestroyEvent(Runnable destroyRun) {
        destroyRuns.add(destroyRun);
    }


    /**
     * @return 获取销毁时间数量
     */
    public static int getDestroyEventCount() {
        return destroyRuns.size();
    }


    /**
     * 线程池关闭,可能存在阻塞UI
     */
    @Deprecated
    public static void destroyAll_old(Runnable call) {

        int destroySize = destroyRuns.size();
        // 计数器
        CountDownLatch cdl = new CountDownLatch(destroySize);

        // 固定线程池
        NamedThreadFactory destroyThread = new NamedThreadFactory("DestroyThread-", false);
        ExecutorService executorService = ExecutorBuilder.create().setCorePoolSize(destroySize).setThreadFactory(destroyThread).build();

        // 计算时间
        StopWatch sw = new StopWatch();
        sw.start();

        destroyRuns.forEach(destroyEvent -> {
            executorService.execute(() -> {
                try {
                    destroyEvent.run();
                } finally {
                    cdl.countDown();
                }

            });
        });

        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            sw.stop();
            sw.getTotalTimeMillis();
            StaticLog.debug("destroy thread toast:{}", sw.getTotalTimeMillis());
            call.run();
        }

    }


    public static void destroyAll() {
        SwingCoreUtil.simpleStopWatch("save&&close", () -> destroyRuns.forEach(Runnable::run));
    }


    /**
     * 销毁所有
     */
    public static void destroyAll(Runnable call) {
        int destroySize = destroyRuns.size();

        if (destroySize == 0) {
            SwingUtilities.invokeLater(call);
            return;
        }


        StopWatch sw = new StopWatch();
        sw.start();

        // 计数器
        CountDownLatch cdl = new CountDownLatch(destroySize);
        BackgroundWorker backgroundWorker = new BackgroundWorker() {

            @Override
            protected Void doInBackground() throws Exception {
                destroyRuns.forEach(e -> {
                    e.run();
                    cdl.countDown();
                });
                return null;
            }

            @Override
            protected void done() {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    StaticLog.error("InterruptedException never or code error!!!", e);
                }
                sw.stop();
                StaticLog.debug("destroy thread toast:{}", sw.getTotalTimeMillis());
                call.run();
            }
        };
        SwingUtilities.invokeLater(backgroundWorker::execute);
    }

}
