package cn.note.swing.view.note.category.style;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.view.theme.ThemeColor;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;

/**
 * @description: 自定义tree样式
 * @author: jee
 */
public class CategoryViewUI {

    /**
     * 添加主题色树
     *
     * @param tree 树插件
     */
    public static void addThemeStyle(JTree tree) {
        String style = "icon.expandedColor:{};icon.collapsedColor:{};icon.openColor:{};icon.closedColor:{};";
        String themeColor = ThemeColor.themeColorHex;
        style = StrUtil.format(style, themeColor, themeColor, themeColor, themeColor);
        tree.putClientProperty(FlatClientProperties.STYLE, style);
    }

}
