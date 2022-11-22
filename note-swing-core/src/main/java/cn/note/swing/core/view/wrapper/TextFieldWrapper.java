package cn.note.swing.core.view.wrapper;

import cn.note.swing.core.view.base.BorderBuilder;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Supplier;

/**
 * 文本框样式包裹类
 *
 * @author jee
 */
public class TextFieldWrapper {

    private JTextField textField;


    public static TextFieldWrapper create(JTextField textField) {
        return new TextFieldWrapper(textField);
    }

    public static TextFieldWrapper create() {
        return create(new JTextField());
    }

    private TextFieldWrapper(JTextField textField) {
        this.textField = textField;
    }


    /**
     * 线风格
     *
     * @param borderSize   边框粗细
     * @param defaultColor 默认边框颜色
     * @param activeColor  激活边框颜色
     * @return TextFieldWrapper
     */
    public TextFieldWrapper lineStyle(int borderSize, Color defaultColor, Color activeColor) {
        Border lostBorder = BorderBuilder.createWrapBorder(textField, BorderBuilder.bottomBorder(borderSize, defaultColor));
        Border gainBorder = BorderBuilder.createWrapBorder(textField, BorderBuilder.bottomBorder(borderSize, activeColor));
        textField.setBorder(lostBorder);
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(gainBorder);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(lostBorder);
            }
        });
        return this;
    }


    /**
     * @param placeholder 提示文本
     * @return TextFieldWrapper
     */
    public TextFieldWrapper placeholder(String placeholder) {
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        return this;
    }

    /**
     * 显示清除按钮
     *
     * @return TextFieldWrapper
     */
    public TextFieldWrapper showClear() {
        return showClear(null);
    }

    /**
     * 显示清除按钮
     *
     * @param call 清除回调事件
     * @return TextFieldWrapper
     */
    public TextFieldWrapper showClear(Runnable call) {
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        if (call != null) {
            textField.putClientProperty(FlatClientProperties.TEXT_FIELD_CLEAR_CALLBACK, call);
        }
        return this;
    }

    /**
     * 添加前缀组件
     *
     * @param component 后缀组件
     * @return TextFieldWrapper
     */
    public TextFieldWrapper prefix(JComponent component) {
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, component);
        return this;
    }


    /**
     * 示例:
     * <pre>
     *   TextFieldWrapper.create(search).prefix(() -> {
     *    Color iconColor = ThemeColor.fontColor;
     *    return SvgIconFactory.icon(SvgIconFactory.Common.search, iconColor);
     * });
     * </pre>
     *
     * @param prefixIcon 自定义图标生成方法
     * @return TextFieldWrapper
     */
    public TextFieldWrapper prefixIcon(Supplier<FlatSVGIcon> prefixIcon) {
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, prefixIcon.get());
        return this;
    }


    public TextFieldWrapper prefixIcon(FlatSVGIcon svgIcon) {
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, svgIcon);
        return this;
    }
    /**
     * 添加后缀组件
     *
     * @param component 后缀组件
     * @return TextFieldWrapper
     */
    public TextFieldWrapper suffix(JComponent component) {
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, component);
        return this;
    }

    /**
     * 示例:
     * <pre>
     *   TextFieldWrapper.create(search).suffix(() -> {
     *    Color iconColor = ThemeColor.fontColor;
     *    return SvgIconFactory.icon(SvgIconFactory.Common.search, iconColor);
     * });
     * </pre>
     *
     * @param suffixIcon 自定义图标生成方法
     * @return TextFieldWrapper
     */
    public TextFieldWrapper suffixIcon(Supplier<FlatSVGIcon> suffixIcon) {
        textField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, suffixIcon.get());
        return this;
    }


    /**
     * 设置文本框样式
     */
    public TextFieldWrapper style(String template, Object... params) {
        FlatWrapper.style(textField, template, params);
        return this;
    }


    /**
     * 光标颜色
     */
    public TextFieldWrapper caretColor(Color color) {
        textField.setCaretColor(color);
        return this;
    }

    public JTextField build() {
        return textField;
    }
}
