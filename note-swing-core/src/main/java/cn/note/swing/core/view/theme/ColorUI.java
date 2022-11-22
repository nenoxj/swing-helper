package cn.note.swing.core.view.theme;

import cn.note.swing.core.view.theme.ThemeColor;

import java.awt.*;

/**
 * 定义丰富的颜色
 */
public interface ColorUI {

    /**
     * 透明色
     */
    default Color blankColor() {
        return ThemeColor.noColor;
    }

    /**
     * 蓝色
     */
    default Color primaryColor() {
        return ThemeColor.primaryColor;
    }

    /**
     * 红色
     */
    default Color dangerColor() {
        return ThemeColor.dangerColor;
    }

    /**
     * 红色
     */
    default Color redColor() {
        return ThemeColor.redColor;
    }

    /**
     * 绿色
     */
    default Color greenColor() {
        return ThemeColor.greenColor;
    }


    /**
     * 灰色
     */
    default Color grayColor() {
        return ThemeColor.grayColor;
    }


    /**
     * 背景色
     */
    public Color backgroundColor();


    /**
     * 前景色
     */
    public Color foregroundColor();

    /**
     * 是否深色
     */
    public boolean isDarken();

    /**
     * 悬浮颜色
     */
    default Color hoverColor() {
        if (isDarken()) {
            return ThemeColor.hoverDarkColor;
        } else {
            return ThemeColor.hoverLightColor;
        }
    }
}
