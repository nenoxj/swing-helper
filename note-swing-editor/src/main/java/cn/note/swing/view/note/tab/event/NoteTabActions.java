package cn.note.swing.view.note.tab.event;

import cn.note.swing.core.event.key.KeyStrokeAction;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * 选项卡工作集合
 * @author: jee
 */
public enum NoteTabActions implements KeyStrokeAction {

    /**
     * close_now
     */
    CLOSE_NOW("close_now", "关闭当前", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK)),
    /**
     * close_all
     */
    CLOSE_ALL("close_all", "关闭所有", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
    /**
     * close_other
     */
    CLOSE_OTHER("close_other", "关闭其他", null),
    /**
     * close_left
     */
    CLOSE_LEFT("close_left", "关闭左侧", null),

    /**
     * close_right
     */
    CLOSE_RIGHT("close_right", "关闭右侧", null),
    ;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;


    /**
     * 按键
     */
    private KeyStroke keyStroke;

    NoteTabActions(String name, String description, KeyStroke keyStroke) {
        this.name = name;
        this.description = description;
        this.keyStroke = keyStroke;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public KeyStroke getKeyStroke() {
        return this.keyStroke;
    }

}
