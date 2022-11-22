package cn.note.swing.core.view.wrapper;

import cn.hutool.core.util.StrUtil;

import javax.annotation.Nonnull;

/**
 * html样式工具类
 * 暂时未考虑 拓展
 *
 * @author jee
 */
public class HtmlBuilder {

    public static String html(String template, @Nonnull Object... params) {
        return format("<html>".concat(template), params);
    }

    /**
     * 红色字体
     */
    public static String danger(@Nonnull Object... params) {
        return format("<font color='#FF4D4F'>{}</font>", params);
    }


    public static String br(@Nonnull Object... params) {
        return format("{}<br/>", params);
    }


    public static String bold(@Nonnull Object... params) {
        return format("<bold>{}</bold>", params);
    }

    /**
     * 格式化文本
     */
    private static String format(@Nonnull String template, Object... params) {
        return StrUtil.format(template, params);
    }
}
