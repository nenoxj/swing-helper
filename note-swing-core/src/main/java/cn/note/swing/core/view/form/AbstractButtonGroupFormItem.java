package cn.note.swing.core.view.form;

import net.miginfocom.swing.MigLayout;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.util.List;

/**
 * 抽象按钮组 表单元素
 *
 * @author jee
 */
public abstract class AbstractButtonGroupFormItem<E extends AbstractButton, T> extends JPanel implements FormItem<T> {

    /**
     * label名称 不带colon
     */
    private String labelName;
    /**
     * 左侧label
     */
    private JLabel label;

    /**
     * 组容器
     */
    private JPanel groupContainer;


    protected void addButton(E button) {
        this.groupContainer.add(button);
    }

    public void setButtonItems(@Nonnull List<E> itemList, boolean selectFirst) {
        for (int i = 0; i < itemList.size(); i++) {
            E button = itemList.get(i);
            if (i == 0 && selectFirst) {
                button.setSelected(true);
            }
            button.setActionCommand(button.getText());
            addButton(button);
        }
    }


    public void setButtonItems(@Nonnull List<E> itemList) {
        setButtonItems(itemList, true);
    }

    public AbstractButtonGroupFormItem(String labelName) {
        this(labelName, new MigLayout("insets 0,gapy 0", "[60::][grow]", "[grow]"), true);

    }


    public AbstractButtonGroupFormItem(String labelName, MigLayout migLayout, boolean colon) {
        super.setLayout(migLayout);
        this.groupContainer = new JPanel(new MigLayout("insets 0,gap 0,nogrid", "[grow]", ""));
        this.labelName = labelName;
        if (colon) {
            labelName += COLON_CHAR;
        }
        label = new JLabel(labelName);
//        label.setLineWrap(true);
        this.render();
    }


    private void render() {
        super.add(label, "right");
        super.add(groupContainer, "grow,wrap");
    }


    @Override
    public boolean valid() {
        return false;
    }


    @Override
    public JComponent getField() {
        return groupContainer;
    }


    @Override
    public JPanel getView() {
        return this;
    }


    public String getLabelName() {
        return labelName;
    }


}
