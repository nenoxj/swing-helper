package cn.note.swing.view.note.editor.core.richtext.handler.undo;

import cn.note.swing.core.event.key.KeyAction;
import cn.note.swing.core.event.key.RegisterKeyAction;
import cn.note.swing.view.note.editor.core.richtext.core.ExtendedEditor;
import cn.note.swing.view.note.editor.core.richtext.handler.action.EditorAction;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

/**
 * @description: 实现撤销和恢复功能
 * @author: jee
 */
@Slf4j
public class UndoActionHandler implements RegisterKeyAction {

    private ExtendedEditor htmlEditor;

    private UndoObserverListener undoObserverListener;

    public UndoActionHandler(ExtendedEditor htmlEditor) {
        this.htmlEditor = htmlEditor;
        undoObserverListener = new UndoObserverListener();
        this.htmlEditor.getDocument().addUndoableEditListener(undoObserverListener);
    }

    public void refresh() {
        if (undoObserverListener != null) {
            log.info("clean all redo/undo actions");
            undoObserverListener.clear();
        }

    }


    /**
     * 绑定撤销事件
     */
    @KeyAction
    private void undoAction() {
        registerKeyAction(EditorAction.UNDO,undoObserverListener.getUndoAction());
    }


    /**
     * 绑定恢复事件
     */
    @KeyAction
    private void redoAction() {
        registerKeyAction(EditorAction.REDO,undoObserverListener.getUndoAction());
    }

    @Override
    public JComponent getComponent() {
        return htmlEditor;
    }
}