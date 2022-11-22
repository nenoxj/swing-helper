package cn.note.swing.view.note.editor;

import cn.note.swing.core.event.right.RightPopupListener;
import cn.note.swing.core.view.base.MenuFactory;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * 文本编辑器右键视图
 */
public class RichFileEditorPopupView implements RightPopupListener {

    /* 选项卡视图*/
    private ExtendedEditor extendedEditor;


    /* 右键视图*/
    private JPopupMenu popupMenu;


    public RichFileEditorPopupView(ExtendedEditor extendedEditor) {
        this.extendedEditor = extendedEditor;
    }

    /**
     * 选项卡默认菜单项
     * 默认关闭当前 /其他/所有
     *
     * @return 选项卡的右键菜单视图
     */
    @Override
    public JPopupMenu getPopup(MouseEvent e) {
        if (popupMenu == null) {
            JMenuItem copyImage2Clip = MenuFactory.createKeyStrokeMenuItem(extendedEditor, EditorAction.COPY_IMG_TO_CLIP, null);
            popupMenu = MenuFactory.createPopupMenu(copyImage2Clip);
        }
        return popupMenu;
    }

    @Override
    public JComponent getComponent() {
        return this.extendedEditor;
    }

}
