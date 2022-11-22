package cn.note.swing.view.note.editor.core.richtext.handler.action;

import cn.note.swing.core.event.key.KeyStrokeAction;
import cn.note.swing.core.util.SwingCoreUtil;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @description: 扩展action 枚举
 * key + 描述+ 默认按键
 * @author: jee
 */
public enum EditorAction implements KeyStrokeAction {
    /**
     * 保存
     */
    SAVE("save", "保存", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)),

    /* 查看资源*/
    HTML_SOURCE("source", "HTML源码", KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)),
    /**
     * 测试
     */
    TEST("test", "测试操作", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)),


    /* 默认的ctrl h 为删除字符*/
    DEFAULT_CTRL_H("default_ctrl_h", "默认ctrl_h", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK)),
    /**
     * 粘贴
     */
    PASTE(DefaultEditorKit.pasteAction, "粘贴", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK)),
    /**
     * 复制
     */
    COPY(DefaultEditorKit.copyAction, "复制", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)),
    /**
     * 剪切
     */
    CUT(DefaultEditorKit.cutAction, "剪切", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK)),
    /**
     * 全选
     */
    SELECT_ALL(DefaultEditorKit.selectAllAction, "全选", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK)),
    /**
     * 撤销动作
     */
    UNDO("undo", "撤销", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK)),
    /**
     * 恢复动作
     */
    REDO("redo", "恢复", KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK)),
    /**
     * 加粗
     *
     * @see javax.swing.text.StyledEditorKit.BoldAction
     */
    FONT_BOLD("define-font-bold", "加粗", KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_MASK)),
    /**
     * 斜体
     *
     * @see javax.swing.text.StyledEditorKit.ItalicAction
     */
    FONT_ITALIC("define-font-italic", "斜体", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_MASK)),


    /**
     * 红色标识
     */
    COLOR_DANGER("color_danger", "红色标识", KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, InputEvent.ALT_MASK)),
    /**
     * 黑色标识
     */
    COLOR_DARK("color_dark", "黑色标识", KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, InputEvent.ALT_MASK)),


    /**
     * h1
     */
    H1("font-size-28", "h1字体", KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_MASK)),
    /**
     * h2
     */
    H2("font-size-24", "h2字体", KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_MASK)),
    /**
     * h3
     */
    H3("font-size-18", "h3字体", KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_MASK)),
    /**
     * h4
     */
    H4("font-size-16", "h4字体", KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_MASK)),
    /**
     * h5
     */
    H5("font-size-14", "h5字体", KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_MASK)),
    /**
     * h6
     */
    H6("font-size-12", "h6字体", KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_MASK)),
    /**
     *
     */
    DELETE_LINE("delete-line", "删除行", KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK)),
    /**
     * F1 或者可以直接F1 字符串
     */
    HELPER("helper", "帮助", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)),
    /**
     * ctrl+ shift +v
     */
    HTML_PASTE("html-paste", "粘贴html格式", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)),
    /**
     * find
     */
    FIND("find", "查找", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK)),
    /**
     * 插入代码块 "/"
     */
    INSERT_PRE("InsertPre", "Code代码块", KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.ALT_MASK)),

    /**
     * 取消代码块 "\"
     */
    CANCEL_PRE("CancelPre", "取消代码块", KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, InputEvent.ALT_MASK)),
    /**
     * 插入段落 "/"
     */
    INSERT_P("InsertP", "插入段落", null),


    /**
     * 拷贝文档为图片至粘贴板 "/"
     */
    COPY_IMG_TO_CLIP("CopyImageToClipboard", "分享文档图片", null),


    /**
     * 插入图片从粘贴板 "/"
     */
    INSERT_IMG_FROM_CLIP("InsertImageFromClipboard", "插入图片", null),
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

    EditorAction(String name, String description, KeyStroke keyStroke) {
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


//    /**
//     * @return 返回按键描述
//     */
//    public String getKeyStrokeDesc() {
//        String keyDesc;
//        // 单独按键
//        if (keyStroke.getModifiers() == 0) {
//            keyDesc = keyStroke.toString().replace("pressed", "").trim();
//        } else {
//            // 组合按键
//            keyDesc = keyStroke.toString();
//            keyDesc = keyDesc.replace("pressed", "+");
//            keyDesc = keyDesc.replace("SLASH", "/");
//        }
//        return keyDesc;
//    }

    @Override
    public KeyStroke getKeyStroke() {
        return this.keyStroke;
    }


    /**
     * @return jTable格式二维数组
     */
    public static Object[][] toJTableArray() {
        Object[][] object = new Object[EditorAction.values().length][3];
        int i = 0;
        for (EditorAction action : EditorAction.values()) {
            if (action.getKeyStroke() != null) {
                object[i][0] = SwingCoreUtil.keyStroke2Str(action.getKeyStroke());
                object[i][1] = action.getDescription();
                object[i][2] = action.getName();
                i++;
            }
        }
        return object;
    }

    public static String[] toJTableHeader() {
        return new String[]{"keyboard", "description", "key"};
    }
}
