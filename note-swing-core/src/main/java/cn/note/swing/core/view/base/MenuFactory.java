package cn.note.swing.core.view.base;

import cn.note.swing.core.event.key.KeyStrokeAction;
import cn.note.swing.core.view.icon.SvgIconFactory;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * 菜单工厂
 */
@Slf4j
public class MenuFactory {

    /**
     * @param menuItems 菜单项
     * @return 右键菜单
     */
    public static JPopupMenu createPopupMenu(JMenuItem... menuItems) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setLayout(new MigLayout("wrap 1,w 200,insets 0,gap 0", "[grow]", "[grow]"));
        for (JMenuItem menuItem : menuItems) {
            popupMenu.add(menuItem, "w 100%,h 30");
        }
        return popupMenu;
    }

    public static JMenuItem createKeyStrokeMenuItem(KeyStrokeAction keyStrokeAction, Action action) {
        return createKeyStrokeMenuItem(keyStrokeAction, action, null);
    }

    public static JMenuItem createKeyStrokeMenuItem(JComponent component, KeyStrokeAction keyStrokeAction, String iconPath) {
        String actionName = keyStrokeAction.getName();
        Action action = component.getActionMap().get(actionName);
        return MenuFactory.createKeyStrokeMenuItem(keyStrokeAction, action, iconPath);
    }

    /**
     * @param keyStrokeAction 按键绑定
     * @param action          动作事件
     * @param iconPath        图标路径
     * @return 带按键的菜单项
     */
    public static JMenuItem createKeyStrokeMenuItem(KeyStrokeAction keyStrokeAction, Action action, String iconPath) {
        String actionName = keyStrokeAction.getName();
        JMenuItem item = new JMenuItem();
        item.setAction(action);
        if (iconPath != null) {
            FlatSVGIcon svgIcon = SvgIconFactory.icon(iconPath);
            item.setIcon(svgIcon);
            svgIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> item.getForeground()));
        }
        item.setText(keyStrokeAction.getDescription());
        item.setAccelerator(keyStrokeAction.getKeyStroke());
        return item;
    }

}
