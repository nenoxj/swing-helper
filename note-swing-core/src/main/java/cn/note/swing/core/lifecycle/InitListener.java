package cn.note.swing.core.lifecycle;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.StaticLog;

import java.lang.reflect.Method;

/**
 * 初始化事件监听器
 *
 * @author jee
 */
public interface InitListener {

    /**
     * 注册所有绑定@InitAction 标记的动作事件
     * 默认注册子类
     */
    default void bindEvents() {
        bindEvents(this);
    }

    /**
     * 注册所有绑定@InitAction 标记的动作事件
     *
     * @param obj 包含@InitAction类的对象
     */
    public static void bindEvents(Object obj) {
        Class clazz = obj.getClass();
        StaticLog.debug("--------init {}  events----", clazz.getName());
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            InitAction initAction = method.getAnnotation(InitAction.class);
            if (initAction != null) {
                StaticLog.debug("initActionName :{}", method.getName());
                ReflectUtil.invoke(obj, method);
            }
        }
    }

}
