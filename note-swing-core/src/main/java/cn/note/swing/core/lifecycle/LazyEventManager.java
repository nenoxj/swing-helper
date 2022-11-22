package cn.note.swing.core.lifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * 延迟管理器
 *
 * @author jee
 */
public class LazyEventManager {

    private static List<Runnable> lazyEvents = new ArrayList<>();


    /**
     * 添加事件
     *
     * @param lazyEvent 延迟事件
     */
    public static void addEvent(Runnable lazyEvent) {
        lazyEvents.add(lazyEvent);
    }


    /**
     * 实例化所有延迟事件
     */
    public static void initAllLazyEvents() {
        for (Runnable lazyEvent : lazyEvents) {
            lazyEvent.run();
        }

    }

}
