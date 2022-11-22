package cn.note.swing.core.view.item.ui;

import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

/**
 * @description: 自定义菜单主题
 * 自定义menuItem选择的背景和前景色
 * @author: jee
 */
public class CustomMenuUI extends BasicMenuItemUI {

    public CustomMenuUI(Color selectBgColor) {
        super.selectionBackground = selectBgColor;
    }


    public CustomMenuUI(Color selectBgColor, Color selectFgColor) {
        super.selectionBackground = selectBgColor;
        super.selectionForeground = selectFgColor;
    }
}