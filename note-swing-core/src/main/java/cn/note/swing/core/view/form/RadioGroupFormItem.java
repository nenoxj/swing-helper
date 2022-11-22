package cn.note.swing.core.view.form;

import javax.swing.*;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单选按钮组元素
 *
 * @author jee
 */
public class RadioGroupFormItem extends AbstractButtonGroupFormItem<JRadioButton, String> {
    /**
     * 按钮组
     */
    private ButtonGroup buttonGroup;

    @Override
    protected void addButton(JRadioButton button) {
        if (buttonGroup == null) {
            buttonGroup = new ButtonGroup();
        }
        buttonGroup.add(button);
        super.addButton(button);
    }

    public RadioGroupFormItem(String labelName, List<String> itemList) {
        super(labelName);
        List<JRadioButton> radioButtonList = itemList.stream().map(JRadioButton::new).collect(Collectors.toList());
        setButtonItems(radioButtonList);
    }

    public RadioGroupFormItem(String labelName) {
        super(labelName);
    }


    @Override
    public String getFieldValue() {
        return buttonGroup.getSelection().getActionCommand();
    }

    @Override
    public void setFieldValue(String value) {
        Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.getText().equals(value)) {
                buttonGroup.setSelected(button.getModel(), true);
            }
        }
    }


}
