package cn.note.swing.core.view.modal;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.core.view.theme.ThemeColor;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.ColorFunctions;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @description: 为弹框快速自定义按钮
 * <p>
 * 支持按钮enter
 * 支持左右 TAB切换焦点
 * @author: jee
 */
@Slf4j
public class ModalOptButton extends AbstractMigView {

    /* 确认*/
    private JToggleButton ok;
    /* 取消 */
    private JToggleButton cancel;

    private ConsumerAction okAction;

    private ConsumerAction cancelAction;

    @Override
    protected void init() {
        ok = createToggleButton("确定");
        cancel = createToggleButton("取消");
    }

    public void setOkText(String text) {
        ok.setText(text);
    }

    private JToggleButton createToggleButton(String name) {
        final JToggleButton btn = new JToggleButton(name);
        String style = "borderWidth:0;foreground:{};background:{};hoverBackground:{};font:bold;";
        String fgColor = ThemeColor.toHEXColor(ThemeColor.fontColor);
        String bgColor = ThemeColor.toHEXColor(ThemeColor.blueColor);
        String focusedColor = ThemeColor.toHEXColor(ThemeColor.primaryColor);
        String hoverBgColor = ThemeColor.toHEXColor(ColorFunctions.darken(ThemeColor.blueColor, 0.1f));
        style = StrUtil.format(style, fgColor, bgColor, hoverBgColor, focusedColor);
        btn.putClientProperty(FlatClientProperties.STYLE, style);
        btn.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                btn.setBackground(ThemeColor.primaryColor);
                btn.setForeground(ThemeColor.fontColor);
            }

            @Override
            public void focusLost(FocusEvent e) {
                btn.setBackground(ThemeColor.blueColor);
                btn.setForeground(ThemeColor.fontColor);
            }
        });
        return btn;
    }

    /**
     * 定义migLayout布局
     *
     * @return migLayout布局
     */
    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("", "[grow]", "[grow]");
    }

    /**
     * render视图
     */
    @Override
    protected void render() {
        view.add(cancel, "east,gapright 5");
        view.add(ok, "east,gapright 5");

        //ESC 关闭当前弹窗
        WinUtil.bindEscDialog(this);

        // 取消默认关闭当前弹窗
        cancelAction = new ConsumerAction(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
        });

        cancel.addActionListener(cancelAction);
        KeyActionFactory.bindEnterAction(cancel, cancelAction);

        // 左右键仅仅切换按钮
        ok.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                toggleFocus(keyCode);
            }
        });

        cancel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                toggleFocus(keyCode);
            }
        });

        // 注册左右键
//        KeyStroke VK_LEFT = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
//        KeyStroke VK_RIGHT = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
//        KeyStroke VK_TAB = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
//        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(
//                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
//                CollUtil.newHashSet(VK_LEFT));
//
//        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(
//                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
//                CollUtil.newHashSet(VK_RIGHT, VK_TAB));

    }

    private void toggleFocus(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            if (ok.isFocusOwner()) {
                cancel.requestFocus();
            } else {
                ok.requestFocus();
            }
        }
    }


    /**
     * ok回调
     *
     * @param consumerAction 回调事件
     */
    public void okCall(@Nonnull ConsumerAction consumerAction) {
        okAction = consumerAction;
        ok.addActionListener(consumerAction);
        KeyActionFactory.bindEnterAction(ok, okAction);

    }


    /**
     * cancel回调
     *
     * @param consumerAction 取消事件
     */
    public void cancelCall(@Nonnull ConsumerAction consumerAction) {
        cancelAction = consumerAction;
    }


    public static void main(String[] args) {
        ThemeFlatLaf.install();
        FrameUtil.launchTest(ModalOptButton.class);
    }
}
