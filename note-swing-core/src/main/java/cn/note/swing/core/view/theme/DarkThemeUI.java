package cn.note.swing.core.view.theme;

import java.awt.*;

public interface DarkThemeUI extends ThemeUI {


    default Color backgroundColor() {
        return ThemeColor.themeColor;
    }


    default Color foregroundColor() {
        return ThemeColor.panelColor;
    }


    default boolean isDarken() {
        return true;
    }


}
