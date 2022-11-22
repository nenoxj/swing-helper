package cn.note.swing.view.note.editor.core.richtext.handler.action;

import cn.hutool.core.swing.clipboard.ClipboardUtil;
import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.key.KeyAction;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.core.util.WinUtil;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdesktop.swingx.JXDialog;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @description: 增强动作事件
 * <p>
 * + 查找
 * + 删除行
 * + 帮助
 * + 粘贴html
 * @author: jee
 */
public class EnhanceActionHandler implements RegisterKeyAction {

    private ExtendedEditor htmlEditor;

    /**
     * 帮助面板
     */
    private JTable helpTable;

    /**
     * 资源面板
     */
    private RTextScrollPane sourcePane;

    public EnhanceActionHandler(ExtendedEditor htmlEditor) {
        this.htmlEditor = htmlEditor;
    }


    @KeyAction("分享图片")
    public void shareImage() {
        registerKeyAction(EditorAction.COPY_IMG_TO_CLIP, new ConsumerAction(e -> {
            Dimension prefSize = htmlEditor.getPreferredSize();
            BufferedImage img = new BufferedImage(prefSize.width, prefSize.height, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = img.getGraphics();
            htmlEditor.paint(graphics);
            graphics.dispose();
            ClipboardUtil.setImage(img);
        }));

    }

    /**
     * 删除行事件
     */
    @KeyAction
    private void bindDeleteLine() {
        registerKeyAction(EditorAction.DELETE_LINE, new ConsumerAction(e -> {
            if (htmlEditor.getDocument().getLength() > 1) {
                htmlEditor.callEditorKitAction(DefaultEditorKit.selectLineAction);
                htmlEditor.replaceSelection("");
                int caretPos = htmlEditor.getCaretPosition();
                try {
                    if (htmlEditor.isLastCaret()) {
                        htmlEditor.getDocument().remove(caretPos - 1, 1);
                    } else {
                        htmlEditor.getDocument().remove(caretPos, 1);
                    }
                    htmlEditor.callEditorKitAction(DefaultEditorKit.endLineAction);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }

            }
        }));
    }


    /**
     * 绑定帮助事件
     * 注册alt+/ 和F1
     */
    @KeyAction
    private void helperAction() {
        registerKeyAction(EditorAction.HELPER, new ConsumerAction(e -> {
            if (helpTable == null) {
                String[] helpColumns = EditorAction.toJTableHeader();
                Object[][] helpValues = EditorAction.toJTableArray();
                helpTable = new JTable(helpValues, helpColumns) {
                    public boolean isCellEditable(int rowIndex, int vColIndex) {
                        return false;
                    }
                };
                helpTable.setShowHorizontalLines(true);
            }
            WinUtil.showDialogForHelper("帮助", new JScrollPane(helpTable));
        }));
    }


    /**
     * html代码
     */
    @KeyAction
    private void htmlSourceAction() {
        registerKeyAction(EditorAction.HTML_SOURCE, new ConsumerAction(e -> {
            if (sourcePane == null) {
                sourcePane = new RTextScrollPane(htmlEditor.getSourceEditor());
            }
            sourcePane.setPreferredSize(new Dimension(600, 600));
            WinUtil.showDialogForHelper("html代码", sourcePane);
        }));
    }

    /**
     * 显示自定义的dialog
     */
    private void showJXDiaLogHelp() {
        JScrollPane helpScrollPane = new JScrollPane(helpTable);
        JXDialog jxDialog = new JXDialog(helpScrollPane);
        Rectangle htmlRectangle = SwingUtilities.getLocalBounds(htmlEditor);
        int x = Double.valueOf(htmlRectangle.getX() + htmlRectangle.getWidth() / 2).intValue();
        int y = Double.valueOf(htmlRectangle.getY() + htmlRectangle.getHeight() / 2).intValue();
        int width = 600;
        int height = 400;
        jxDialog.setBounds(x, y, width, height);
        jxDialog.setVisible(true);
        jxDialog.setFocusable(false);
    }

    /**
     * 绑定html 文本粘贴ctrl+shift+v
     */
    @KeyAction
    private void htmlPasteAction() {
        registerKeyAction(EditorAction.HTML_PASTE, new ConsumerAction(e -> {
            htmlEditor.setAllowPasteHtml(true);
            htmlEditor.paste();
        }));
    }


    /**
     * 查找功能
     */
    @KeyAction()
    private void findAction() {
        registerKeyAction(EditorAction.FIND, new ConsumerAction(e -> htmlEditor.find()));

    }

    @Override
    public JComponent getComponent() {
        return htmlEditor;
    }
}