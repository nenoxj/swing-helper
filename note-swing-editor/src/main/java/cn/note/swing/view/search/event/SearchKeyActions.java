package cn.note.swing.view.search.event;

import cn.note.swing.core.event.key.KeyStrokeAction;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * 搜索面板按键集合
 *
 * @author jee
 */
public enum SearchKeyActions implements KeyStrokeAction {

    /**
     * ctrl +R
     * search_file
     */
    SEARCH_FILE("search_file", "搜索文件", KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
    /**
     * ctrl+h
     * search_context
     */
    SEARCH_CONTEXT("search_context", "搜索内容", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK)),
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

    SearchKeyActions(String name, String description, KeyStroke keyStroke) {
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
