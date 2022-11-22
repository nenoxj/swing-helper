package cn.note.swing.store;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.awt.*;

/**
 * 事件通知管理
 */
public class EventBusManager {

    private static EventBusManager eventBusManager;

    /**
     * 事件总线
     */
    @SuppressWarnings("UnstableApiUsage")
    private EventBus eventBus;


    private EventBusManager() {
        eventBus = new AsyncEventBus("noteAsyncBus", cmd -> {
            if (EventQueue.isDispatchThread()) {
                cmd.run();
            } else {
                EventQueue.invokeLater(cmd);
            }
        });
    }

    public static EventBusManager getInstance() {
        if (eventBusManager == null) {
            eventBusManager = new EventBusManager();
        }
        return eventBusManager;
    }

    /**
     * 通过eventBus 广播对象
     *
     * @param event 事件对象
     */
    public void postNotice(Object event) {
        eventBus.post(event);
    }


    /**
     * 注册广播对象
     */
    public void register(Object noticeObject) {
        eventBus.register(noticeObject);
    }
}
