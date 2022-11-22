package cn.note.swing.core.view.form;

import cn.note.swing.core.view.wrapper.FlatWrapper;
import cn.note.swing.core.view.theme.ThemeColor;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 添加下拉选项,防止重复
 *
 * @author: jee
 */
public class SelectedFormItem<E> extends AbstractFormItem<E, JComboBox<E>> {

    private DefaultComboBoxModel<E> model;

    private Set<E> items;

    /**
     * 设置元素集合
     *
     * @param items 元素集合
     */
    public void setSelectItems(@Nonnull Collection<E> items) {
        model = new DefaultComboBoxModel<>();
        this.items = new HashSet<>();
        items.stream().distinct().forEach(this::addSelectItem);
        getField().setModel(model);
    }


    /**
     * 添加元素
     *
     * @param item 元素
     */
    public void addSelectItem(E item) {
        if (!items.contains(item)) {
            model.addElement(item);
            items.add(item);
        }
    }

    public SelectedFormItem(String labelName) {
        this(labelName, new JComboBox<E>());
    }

    public SelectedFormItem(String labelName, JComboBox<E> field) {
        super(labelName, field);
        model = (DefaultComboBoxModel<E>) field.getModel();
        items = new HashSet<>();
    }


    @Override
    @SuppressWarnings("unchecked")
    public E getFieldValue() {
        return (E) getField().getSelectedItem();
    }

    @Override
    public void setFieldValue(E value) {
        getField().setSelectedItem(value);
    }


    @Override
    public void useLineStyle(Color bgColor) {
        super.useLineStyle(bgColor);
        // 设置后缀button颜色
        FlatWrapper.style(getField(), "buttonBackground:{}", ThemeColor.toHEXColor(bgColor));
    }
}
