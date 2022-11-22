package cn.note.swing.core.view.theme;

import java.awt.*;

public interface LightThemeUI extends ThemeUI {


    default Color backgroundColor() {
        return ThemeColor.panelColor;
    }


    default Color foregroundColor() {
        return ThemeColor.themeColor;
    }

    default boolean isDarken() {
        return false;
    }
}
