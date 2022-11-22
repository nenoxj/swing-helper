package cn.note.swing.core.view.form;

import cn.note.swing.core.event.ConsumerAction;
import cn.note.swing.core.event.change.ChangeDocumentListener;
import cn.note.swing.core.event.key.KeyActionFactory;

import javax.swing.text.JTextComponent;

/**
 * 监听文本变化事件触发
 *
 * @author: jee
 */
public class TextFormItem<T extends JTextComponent> extends AbstractFormItem<String, T> implements TextFormValidate {

    public TextFormItem(String labelName, T field) {
        this(labelName, field, null);
    }

    public TextFormItem(String labelName, T field, String placeHolder) {
        super(labelName, field);
        listenerInputChange();
    }


    @Override
    public String getFieldValue() {
        return getField().getText();
    }

    @Override
    public void setFieldValue(String value) {
        getField().setText(value);
    }

    /**
     * 监听inputChange
     */
    public void listenerInputChange() {
        ChangeDocumentListener changeDocumentListener = e -> valid();
        getField().getDocument().addDocumentListener(changeDocumentListener);
    }


    /**
     * 调用该方法多次,仅保留最后一次回车动作
     *
     * @param action 回车动作
     */
    public void addEnterEvent(ConsumerAction action) {
        KeyActionFactory.bindEnterAction(getField(), action);
    }


    @Override
    public AbstractFormItem getFormItem() {
        return this;
    }
}
