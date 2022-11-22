package cn.note.swing.core.view.form;

import cn.hutool.core.util.StrUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * 复选框 表单元素
 * @author jee
 */
public class CheckBoxFormItem extends JPanel implements FormItem<Boolean> {

    private JCheckBox checkBox;


    public CheckBoxFormItem(String text) {
        this(text, false);
    }

    public CheckBoxFormItem(String text, boolean checked) {
        this(new JCheckBox(text), checked, new MigLayout("insets 0,gapy 0", "[grow]", ""));
    }

    public CheckBoxFormItem(JCheckBox checkBox, boolean checked, MigLayout migLayout) {
        super.setLayout(migLayout);
        this.checkBox = checkBox;
        this.checkBox.setSelected(checked);
        this.render();
    }


    private void render() {
        super.add(checkBox, "gapleft 20,grow");
    }


    public void setLeftOffset(int offset) {
        super.add(checkBox, StrUtil.format("gapleft {},grow", offset));
    }

    @Override
    public Boolean getFieldValue() {
        return checkBox.isSelected();
    }

    @Override
    public void setFieldValue(Boolean value) {
        checkBox.setSelected(value);
    }


    /**
     * @return 执行校验规则
     */
    @Override
    public boolean valid() {
        return false;
    }

    @Override
    public JComponent getField() {
        return checkBox;
    }


    @Override
    public JPanel getView() {
        return this;
    }

}
