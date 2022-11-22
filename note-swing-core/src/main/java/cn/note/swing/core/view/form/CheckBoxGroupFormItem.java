package cn.note.swing.core.view.form;

import cn.hutool.core.util.ArrayUtil;

import javax.swing.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 复选按钮组元素
 * //TODO 实现全选功能
 *
 * @author jee
 */
public class CheckBoxGroupFormItem extends AbstractButtonGroupFormItem<JCheckBox, String[]> {


    public CheckBoxGroupFormItem(String labelName, List<String> itemList) {
        super(labelName);
        List<JCheckBox> checkboxButtonList = itemList.stream().map(JCheckBox::new).collect(Collectors.toList());
        setButtonItems(checkboxButtonList, false);
    }

    public CheckBoxGroupFormItem(String labelName) {
        super(labelName);
    }


    @Override
    public String[] getFieldValue() {
        return null;
    }

    @Override
    public void setFieldValue(String[] values) {
    }


}
