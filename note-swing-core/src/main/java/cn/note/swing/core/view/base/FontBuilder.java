package cn.note.swing.core.view.base;

import javax.swing.*;
import java.awt.*;

/**
 * 快速构建字体
 *
 * @author jee
 */
public class FontBuilder {

    public final static Font TASK_PANL_FONT = new Font("Microsoft YaHei UI", Font.PLAIN, 13);
    public final static Font BUTTON_FONT = new Font("Microsoft YaHei UI", Font.PLAIN, 13);


    /**
     * @param size 字体大小float
     * @return 获取size 字号的label字体
     */
    public static Font getLabelFont(float size) {
        return getFont("Label.font", size);
    }

    public static Font increaseLabelFont(int offset) {
        Font font = UIManager.getFont("Label.font");
        return getLabelFont(font.getSize() + offset);
    }


    /**
     * @param size 字体大小float
     * @return 获取size 字号的button字体
     */
    public static Font getButtonFontFont(float size) {
        return getFont("Button.font", size);
    }

    /**
     * 只自定义组件的大小,不去改变字体风格
     *
     * @param fontUI font UI 名称
     * @param size   字体大小float
     * @return 新字体大小
     */
    private static Font getFont(String fontUI, float size) {
        return UIManager.getFont(fontUI).deriveFont(size);
    }


    /**
     * @param size 字体大小float
     * @return 获取size 字号的TextPane字体
     */
    public static Font getTextPaneFont(float size) {
        return getFont("TextPane.font", size);
    }

    /**
     * @param size 字体大小float
     * @return 获取size 字号的TextField字体
     */

    public static Font getTextFieldFont(float size) {
        return getFont("TextField.font", size);
    }

    /**
     * 字体
     * UIManager.put("Button.font",font);
     * UIManager.put("ToggleButton.font",font);
     * UIManager.put("RadioButton.font",font);
     * UIManager.put("CheckBox.font",font);
     * UIManager.put("ColorChooser.font",font);
     * UIManager.put("ToggleButton.font",font);
     * UIManager.put("ComboBox.font",font);
     * UIManager.put("ComboBoxItem.font",font);
     * UIManager.put("InternalFrame.titleFont",font);
     * UIManager.put("Label.font",font);
     * UIManager.put("List.font",font);
     * UIManager.put("MenuBar.font",font);
     * UIManager.put("Menu.font",font);
     * UIManager.put("MenuItem.font",font);
     * UIManager.put("RadioButtonMenuItem.font",font);
     * UIManager.put("CheckBoxMenuItem.font",font);
     * UIManager.put("PopupMenu.font",font);
     * UIManager.put("OptionPane.font",font);
     * UIManager.put("Panel.font",font);
     * UIManager.put("ProgressBar.font",font);
     * UIManager.put("ScrollPane.font",font);
     * UIManager.put("Viewport",font);
     * UIManager.put("TabbedPane.font",font);
     * UIManager.put("TableHeader.font",font);
     * UIManager.put("TextField.font",font);
     * UIManager.put("PasswordFiled.font",font);
     * UIManager.put("TextArea.font",font);
     * UIManager.put("TextPane.font",font);
     * UIManager.put("EditorPane.font",font);
     * UIManager.put("TitledBorder.font",font);
     * UIManager.put("ToolBar.font",font);
     * UIManager.put("ToolTip.font",font);
     * UIManager.put("Tree.font",font);
     */
    public static void helper() {

    }
}
