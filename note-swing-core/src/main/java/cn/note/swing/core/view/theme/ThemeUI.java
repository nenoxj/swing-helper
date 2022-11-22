package cn.note.swing.core.view.theme;

import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.modal.ModalOptButton;
import cn.note.swing.core.view.theme.painter.PainterUI;
import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.core.view.wrapper.StyleWrapper;
import cn.note.swing.core.view.wrapper.TextFieldWrapper;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.ToolTipBalloonStyle;
import net.java.balloontip.utils.ToolTipUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 业务主题色
 */
public interface ThemeUI extends ColorUI, PainterUI, ButtonStyleUI {

    /**
     * 设置滚动条跟随主题
     */
    default void setJScrollPaneUI(JScrollPane scrollPane) {
        FlatWrapper.decorativeScrollPane(scrollPane, backgroundColor(), foregroundColor());
    }


    /**
     * 用于静态提示
     *
     * @param parent  父组件
     * @param tipText 提示文本
     */
    default void balloonTip(JComponent parent, String tipText) {
        BalloonTipStyle style = new ToolTipBalloonStyle(foregroundColor(), backgroundColor());
        JLabel tipLabel = new JLabel(tipText);
        int w = parent.getWidth() / 2;
        int h = parent.getHeight();
        tipLabel.setForeground(backgroundColor());
        final BalloonTip balloon = new BalloonTip(parent, tipLabel, style, BalloonTip.Orientation.RIGHT_BELOW, BalloonTip.AttachLocation.SOUTH, w, h, false);
        ToolTipUtils.balloonToToolTip(balloon, 500, 3000);
    }


    /**
     * 默认的删除窗口
     *
     * @param parent     组件
     * @param title      标题
     * @param deleteText 删除内容文本
     * @param okCall     确定回调
     */
    default void deleteDialogWindow(JComponent parent, String title, String deleteText, Runnable okCall) {
        JPanel deletePanel = new JPanel(new MigLayout("gap 0,w 320", "[grow]", "[grow]"));
        JTextArea deleteTextArea = new JTextArea(deleteText);
        deleteTextArea.setEditable(false);
        deleteTextArea.setLineWrap(true);
        deleteTextArea.setForeground(dangerColor());
        deletePanel.add(new JLabel("删除:"), "top,gaptop 5");
        deletePanel.add(deleteTextArea, "grow,wrap");
        ModalOptButton modalOptButton = new ModalOptButton();
        deletePanel.add(modalOptButton, "growx,span 2");
        modalOptButton.okCall(new ConsumerAction(e -> {
            okCall.run();
            SwingUtilities.getWindowAncestor(deletePanel).dispose();
        }));
        WinUtil.showDialogForCustom(parent, title, deletePanel);

    }


    /**
     * 创建不可编辑,但是光标可以移动的文本域
     */
    default JTextArea unEditCaretVisibleTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(backgroundColor());
        textArea.setForeground(foregroundColor());
        textArea.setCaretColor(foregroundColor());

        // 设置光标可见
        Caret caret = textArea.getCaret();
        caret.setBlinkRate(0);
        caret.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                caret.setVisible(true);
            }
        });
        return textArea;
    }

    /*------------------textfield------------------------------------------*/

    default JTextField lineInnerTextField(String itemText) {
        return lineInnerTextField(itemText, 1);
    }

    /**
     * 行内文本框 ,默认不可编辑, 编辑时激活边框
     */
    default JTextField lineInnerTextField(String itemText, int borderWidth) {
        // tooltip 与 text 一致
        JTextField item = new JTextField(itemText) {

            @Override
            public String getToolTipText(MouseEvent e) {
                if (SwingCoreUtil.chineseTextLength(this.getText()) < 11) {
                    return null;
                } else {
                    return this.getText();
                }
            }

            @Override
            public Point getToolTipLocation(MouseEvent event) {
                return new Point(this.getInsets().left, this.getHeight());
            }
        };
        StyleWrapper.create()
                .background(blankColor())
                .disabledBackground(blankColor())
                .foreground(primaryColor())
                .borderColor(primaryColor())
                .innerFocusWidth(0)
                .focusColor(primaryColor())
                .focusedBorderColor(primaryColor())
                .disabledBorderColor(blankColor())
                .borderWidth(borderWidth)
                .build(item);
        item.setToolTipText("default");
        item.setCaretColor(foregroundColor());
        item.setDisabledTextColor(foregroundColor());
        item.setEnabled(false);
        item.setCaretPosition(0);
        return item;
    }


    /**
     * 线形 搜索风格
     */
    default JTextField lineSearchTextField() {
        JTextField search = new JTextField();
        FlatSVGIcon searchIcon = SvgIconFactory.icon(SvgIconFactory.Common.search);
        searchIcon.setColorFilter(new FlatSVGIcon.ColorFilter(c -> {
            if (search.isFocusOwner()) {
                return primaryColor();
            } else {
                return foregroundColor();
            }
        }));
        search.setBackground(backgroundColor());
        TextFieldWrapper.create(search)
                .lineStyle(1, foregroundColor(), primaryColor())
                .prefixIcon(searchIcon).build();
        return search;
    }
    /*-------------------------button按钮-------------------------------------------*/

}
