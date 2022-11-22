package cn.note.swing.core.view.theme;

import cn.note.swing.core.view.base.BorderBuilder;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.core.view.wrapper.StyleWrapper;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.ColorFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 创建常用的button样式按钮
 */
public interface ButtonStyleUI extends ColorUI {


    default JButton optIconCopy(ActionListener actionListener) {
        return optIconBtn(SvgIconFactory.Common.copy, "复制", actionListener);
    }

    default JButton optIconCancel(ActionListener actionListener) {
        JButton button = optIconBtn(SvgIconFactory.Message.error, redColor(), "取消", actionListener);
        button.setVisible(false);
        return button;
    }

    default JButton optIconConfirm(ActionListener actionListener) {
        JButton button = optIconBtn(SvgIconFactory.Message.ok, greenColor(), "确认", actionListener);
        button.setVisible(false);
        return button;
    }

    default JButton optIconEdit(ActionListener actionListener) {
        return optIconBtn(SvgIconFactory.Common.update, "修改", actionListener);
    }

    default JButton optIconAdd(ActionListener actionListener) {
        return optIconBtn(SvgIconFactory.Common.add, "添加", actionListener);
    }

    default JButton optIconDelete(ActionListener actionListener) {
        return optIconBtn(SvgIconFactory.Common.delete, "删除", actionListener);
    }


    /**
     * 默认与前景色一致
     */
    default JButton optIconBtn(String iconPath, String tooltip, ActionListener actionListener) {
        return optIconBtn(iconPath, foregroundColor(), tooltip, actionListener);
    }

    /**
     * @param iconPath       图标路径
     * @param iconColor      图标颜色
     * @param tooltip        提示
     * @param actionListener 按钮事件
     * @return 操作图标按钮
     */
    default JButton optIconBtn(String iconPath, Color iconColor, String tooltip, ActionListener actionListener) {
        FlatSVGIcon svgIcon = SvgIconFactory.icon(iconPath);
        FlatWrapper.icon(svgIcon, iconColor);
        return optIconBtn(svgIcon, tooltip, actionListener);
    }


    default JButton optIconBtn(Icon icon, String tooltip, ActionListener actionListener) {
        JButton btn = new JButton(icon);
        btn.addActionListener(actionListener);
        btn.setToolTipText(tooltip);
        StyleWrapper.create().borderWidth(0)
                .arc(25)
                .background(backgroundColor())
                .hoverBackground(hoverColor())
                .foreground(foregroundColor())
                .build(btn);
        return btn;
    }

    default JButton tagBtn(String text) {
        JButton btn = new JButton(text);
        StyleWrapper.create()
                .arc(5)
                .background(foregroundColor())
                .hoverBackground(foregroundColor())
                .foreground(backgroundColor())
                .borderWidth(0)
                .build(btn);
        return btn;
    }


    /**
     * 选项卡 图标开关按钮
     *
     * @param iconPath 图标路径
     */
    default JToggleButton tabIconToggleButton(String iconPath) {
        FlatSVGIcon defaultIcon = new FlatSVGIcon(iconPath, 20, 20);
        JToggleButton button = new JToggleButton(defaultIcon);
        defaultIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> {
            Boolean moved = (Boolean) button.getClientProperty("moved");
            if (button.isSelected() || (moved != null && moved)) {
                return foregroundColor();
            } else {
                return ColorFunctions.darken(foregroundColor(), 0.4f);
            }
        }));
        button.setBackground(blankColor());
        button.setBorder(BorderBuilder.leftBorder(2, foregroundColor()));
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.putClientProperty("moved", true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.putClientProperty("moved", false);
            }
        });
        return button;
    }

}
