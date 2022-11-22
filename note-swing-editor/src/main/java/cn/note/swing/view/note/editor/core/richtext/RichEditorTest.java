package cn.note.swing.view.note.editor.core.richtext;

import cn.note.swing.core.event.focus.RequestFocusListener;
import cn.note.swing.core.util.FrameUtil;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.core.view.AbstractMigView;
import cn.note.swing.core.view.base.ButtonFactory;
import cn.note.swing.core.view.base.FontBuilder;
import cn.note.swing.core.view.theme.ThemeColor;
import cn.note.swing.core.view.theme.ThemeFlatLaf;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.handler.image.EditorFileImage;
import cn.note.swing.view.note.editor.core.richtext.handler.search.FindReplaceView;
import com.google.common.base.Enums;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.springframework.util.SerializationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 * 富文本测试
 */
@Slf4j
public class RichEditorTest extends AbstractMigView {
    /**
     * html编辑器
     */
    private ExtendedEditor htmlEditor;

    private JScrollPane textPane;

    private JPanel title;

    private JXTextField titleField;

    private int maxTitleLength;

    private String storeFile;

    @Override
    protected MigLayout defineMigLayout() {
        return new MigLayout("insets 0,gap 0,wrap 1", "[grow]", "[grow]");
    }

    @Override
    protected void init() {
        maxTitleLength = 50;
        storeFile = "D:/test-doc";
        htmlEditor = new ExtendedEditor();
        htmlEditor.setImageSupport(new EditorFileImage(htmlEditor, storeFile));

        textPane = new JScrollPane(htmlEditor);
        textPane.setBorder(BorderFactory.createLineBorder(getBackground()));
        title = createTitlePanel();
    }

    @Override
    protected void render() {
//        addActionTypeEvents();
        JXCollapsiblePane collapsiblePane = defineFindBarCollapsible();
        JLayeredPane layerView = new JLayeredPane();
        layerView.setLayout(new MigLayout("insets 0,gap 0", "[grow]", "[grow]"));
        layerView.add(title, "grow,wrap");
        layerView.setLayer(collapsiblePane, JLayeredPane.MODAL_LAYER);
        layerView.add(collapsiblePane, "w 450,pos 1al 50");
        layerView.add(textPane, "w 100%,h 100%");
        layerView.setBorder(BorderFactory.createLineBorder(ThemeColor.themeColor));
        view.add(layerView, "w 90%,h 90%,center");
        // 使编辑器获取焦点
        htmlEditor.addHierarchyListener(new RequestFocusListener());
    }


    /**
     * @return 创建标题面板
     */
    protected JPanel createTitlePanel() {
        JPanel titleRow = new JPanel(new MigLayout("insets 10 20 10 20,gap 0", "[grow]", "[grow]"));
        titleRow.setBackground(Color.white);
        titleField = new JXTextField("Snippet笔记", ThemeColor.themeColor, Color.white);
        titleField.setPromptFontStyle(24);
        titleField.setBorder(BorderFactory.createEmptyBorder());
        titleField.setFont(FontBuilder.getTextFieldFont(24f));
        titleField.setBackground(Color.white);
        titleField.setColumns(maxTitleLength);
        titleField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String s = titleField.getText();
                if (s.length() >= maxTitleLength) {
                    e.consume();
                }
            }
        });
        titleRow.add(titleField, "w 50%");
        return titleRow;
    }

    /**
     * 可折叠的查找/替换面板
     * Color.decode("#FF6666");
     */
    protected JXCollapsiblePane defineFindBarCollapsible() {
        final JXCollapsiblePane collapsible = new JXCollapsiblePane(JXCollapsiblePane.Direction.DOWN);
        collapsible.setOpaque(false);
        FindReplaceView findBar = new FindReplaceView(htmlEditor);
        collapsible.add(findBar);
        collapsible.setCollapsed(true);
        collapsible.setAnimated(false);

        Action openFindBar = new AbstractActionExt() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapsible.setCollapsed(false);
                findBar.showSearchText(true);
                KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .focusNextComponent(findBar);
            }
        };
        Action closeFindBar = new AbstractActionExt() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapsible.setCollapsed(true);
                findBar.showSearchText(false);
                htmlEditor.requestFocusInWindow();
            }

        };
        htmlEditor.getActionMap().put("find", openFindBar);
        findBar.getActionMap().put("close", closeFindBar);
        return collapsible;
    }


    enum ActionType {
        save, load, clean;

        public JButton toBtn() {
            return ButtonFactory.primaryButton(this.name());
        }
    }

    private void addActionTypeEvents() {
        JPanel panel = new JPanel(new MigLayout());
        ActionTypeListener actionTypeListener = new ActionTypeListener();
        for (ActionType actionType : ActionType.values()) {
            JButton btn = actionType.toBtn();
            panel.add(btn);
            btn.addActionListener(actionTypeListener);
        }
        view.add(panel, "w 100%");
    }

    private class ActionTypeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            ActionType type = Enums.getIfPresent(ActionType.class, command).or(ActionType.clean);
            switch (type) {
                case save:
                    saveData();
                    break;
                case load:
                    loadData();
                    break;
                case clean:
                    htmlEditor.setText("");
                    break;
                default:
                    WinUtil.alert("未知的命令:{}", command);
                    break;
            }
        }

        private void serializationData() {
            File file = FileUtils.getFile(storeFile, getTitle());
            byte[] contents = SerializationUtils.serialize(htmlEditor.getDocument());
            try {
                FileUtils.writeByteArrayToFile(file, contents);
            } catch (IOException e) {
                log.error("save error", e);
            }
        }

        private void saveData() {
            File file = FileUtils.getFile(storeFile, getTitle());
            try {
                FileUtils.write(file, htmlEditor.getSourceEditor().getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadData() {
            File file = FileUtils.getFile(storeFile, getTitle());
            try {
                setTitle(file);
                htmlEditor.refresh();
                htmlEditor.setText(FileUtils.readFileToString(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final String FILE_TYPE = ".jhtml";

    private String getTitle() {
        String title = titleField.getText();
        return title + FILE_TYPE;
    }

    private void setTitle(File file) {
        String name = file.getName().replace(FILE_TYPE, "");
        titleField.setText(name);
    }


    public static void main(String[] args) {
//        Beans.setDesignTime(true);
//        LoggerUtil.setLogLevelInfo(RichEditorTest.class);
        log.info("start");
        ThemeFlatLaf.install();
        log.info("load ui finish");
        FrameUtil.launchTest(new RichEditorTest());
        log.info("show all finish");
    }

}

