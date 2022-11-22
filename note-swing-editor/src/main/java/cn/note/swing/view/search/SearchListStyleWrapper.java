package cn.note.swing.view.search;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyActionFactory;
import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.core.view.base.FontBuilder;
import cn.note.swing.core.view.wrapper.TextFieldWrapper;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 搜索样式包裹器
 */
public class SearchListStyleWrapper {

    private static String bgColor = ThemeColor.blackColorHex;
    private static String themeColor = ThemeColor.themeColorHex;
    private static String fgColor = ThemeColor.fontColorHex;


    /**
     * 美化滚动条样式
     */
    public static void createScrollStyle(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        FlatWrapper.decorativeScrollBar(verticalScrollBar, ThemeColor.themeColor, ThemeColor.fontColor);

    }


    public static JXTitledPanel createTitledPanel(String title, Container container) {
        JXTitledPanel titledPanel = new JXTitledPanel();
        titledPanel.setContentContainer(container);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(ThemeColor.fontColor);
        titleLabel.setFont(FontBuilder.getLabelFont(14.f));
        titledPanel.setLeftDecoration(titleLabel);
        return titledPanel;

    }


    /**
     * 创建搜索框样式
     */
    public static void createSearchStyle(JTextField search) {
        String searchStyle = "arc:0;focusWidth:0;borderWidth:0;innerOutlineWidth:0;innerFocusWidth:0;" +
                "background:{};foreground:{};";
        TextFieldWrapper.create(search).prefixIcon(() -> {
            return SvgIconFactory.icon(SvgIconFactory.Common.search, ThemeColor.fontColor);
        })
                .style(searchStyle, bgColor, fgColor)
                .caretColor(ThemeColor.fontColor)
                .build();
    }


    /**
     * 设置List样式
     */
    public static void createListStyle(JList fileContextJList) {
        String contentStyle = "background:{};foreground:{};";
        FlatWrapper.style(fileContextJList, contentStyle, themeColor, fgColor);
    }


    public static int oldSelectedIndex = 0;

    /**
     * 添加搜索移动list
     */
    public static void addSearchMoveListStyle(JDialog dialog, JScrollPane scrollPane, JTextField search, JList<FileContext> fileContextJList, Runnable selectedCall) {
        createScrollStyle(scrollPane);
        createSearchStyle(search);
        createListStyle(fileContextJList);
        search.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int selectIndex = fileContextJList.getSelectedIndex();
                int maxSize = fileContextJList.getModel().getSize() - 1;
                if (e.getKeyCode() == KeyEvent.VK_UP) {

                    if (selectIndex > 0) {
                        fileContextJList.setSelectedIndex(selectIndex - 1);
                    } else {
                        fileContextJList.setSelectedIndex(maxSize);
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (selectIndex < maxSize) {
                        fileContextJList.setSelectedIndex(selectIndex + 1);
                    } else {
                        fileContextJList.setSelectedIndex(0);
                    }
                }

            }


        });


        // 滚动条跟随 list选择而滚动
        fileContextJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = fileContextJList.getSelectedIndex();
                Rectangle bounds = fileContextJList.getCellBounds(index, index);
                if (bounds != null) {
                    Point p = bounds.getLocation();
                    // 降低频率
                    if (Math.abs(index - oldSelectedIndex) >= 3) {
                        scrollPane.getViewport().setViewPosition(p);
                        oldSelectedIndex = index;
                    }
                }


            }
        });


        // esc 隐藏窗口
        KeyActionFactory.bindEscAction(search, new ConsumerAction(e -> {
            dialog.setVisible(false);
        }));


        // 回车事件
        KeyActionFactory.bindEnterAction(search, new ConsumerAction(e -> {
            if (StrUtil.isNotBlank(search.getText())) {
                selectedCall.run();
            }
        }));

        // 双击激活窗口
        fileContextJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectedCall.run();
                }
            }
        });

    }


}
