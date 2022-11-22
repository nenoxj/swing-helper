package cn.note.swing.view.note.category.event;

import cn.note.swing.core.event.key.KeyStrokeAction;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @description: 扩展action 枚举
 * key + 描述+ 默认按键
 * @author: jee
 */
public enum CategoryActionEnum implements KeyStrokeAction {

    /**
     * update=f2
     */
    UPDATE("update", "重命名", KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)),
    /**
     * delete =delete
     */
    DELETE("delete", "删除", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)),
    /**
     * createDir
     */
    CREATE_DIR("createDir", "新建分类", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
    /**
     * createFile
     */
    CREATE_FILE("createFile", "新建笔记", KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)),

    /**
     * save
     */
    SAVE("save", "保存", null),
    /**
     * reload
     */
    RELOAD("reload", "重载", null),

    DELETE_ALL("deleteAll", "清空目录", null),
    EXPAND_ALL("expandAll", "展开", null),
    COLLAPSE_ALL("collapseAll", "收起", null),

    ACTIVE_PATH("activePath", "激活路径", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_MASK)),
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

    CategoryActionEnum(String name, String description, KeyStroke keyStroke) {
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
