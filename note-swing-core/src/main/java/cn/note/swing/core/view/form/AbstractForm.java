package cn.note.swing.core.view.form;

import cn.hutool.core.util.BooleanUtil;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 表单管理器
 * @author: jee
 */
public abstract class AbstractForm extends JPanel {

    protected List<FormItem> formItems;

    public AbstractForm() {
        formItems = new ArrayList<>();
        super.setLayout(createLayout());
    }

    public abstract LayoutManager createLayout();


    public void addItem(FormItem formItem, String nameKey) {
        formItem.getField().setName(nameKey);
        formItems.add(formItem);
    }

    //
    public void addFormItem(@Nonnull FormItem formItem, String nameKey, java.lang.Object constraints) {
        addItem(formItem, nameKey);
        super.add(formItem.getField(), constraints);
    }

    /**
     * 根据名称获取组件
     */
    private FormItem getFormItem(String nameKey) {
        return formItems.stream()
                .filter(formItem -> nameKey.equals(formItem.getField().getName()))
                .findFirst().orElse(null);
    }


    /**
     * 设置form 值
     * <p>
     * 为了不限制泛型的检查....所有使用了@SuppressWarnings
     *
     * @param nameKey 名称
     * @param value   值
     */
    @SuppressWarnings("unchecked")
    public void setFormValue(String nameKey, Object value) {
        getFormItem(nameKey).setFieldValue(value);
    }


    /**
     * 获取form 值
     *
     * @param nameKey 名称
     */
    public Object getFormValue(String nameKey) {
        return getFormItem(nameKey).getFieldValue();
    }


    /**
     * 校验所有属性
     *
     * @return 返回校验结果
     */
    public boolean validFields() {
        boolean[] errors = new boolean[formItems.size()];
        int key = 0;
        for (FormItem formItem : formItems) {
            boolean error = formItem.valid();
            errors[key] = error;
            key++;
        }
        return BooleanUtil.or(errors);
    }


    public Map<String, Object> getFormValues() {
        Map<String, Object> result = new HashMap<>();
        formItems.forEach(formItem -> result.put(formItem.getField().getName(), formItem.getFieldValue()));
        return result;
    }


    public void setFormValues(Map<String, Object> dataMap) {
        formItems.forEach(formItem -> {
            String name = formItem.getField().getName();
            Object value = dataMap.get(name);
            setFormValue(name, value);

        });

    }


}
