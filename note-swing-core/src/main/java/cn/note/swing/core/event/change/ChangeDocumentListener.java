package cn.note.swing.core.event.change;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 简化DocumentListener
 * 用于JTextComponent 及其子类的变更事件
 * JTextField
 * JTextArea
 * JTextPane
 *
 * @author jee
 */
@FunctionalInterface
public interface ChangeDocumentListener extends DocumentListener {

    void update(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    default void removeUpdate(DocumentEvent e) {
        update(e);
    }

    @Override
    default void changedUpdate(DocumentEvent e) {
        update(e);
    }
}
