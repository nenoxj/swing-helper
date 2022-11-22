package cn.note.swing.core.view.theme;

import cn.hutool.core.util.StrUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author jee
 * @version 1.0
 */
public final class ThemeColor {

    /*------------common------------------------------------*/
    public static final Color noColor = new Color(255, 255, 255, 0);
    public static final Color hoverDarkColor = new Color(129, 212, 250, 100);
    public static final Color hoverLightColor = new Color(97,97,97,100);
    public static final Color panelColor = UIManager.getColor("Panel.background");
    public static final Color themeColor = UIManager.getColor("App.common.themeColor");
    public static final Color fontColor = UIManager.getColor("App.common.fontColor");
    public static final Color hoverColor = UIManager.getColor("App.common.hoverColor");
    public static final Color hoverBgColor = UIManager.getColor("App.common.hoverBackgroundColor");
    public static final Color blackColor = UIManager.getColor("App.common.blackColor");
    public static final Color whiteColor = UIManager.getColor("App.common.whiteColor");
    public static final Color blueColor = UIManager.getColor("App.common.blueColor");
    public static final Color primaryColor = UIManager.getColor("App.common.primaryColor");
    public static final Color dangerColor = UIManager.getColor("App.common.dangerColor");
    public static final Color greenColor = UIManager.getColor("App.common.greenColor");
    public static final Color yellowColor = UIManager.getColor("App.common.yellowColor");
    public static final Color redColor = UIManager.getColor("App.common.redColor");
    public static final Color grayColor = UIManager.getColor("App.common.grayColor");
    public static final Color taskPaneSelect = UIManager.getColor("TaskPane.selectedForeground");
    public static final Color taskPaneHover = UIManager.getColor("TaskPane.hoverBackground");

    /*-----------------editor-----------------------*/
    public static final Color notFindColor = UIManager.getColor("App.common.notFindColor");
    /*----------------drag========--*/
    public static final Color dragColor = UIManager.getColor("App.common.dragColor");

    /*---------------------16进制----------------------*/
    public static final String noColorHex = toHEXColor(noColor);
    public static final String themeColorHex = toHEXColor(UIManager.getColor("App.common.themeColor"));
    public static final String fontColorHex = toHEXColor(UIManager.getColor("App.common.fontColor"));
    public static final String blackColorHex = toHEXColor(UIManager.getColor("App.common.blackColor"));
    public static final String taskPaneBgHex = toHEXColor(UIManager.getColor("TaskPaneContainer.backgroundPainter"));
    public static final String taskPaneFgHex = toHEXColor(UIManager.getColor("TaskPane.titleForeground"));
    public static final String taskPaneHoverHex = toHEXColor(UIManager.getColor("TaskPane.hoverBackground"));
    public static final String taskPaneSelectHex = toHEXColor(UIManager.getColor("TaskPane.selectedForeground"));


    /**
     * @param color 颜色
     * @return toHex颜色:#263238
     */
    public static String toHEXColor(Color color) {
        String redHex = Integer.toHexString(color.getRed());
        String greenHex = Integer.toHexString(color.getGreen());
        String blueHex = Integer.toHexString(color.getBlue());
        return StrUtil.format("#{}{}{}", redHex, greenHex, blueHex).toUpperCase();
    }
}
