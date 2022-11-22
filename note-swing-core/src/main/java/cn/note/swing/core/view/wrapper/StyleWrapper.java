package cn.note.swing.core.view.wrapper;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.view.theme.ThemeColor;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * https://www.formdev.com/flatlaf/components/
 * <p>
 * <p>
 * 字符串样式
 * "foreground: #fff;  background: {};focusedBackground:{};borderColor: {};hoverBackground: {};hoverBorderColor:{};focusedBorderColor:{};"
 * 使用StyleWrapper的替换
 *
 * @author jee
 * @version 1.0
 */
public class StyleWrapper {

    private Map<String, Object> style;

    private StyleWrapper() {
        this.style = new HashMap<>();
    }

    public static StyleWrapper create() {
        return new StyleWrapper();
    }

    /**
     * @param color 指定前景颜色(字体颜色)
     * @return StyleWrapper
     */
    public StyleWrapper foreground(Color color) {
        style.put("foreground", color);
        return this;
    }

    /**
     * @param color 指定背景颜色
     * @return StyleWrapper
     */
    public StyleWrapper background(Color color) {
        style.put("background", color);
        return this;
    }

    /**
     * @param color 指定禁用时背景颜色
     * @return StyleWrapper
     */
    public StyleWrapper disabledBackground(Color color) {
        style.put("disabledBackground", color);
        return this;
    }

    /**
     * @param color 指定焦点时背景颜色
     * @return StyleWrapper
     */
    public StyleWrapper focusedBackground(Color color) {
        style.put("focusedBackground", color);
        return this;
    }

    /**
     * @param width 指定边框的宽度
     * @return StyleWrapper
     */
    public StyleWrapper borderWidth(int width) {
        style.put("borderWidth", width);
        return this;
    }

    /**
     * @param color 指定默认边框颜色
     * @return StyleWrapper
     */
    public StyleWrapper borderColor(Color color) {
        style.put("borderColor", color);
        return this;
    }

    /**
     * @param color 指定默认边框颜色
     * @return StyleWrapper
     */
    public StyleWrapper borderColor(Supplier<Color> color) {
        style.put("borderColor", color.get());
        return this;
    }

    /**
     * @param color 禁用时边框颜色
     * @return StyleWrapper
     */
    public StyleWrapper disabledBorderColor(Color color) {
        style.put("disabledBorderColor", color);
        return this;
    }


    /**
     * @param color 指定悬浮时背景颜色
     * @return StyleWrapper
     */
    public StyleWrapper hoverBackground(Color color) {
        style.put("hoverBackground", color);
        return this;
    }

    /**
     * @param color 指定悬浮时背景颜色
     * @return StyleWrapper
     */
    public StyleWrapper hoverBackground(Supplier<Color> color) {
        style.put("hoverBackground", color.get());
        return this;
    }


    /**
     * @param color 指定悬浮时边框的颜色
     * @return StyleWrapper
     */
    public StyleWrapper hoverBorderColor(Color color) {
        style.put("hoverBorderColor", color);
        return this;
    }


    /**
     * @param color 指定焦点时边框的颜色
     * @return StyleWrapper
     */
    public StyleWrapper focusedBorderColor(Color color) {
        style.put("focusedBorderColor", color);
        return this;
    }

    /**
     * @param width 指定焦点时边框的宽度
     * @return StyleWrapper
     */
    public StyleWrapper focusWidth(int width) {
        style.put("focusWidth", width);
        return this;
    }

    /**
     * @param color 指定焦点时颜色
     * @return StyleWrapper
     */
    public StyleWrapper focusColor(Color color) {
        style.put("focusColor", color);
        return this;
    }


    /**
     * @param size 指定焦点时边框的粗细
     */
    public StyleWrapper innerFocusWidth(int size) {
        style.put("innerFocusWidth", size);
        return this;
    }

    /**
     * @param width 指定圆角弧度
     * @return StyleWrapper
     */
    public StyleWrapper arc(int width) {
        style.put("arc", width);
        return this;
    }

    /**
     * 其他使用风格样式
     *
     * @param styleProperty 属性名称
     * @param value         属性值
     * @return StyleWrapper
     */
    public StyleWrapper wrap(String styleProperty, Object value) {
        style.put(styleProperty, value);
        return this;
    }


    /**
     * 打印转换后样式
     */
    public void debugStyle() {
        String result = style.keySet().stream().map(x -> {
            Object value = style.get(x);
            if (value instanceof Color) {
                value = ThemeColor.toHEXColor((Color) value);
            }
            return StrUtil.format("{}={}", x, value);
        }).collect(Collectors.joining(";"));
        Console.log("debug component style:{}", result);
    }


    public void build(JComponent component, boolean debug) {
        if (debug) {
            debugStyle();
        }
        component.putClientProperty(FlatClientProperties.STYLE, style);
        this.style = null;
    }

    public void build(JComponent component) {
        build(component, false);
    }

}
