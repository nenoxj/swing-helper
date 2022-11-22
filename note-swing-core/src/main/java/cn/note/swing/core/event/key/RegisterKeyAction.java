package cn.note.swing.core.event.key;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.log.StaticLog;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * 注册按键action
 *
 * @author jee
 */
public interface RegisterKeyAction {

    /**
     * 注册按键动作
     *
     * @param keyStrokeAction 按键绑定
     * @param action          动作事件
     * @param actionStatus    发生状态
     */
    default void registerKeyAction(KeyStrokeAction keyStrokeAction, Action action, KeyActionStatus actionStatus) {
        KeyActionFactory.bindKeyAction(getComponent(), keyStrokeAction.getName(), keyStrokeAction.getKeyStroke(), action, actionStatus);
    }


    /**
     * 注册按键动作 当获取焦点时发生
     *
     * @param keyStrokeAction 按键绑定
     * @param action          动作事件
     */
    default void registerKeyAction(KeyStrokeAction keyStrokeAction, Action action) {
        registerKeyAction(keyStrokeAction, action, KeyActionStatus.WHEN_FOCUSED);
    }


    /**
     * 注册所有绑定@ComponentKeyAction 标记的动作事件
     */
    default void addKeyActions() {
        Class clazz = this.getClass();
        StaticLog.debug("--------add keyActions:  {}  ----", clazz.getName());
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            KeyAction initAction = method.getAnnotation(KeyAction.class);
            if (initAction != null) {
                ReflectUtil.invoke(this, method);
            }
        }
    }

    /**
     * @return 组件
     */
    JComponent getComponent();
}
