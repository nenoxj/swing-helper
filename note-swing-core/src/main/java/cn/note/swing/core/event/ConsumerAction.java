package cn.note.swing.core.event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

/**
 * 自定义回调动作
 * AbstractAction的便捷操作
 *
 * @author: jee
 */
public class ConsumerAction extends AbstractAction {
    private final Consumer<ActionEvent> consumer;

    public ConsumerAction(Consumer<ActionEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        consumer.accept(e);
    }
}