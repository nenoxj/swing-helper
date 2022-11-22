package cn.note.swing.store;

import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;

import javax.swing.*;

/**
 * editor配置信息
 */
public final class NoteConstants {

    /**
     * 配置信息
     */
    public static final String NOTE_CONFIG = "config.json";

    /**
     * 笔记目录
     */
    public static final String NOTE_NAME = ".note";

    /**
     * 备份目录
     */
    public static final String NOTE_BAK_NAME = ".note-bak";

    /**
     * 文件类型
     */
    public static final String FILE_TYPE = ".jnote";


    /**
     * 图片存储目录
     */
    public static final String IMAGE_FOLDER = "note-images";

    /**
     * 笔记图标
     */
    public static final Icon NOTE_ICON = SvgIconFactory.icon(SvgIconFactory.Editor.note, ThemeColor.themeColor);

    /**
     * 目录
     */
    public interface Category {
        /**
         * 根节点名称
         */
        String ROOT_NAME = "默认笔记";
        /**
         * 配置路径
         */
        String SETTING_NAME = "note-category";
    }


    /**
     * 选项卡
     */
    public interface NoteTab {

        /**
         * 配置路径
         */
        String SETTING_NAME = "note-tab";
    }

}
