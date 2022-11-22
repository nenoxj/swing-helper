package cn.note.swing.view.note.editor.core.richtext.handler.undo;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.event.ActionEvent;

/**
 * @description: 撤销/恢复监听器
 * @author: jee
 */
@Slf4j
public class UndoObserverListener implements UndoableEditListener {

    private UndoManager undoManager;

    private UndoAction undoAction;

    private RedoAction redoAction;

    public UndoObserverListener() {
        undoManager = new UndoManager();
        undoAction = new UndoAction();
        redoAction = new RedoAction();
        undoManager.discardAllEdits();
    }


    public AbstractAction getUndoAction() {
        return undoAction;
    }


    public AbstractAction getRedoAction() {
        return redoAction;
    }

    /**
     * 清空所有撤销编辑
     */
    public void clear() {
        undoManager.discardAllEdits();
    }

    /**
     * An undoable edit happened
     *
     * @param e
     */
    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        UndoableEdit undoableEdit = e.getEdit();
        undoManager.addEdit(undoableEdit);
        SwingUtilities.invokeLater(() -> {
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        });
    }

    /**
     * 撤销操作
     */
    private class UndoAction extends AbstractAction {
        public UndoAction() {
            super("撤销");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.undo();
            } catch (CannotUndoException ex) {
                log.debug("no undo action:{}", ex.getMessage());
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        protected void updateUndoState() {
            setEnabled(undoManager.canUndo());
        }
    }

    /**
     * 恢复事件
     */
    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("恢复");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undoManager.redo();
            } catch (CannotRedoException ex) {
                log.debug("no redo action:{}", ex.getMessage());
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            setEnabled(undoManager.canRedo());
        }
    }


}
