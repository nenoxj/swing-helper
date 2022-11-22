package cn.note.swing.core.event.key;

import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.lifecycle.DestroyManager;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 快速创建key绑定工作工厂
 * + 创建回车事件
 * + 创建ESC事件
 *
 * @author jee
 */
public class KeyActionFactory {
    /**
     * 回车事件标识
     */
    public static final String ENTER_TAG = "enter_tag";
    /**
     * 回车按键
     */
    public static final KeyStroke ENTER_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

    /**
     * esc 事件标识
     */
    public static final String ESC_TAG = "esc_tag";

    /**
     * ESC按键
     */
    public static final KeyStroke ESC_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

    /**
     * 回车事件, 默认组件获取焦点时触发
     *
     * @param component 组件
     * @param action    动作事件
     */
    public static void bindEnterAction(JComponent component, ConsumerAction action) {
        bindEnterAction(component, action, KeyActionStatus.WHEN_FOCUSED);
    }

    /**
     * 绑定回车事件, 需要指定发生状态
     *
     * @param component    组件
     * @param action       动作事件
     * @param actionStatus 发生状态
     */
    public static void bindEnterAction(JComponent component, ConsumerAction action, KeyActionStatus actionStatus) {
        bindKeyAction(component, ENTER_TAG, ENTER_KEY, action, actionStatus);
    }

    /**
     * 绑定ESC事件 默认组件所在窗口处于激活状态
     *
     * @param component 组件
     * @param action    动作事件
     */
    public static void bindEscAction(JComponent component, ConsumerAction action) {
        bindEscAction(component, action, KeyActionStatus.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * 绑定ESC 事件,需要指定发生状态
     *
     * @param component    组件
     * @param action       动作事件
     * @param actionStatus 发生状态
     */
    public static void bindEscAction(JComponent component, ConsumerAction action, KeyActionStatus actionStatus) {
        bindKeyAction(component, ESC_TAG, ESC_KEY, action, actionStatus);
    }


    /**
     * @param component    组件
     * @param actionName   动作名称
     * @param keyStroke    动作按键
     * @param action       动作事件
     * @param actionStatus 发生状态
     */
    public static void bindKeyAction(JComponent component, String actionName, KeyStroke keyStroke, Action action, KeyActionStatus actionStatus) {
        new ComponentKeyAction.Builder(component).actionName(actionName).keyStroke(keyStroke).actionEvent(action).build(actionStatus);

    }


    /**
     * 快速注册焦点事件
     *
     * @param component       组件
     * @param keyStrokeAction 按键美剧
     * @param action          动作事件
     */
    public static void registerFocusKeyAction(JComponent component, KeyStrokeAction keyStrokeAction, Action action) {
        registerKeyAction(component, keyStrokeAction, action, KeyActionStatus.WHEN_FOCUSED);
    }


    /**
     * 快速注册焦点事件
     *
     * @param component       组件
     * @param keyStrokeAction 按键美剧
     * @param action          动作事件
     */
    public static void registerKeyAction(JComponent component, KeyStrokeAction keyStrokeAction, Action action, KeyActionStatus keyActionStatus) {
        new ComponentKeyAction.Builder(component).actionName(keyStrokeAction.getName()).keyStroke(keyStrokeAction.getKeyStroke()).actionEvent(action).build(keyActionStatus);
    }


    /**
     * 注册全局按键事件
     *
     * @param globalKeyListener 需要实现全局按键
     */
    public static void registerGlobalKey(GlobalKeyListener globalKeyListener) {
        Toolkit.getDefaultToolkit().addAWTEventListener(globalKeyListener, AWTEvent.KEY_EVENT_MASK);
    }


    /**
     * 注册系统按键
     *
     * @param keyStroke 按键值
     * @param runnable  回调方法
     */
    public static void registerSystemKey(KeyStroke keyStroke, Runnable runnable) {
        Provider provider = Provider.getCurrentProvider(true);
        // 添加销毁事件
        DestroyManager.addDestroyEvent(provider::stop);
        provider.register(keyStroke, hotKey -> runnable.run());
    }

}
