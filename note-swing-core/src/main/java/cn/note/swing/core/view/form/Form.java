package cn.note.swing.core.view.form;

import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXTitledSeparator;

import java.awt.*;

/**
 * 单行表单管理器
 *
 * @author: jee
 */
public class Form extends AbstractForm {

    private int labelWidth;

    public Form() {
        this(0);
    }

    public Form(int labelWidth) {
        this.labelWidth = labelWidth;
    }


    @Override
    public LayoutManager createLayout() {
        return new MigLayout("", "[grow]", "");
    }


    /**
     * 根据名称添加组件
     */
    public void addFormItem(FormItem formItem, String nameKey) {
        super.addItem(formItem, nameKey);
        super.add(formItem.getView(), "growx,wrap");
        Component component = formItem.getView();
        if (component instanceof AbstractFormItem) {
            AbstractFormItem abstractFormItem = (AbstractFormItem) component;
            if (labelWidth > 0) {
                abstractFormItem.setLabelWidth(labelWidth);
            }
        }
    }


    /**
     * 设置标签样式
     */
    public void addFormItem(FormItem formItem, String nameKey, int labelWidth) {
        addFormItem(formItem, nameKey);

    }


    /**
     * 添加标题分割线
     *
     * @param title 标题
     */
    public void addTitleSeparator(String title) {
        addTitleSeparator(new JXTitledSeparator(title));
    }


    public void addTitleSeparator(JXTitledSeparator titledSeparator) {
        super.add(titledSeparator, "growx,wrap");
    }

}
