package cn.note.swing.core.view.base;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.core.view.wrapper.StyleWrapper;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.ColorFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ButtonFactory {


    /**
     * 装饰按钮
     * <p>
     * StyleWrapper vs 字符串样式
     * String style = StrUtil.format("foreground: #fff;  background: {};focusedBackground:{};borderColor: {};hoverBackground: {};hoverBorderColor:{};focusedBorderColor:{};", bgColorHex, "#ffffff", bgColorHex, hoverColorHex, hoverColorHex, hoverColorHex);
     * button.putClientProperty(FlatClientProperties.STYLE, style);
     *
     * @param button  按钮
     * @param bgColor 修饰颜色
     * @return 按钮
     */
    public static JButton decorativeButton(JButton button, Color bgColor) {
        Color hoverColor = ColorFunctions.lighten(bgColor, 0.2f);
        StyleWrapper.create().foreground(ThemeColor.fontColor)
                .background(bgColor)
                .focusedBackground(bgColor)
                .borderColor(bgColor)
                .hoverBackground(hoverColor)
                .hoverBorderColor(hoverColor)
                .focusedBorderColor(hoverColor)
                .build(button);

        // 设置focusedBackground 属性无效, 更改focus事件,设置按钮获取焦点时与hover一致
        button.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    /**
     * @param text 文字
     * @return 主色调按钮
     */
    public static JButton primaryButton(String text) {
        return decorativeButton(new JButton(text), ThemeColor.primaryColor);
    }


    /**
     * @param icon 图标
     * @return 圆形图标按钮
     */
    public static JButton primaryRoundButton(Icon icon) {
        JButton button = new JButton(icon);
        FlatWrapper.style(button, "borderColor: #08f; " +
                "background: #08f; " +
                "foreground: #fff;" +
                "disabledBackground: #9e9e9e;" +
                "disabledText:#fff;");
        FlatWrapper.buttonRound(button);
        return button;
    }

    /**
     * 以图标为主题色的运行风格按钮,悬浮时产生
     *
     * @param bgColor    背景颜色
     * @param hoverColor 悬浮颜色 对颜色使用轻亮处理
     * @param icon       图标
     * @param iconColor  图标颜色
     * @return 图标类型运行按钮
     */
    public static JButton runIconButton(Color bgColor, Color hoverColor, FlatSVGIcon icon, Color iconColor) {
        JButton button = new JButton(icon);

        FlatWrapper.style(button, "borderWidth:0;" +
                        "arc:25;" +
                        "background:{};" +
                        "hoverBackground:{};"
                , ThemeColor.toHEXColor(bgColor)
                , ThemeColor.toHEXColor(ColorFunctions.lighten(hoverColor, .2f))
        );
        FlatWrapper.icon(icon, iconColor);
        return button;
    }

    /**
     * @param icon  图标
     * @param color 颜色
     * @return 图标开关按钮
     */
    public static JToggleButton toggleIconButton(FlatSVGIcon icon, Color color) {
        JToggleButton button = new JToggleButton(icon);
        FlatWrapper.style(button, "toolbar.focusWidth:0;");
        FlatWrapper.icon(icon, color);
        FlatWrapper.buttonToolBar(button);
        return button;
    }


    /**
     * @param text    文本
     * @param icon    图标
     * @param bgColor 背景主色
     * @return 面板开关按钮
     */
    public static JToggleButton ghostTaskPaneButton(String text, FlatSVGIcon icon, Color bgColor) {
        JToggleButton button = new JToggleButton(text, icon);
        button.setHorizontalAlignment(SwingUtilities.LEFT);
        String hexBgColor = ThemeColor.toHEXColor(bgColor);
        String style = "borderWidth:0;toolbar.focusWidth:0;arc:0;background:{};foreground:{};hoverBackground:{};selectedForeground:{};selectedBackground:{};";
        style = StrUtil.format(style, hexBgColor, ThemeColor.taskPaneFgHex, ThemeColor.taskPaneHoverHex, ThemeColor.taskPaneSelectHex, hexBgColor);
        button.putClientProperty(FlatClientProperties.STYLE, style);
        if (icon != null) {
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> {
                if (button.isSelected()) {
                    return ThemeColor.taskPaneSelect;
                } else {
                    return ThemeColor.whiteColor;
                }
            }));
        }
        return button;
    }

    /**
     * @param iconPath 图标路径
     * @param tooltip  提示文本
     * @return 圆形按钮
     */
    public static JButton roundThemeIconButton(String iconPath, String tooltip) {
        FlatSVGIcon themeIcon = SvgIconFactory.icon(iconPath, ThemeColor.fontColor);
        return roundThemeIconButton(themeIcon, tooltip);
    }

    /**
     * 圆形主题风格按钮
     *
     * @param icon    图标
     * @param tooltip 提示文本
     * @return 圆形按钮
     */
    public static JButton roundThemeIconButton(Icon icon, String tooltip) {
        return roundThemeIconButton(25, icon, tooltip);
    }

    /**
     * 圆形主题风格按钮
     *
     * @param arc     圆角弧度
     * @param icon    图标
     * @param tooltip 提示文本
     * @return 圆形按钮
     */
    public static JButton roundThemeIconButton(int arc, Icon icon, String tooltip) {
        JButton smallButton = new JButton(icon);
        smallButton.setToolTipText(tooltip);
        StyleWrapper.create().borderWidth(0)
                .arc(arc)
                .foreground(ThemeColor.fontColor)
                .background(ThemeColor.themeColor)
                .hoverBackground(ThemeColor.taskPaneHover)
                .build(smallButton);
        return smallButton;
    }

    /**
     * @param icon       图标路径
     * @param background 背景
     * @return 方型背景图标按钮
     */
    public static JButton ghostIconButton(FlatSVGIcon icon, Color background) {
        JButton smallButton = new JButton(icon);
        StyleWrapper.create().borderWidth(0)
                .arc(0)
                .background(background)
                .hoverBackground(() -> {
                    return ColorFunctions.darken(background, 0.2f);
                })
                .disabledBackground(background)
                .build(smallButton);
        return smallButton;
    }

}
