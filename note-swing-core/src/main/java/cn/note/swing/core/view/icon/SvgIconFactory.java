package cn.note.swing.core.view.icon;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * ColorFunctions.darken/ColorFunctions.lighten  调明亮度 , 光线越强，看上去越亮；光线越弱，看上去越暗
 * ColorFunctions.saturate/ColorFunctions.desaturate   调饱和度
 * ColorFunctions.spin 旋转颜色
 * ColorFunctions.tint  与白色混合，增加颜色鲜艳
 * ColorFunctions.shade 与黑色混合，增加颜色灰度
 * ColorFunctions.luma 计算颜色比例值0-1
 * <p>
 * svg图标管理
 */
public final class SvgIconFactory {

    /**
     * @param path 图标路径
     * @return 20*20的图标
     */
    public static FlatSVGIcon icon(String path) {
        return new FlatSVGIcon(path, 16, 16);
    }


    /**
     * @param path  图标路径
     * @param color 颜色
     * @return 指定颜色的svgIcon
     */
    public static FlatSVGIcon icon(String path, @Nonnull Color color) {
        FlatSVGIcon svgIcon = icon(path);
        svgIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
        return svgIcon;
    }

    public interface Note {
        String icon = "icons/note.svg";
        String editor = "icons/note-editor.svg";
        String plugins = "icons/note-plugins.svg";
        String sync = "icons/note-sync.svg";
        String question = "icons/note-question.svg";
    }


    public interface Find {
        String arrowUp = "icons/find/arrow-up.svg";
        String arrowDown = "icons/find/arrow-down.svg";
        String close = "icons/find/close.svg";
    }

    public interface Menu {
        String formatter = "icons/menu/formatter.svg";
        String helper = "icons/menu/helper.svg";
        String example = "icons/menu/example.svg";
    }

    public interface Category {
        String trace = "icons/category/trace.svg";
        String collapseAll = "icons/category/collapse_all.svg";
    }


    public interface Editor {
        String copy = "icons/editor/copy.svg";
        String cut = "icons/editor/menu-cut.svg";
        String paste = "icons/editor/menu-paste.svg";
        String note = "icons/editor/note.svg";
    }

    public interface Message {
        String ok = "icons/message/check-circle.svg";
        String error = "icons/message/close-circle.svg";
        String warn = "icons/message/info-circle.svg";
    }

    public interface Formatter {
        String code = "icons/formatter/format-code.svg";
        String mysql = "icons/formatter/format-mysql.svg";
        String json = "icons/formatter/format-json.svg";
        String json_structure = "icons/formatter/json-structure.svg";
    }


    public interface Common {
        /**
         * 搜索图标
         */
        String search = "icons/common/search.svg";
        /**
         * 透明图标
         */
        String transparent = "icons/common/transparent.svg";

        String folder = "icons/common/folder.svg";

        String addFile = "icons/common/add-file.svg";

        String setting = "icons/common/setting.svg";

        String copy = "icons/editor/copy.svg";

        String update = "icons/common/update.svg";
        String delete = "icons/common/delete.svg";
        String add = "icons/common/add.svg";
    }


    public interface Tools {
        /**
         * 苹果标签
         */
        String carbon_title = "icons/tools/carbon-title.svg";
    }

}
