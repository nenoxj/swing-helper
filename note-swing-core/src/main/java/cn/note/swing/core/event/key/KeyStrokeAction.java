package cn.note.swing.core.event.key;

import javax.swing.*;

/**
 * 绑定按键的action
 *
 * @author jee
 */
public interface KeyStrokeAction {


    /**
     * @return action名称
     */
    String getName();

    /**
     * @return action描述
     */
    String getDescription();

    /**
     * @return 按键KeyStroke
     */
    KeyStroke getKeyStroke();
}
