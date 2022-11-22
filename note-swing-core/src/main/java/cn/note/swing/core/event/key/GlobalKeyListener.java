package cn.note.swing.core.event.key;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

/**
 * 全局key快捷监听器
 *
 * @author jee
 */
@FunctionalInterface
public interface GlobalKeyListener extends AWTEventListener {

    void callable(KeyEvent ke);

    default void eventDispatched(AWTEvent event) {
        if (event instanceof KeyEvent) {
            KeyEvent ke = (KeyEvent) event;
            callable(ke);
        }
    }
}