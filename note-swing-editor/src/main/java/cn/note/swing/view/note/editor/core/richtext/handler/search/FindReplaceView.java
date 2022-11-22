package cn.note.swing.view.note.editor.core.richtext.handler.search;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.lifecycle.InitAction;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.icon.SvgIconFactory;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.core.view.theme.ThemeIcon;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.JXFindPanel;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.search.Searchable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @description: 扩展ExendedEditor查找替换bar
 * @author: jee
 * @see org.jdesktop.swingx.JXFindBar
 */
@Slf4j
public class FindReplaceView extends JXFindPanel {
    /**
     * 查找背景色
     */
    private Color previousBackgroundColor;

    /**
     * 查找前景色
     */
    private Color previousForegroundColor;

    /**
     * 未找到背景色
     */
    private Color notFoundBackgroundColor;

    /**
     * 未找到前景色
     */
    private Color notFoundForegroundColor;

    /**
     * 查找下一个
     */
    private JButton findNext;

    /**
     * 查找当前
     */
    private JButton findPrevious;

    /**
     * 关闭
     */
    private JButton close;

    /**
     * 替换开关
     */
    private JButton replaceToggle;


    /**
     * 区分大小写
     */
    private JCheckBox caseSensitiveSelect;

    /**
     * 替换面板
     */
    private JPanel replacePanel;

    /**
     * 替换折叠面板
     */
    private JXCollapsiblePane replaceCollapsible;

    private ExtendedEditor htmlEditor;

    public FindReplaceView(ExtendedEditor htmlEditor) {
        super(htmlEditor.getSearchable());
        this.htmlEditor = htmlEditor;
        this.notFoundBackgroundColor = ThemeColor.notFindColor;
        this.notFoundForegroundColor = ThemeColor.whiteColor;
        // 循环查找
        getPatternModel().setIncremental(true);
        getPatternModel().setWrapping(true);

    }

    @Override
    public void setSearchable(Searchable searchable) {
        super.setSearchable(searchable);
        match();
    }

    /**
     * here: set textfield colors to not-found colors.
     */
    @Override
    protected void showNotFoundMessage() {
        //JW: quick hack around #487-swingx - NPE in setSearchable
        if (searchField == null) return;
        searchField.setForeground(notFoundForegroundColor);
        searchField.setBackground(notFoundBackgroundColor);
    }

    /**
     * here: set textfield colors to normal.
     */
    @Override
    protected void showFoundMessage() {
        //JW: quick hack around #487-swingx - NPE in setSearchable
        if (searchField == null) return;
        searchField.setBackground(previousBackgroundColor);
        searchField.setForeground(previousForegroundColor);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (previousBackgroundColor == null) {
            previousBackgroundColor = searchField.getBackground();
            previousForegroundColor = searchField.getForeground();
        } else {
            searchField.setBackground(previousBackgroundColor);
            searchField.setForeground(previousForegroundColor);
        }
    }


    // -------------------- init

    @Override
    protected void initExecutables() {
        getActionMap().put(JXDialog.CLOSE_ACTION_COMMAND,
                createBoundAction(JXDialog.CLOSE_ACTION_COMMAND, "cancel"));
        super.initExecutables();
    }

    @Override
    protected void bind() {
        super.bind();
        // esc退出
        searchField.addActionListener(getAction(JXDialog.EXECUTE_ACTION_COMMAND));
        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(stroke,
                JXDialog.CLOSE_ACTION_COMMAND);


        /*关闭 下一个 上一个*/
        close.addActionListener((e) -> triggerAction(JXDialog.CLOSE_ACTION_COMMAND));
        findNext.addActionListener(e -> triggerAction(FIND_NEXT_ACTION_COMMAND));
        findPrevious.addActionListener(e -> triggerAction(FIND_PREVIOUS_ACTION_COMMAND));

        // 控制显示隐藏 替换工作栏
        replaceToggle.addActionListener((e) -> {

            if (replaceCollapsible.isCollapsed()) {
                replaceCollapsible.setCollapsed(false);
                replaceToggle.setIcon(ThemeIcon.expandedIcon);
            } else {
                replaceCollapsible.setCollapsed(true);
                replaceToggle.setIcon(ThemeIcon.collapsedIcon);
            }

        });

        caseSensitiveSelect.addChangeListener((e) -> {
            getPatternModel().setCaseSensitive(caseSensitiveSelect.isSelected());
        });
    }


    /**
     * @param actionName 调用内部事件名称
     */
    private void triggerAction(String actionName) {
        getAction(actionName).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
    }

    @Override
    protected void build() {
        setLayout(new MigLayout("insets 0,gapy 0,aligny top", "[grow]", "[min!]"));
        JPanel panel = new JPanel(new MigLayout("nogrid", "[grow]", "[min!]"));
        panel.setBackground(getBackground());
        panel.add(searchField);
        panel.add(findNext, "w 25!,h 25!");
        panel.add(findPrevious, "w 25!,h 25!");
        panel.add(caseSensitiveSelect, "wrap");
        panel.add(replaceCollapsible);
        add(panel, "grow");
        add(close, "east,gap 2 2 2 90%,w 25!,h 25!");
        add(replaceToggle, "dock west,gapleft 2,w 25!");
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        setBackground(ThemeColor.themeColor);
        caseSensitiveSelect = createCheckBox("区分大小写");
        searchField = createJXTextField("查找");
        findNext = ButtonFactory.roundThemeIconButton(SvgIconFactory.Find.arrowDown, "查找下一个");
        findPrevious = ButtonFactory.roundThemeIconButton(SvgIconFactory.Find.arrowUp, "查找上一个");
        close = ButtonFactory.roundThemeIconButton(ThemeIcon.closeIcon, "关闭");
        replacePanel = createReplacePanel();
        replaceCollapsible = createReplaceCollapsible();
        replaceToggle = ButtonFactory.roundThemeIconButton(0, ThemeIcon.collapsedIcon, "替换");
    }

    private JCheckBox createCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setForeground(ThemeColor.fontColor);
        checkBox.setBackground(ThemeColor.themeColor);
        return checkBox;
    }


    /**
     * @return 替换面板
     */
    private JPanel createReplacePanel() {
        return new AbstractMigView() {
            private JXTextField replaceField;
            private JButton replaceBtn;
            private JButton replaceAll;

            @Override
            protected MigLayout defineMigLayout() {
                return new MigLayout("insets 0,gap 0", "[grow]", "[grow]");
            }

            @Override
            protected void init() {
                replaceField = createJXTextField("替换");
                replaceBtn = new JButton("替换");
                replaceAll = new JButton("替换全部");
            }

            @Override
            protected void render() {
                view.setBackground(ThemeColor.themeColor);
                view.add(replaceField);
                view.add(replaceBtn);
                view.add(replaceAll);
            }

            @InitAction
            private void actions() {
                /*替换*/
                replaceBtn.addActionListener((e) -> {
                    String findText = searchField.getText();
                    String replaceText = replaceField.getText();
                    String selectText = htmlEditor.getSelectedText();
                    if (replaceText != null && StrUtil.equals(findText, selectText)) {
                        htmlEditor.replaceSelection(replaceText);
                    } else {
                        log.info("选择文本不是查找文本,取消替换!");
                    }
                });

                /*全部替换*/
                replaceAll.addActionListener((e) -> {
                    String replaceText = replaceField.getText();
                    if (replaceText != null) {
                        getPatternModel().setIncremental(false);
                        htmlEditor.setCaretPosition(0);
                        while (doSearch() >= 0) {
                            htmlEditor.replaceSelection(replaceText);
                        }
                        getPatternModel().setIncremental(true);
                    }
                });
            }
        };
    }


    /**
     * 默认折叠-非动画的折叠面板
     *
     * @return 替换折叠面板
     */
    private JXCollapsiblePane createReplaceCollapsible() {
        final JXCollapsiblePane collapsible = new JXCollapsiblePane(JXCollapsiblePane.Direction.DOWN);
        collapsible.add(replacePanel);
        collapsible.setCollapsed(true);
        collapsible.setAnimated(false);
        return collapsible;
    }


    /**
     * 15字符长度
     *
     * @param promptText 类似placeHolder
     * @return 文本框
     */
    private JXTextField createJXTextField(String promptText) {
        JXTextField field = new JXTextField(promptText, ThemeColor.themeColor);
        field.setCaretColor(ThemeColor.fontColor);
        field.setColumns(15);
        field.setBackground(ThemeColor.themeColor);
        field.setForeground(ThemeColor.fontColor);
        return field;
    }


    /**
     * @param display 是否设置搜索文本
     */
    public void showSearchText(boolean display) {
        if (display) {
            String text = htmlEditor.getSelectedText();
            if (StrUtil.isNotBlank(text)) {
                this.searchField.setText(text);
            }
            this.searchField.requestFocusInWindow();
        } else {
            this.searchField.setText("");
        }

    }
}
