package cn.note.swing.core.view.wrapper;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.view.theme.ThemeColor;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

/**
 * FlatLaf 快速包裹类
 *
 * @see FlatClientProperties
 */
public class FlatWrapper {

    public static void style(JComponent component, String template, Object... styles) {
        component.putClientProperty(FlatClientProperties.STYLE, StrUtil.format(template, styles));
    }

    /**
     * FlatSVGIcon.ColorFilter.getInstance().setMapper(color -> HSLColor.toRGB(HSLColor.fromRGB(Color.WHITE)));
     */
    public static void icon(FlatSVGIcon icon, Color color) {
        icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> color));
    }


    public static void buttonRound(AbstractButton button) {
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
    }

    public static void buttonToolBar(AbstractButton button) {
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
    }


    /**
     * 椭圆滚动条
     *
     * @param scrollBar 滚动条
     */
    public static void decorativeScrollBar(JScrollBar scrollBar, Color bgColor, Color fgColor) {
        style(scrollBar, "track:{};thumb:{};width:5;thumbArc:10",
                ThemeColor.toHEXColor(bgColor), ThemeColor.toHEXColor(fgColor));

    }


    /**
     * 设置滚动面板及滚动量
     */
    public static void decorativeScrollPane(JScrollPane scrollPane, Color bgColor, Color fgColor) {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        decorativeScrollBar(verticalScrollBar, bgColor, fgColor);
        // 设置滚动量
        verticalScrollBar.setUnitIncrement(20);
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        decorativeScrollBar(horizontalScrollBar, bgColor, fgColor);
    }


}
