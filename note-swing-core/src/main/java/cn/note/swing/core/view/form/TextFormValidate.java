package cn.note.swing.core.view.form;

import cn.hutool.core.util.StrUtil;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * @description: 文本校验器
 * @author: jee
 */
public interface TextFormValidate<T extends JTextComponent> {

    public abstract AbstractFormItem<String, T> getFormItem();

    /**
     * 验证空值
     */
    default TextFormValidate validEmpty() {
        String msg = StrUtil.format("{}不能为空 !", getFormItem().getLabelName());
        return validEmpty(msg);
    }


    default TextFormValidate validEmpty(String msg) {
        Validate validateForm = () -> {
            String inputText = getFormItem().getFieldValue();
            if (StrUtil.isBlank(inputText)) {
                return ValidateStatus.fail(msg);
            }
            return ValidateStatus.ok();
        };
        getFormItem().addValidateForm(validateForm);
        return this;
    }

    default TextFormValidate validMaxLength(int size) {
        String msg = StrUtil.format("{}长度不能超过{}字符 !", getFormItem().getLabelName(), size);
        return validMaxLength(size, msg);
    }

    /**
     * @param size 最大输入长度
     */
    default TextFormValidate validMaxLength(int size, String msg) {
        Validate validateForm = () -> {
            String inputText = getFormItem().getFieldValue();
            if (inputText != null && inputText.length() > size) {
                return ValidateStatus.fail(msg);
            }
            return ValidateStatus.ok();
        };
        getFormItem().addValidateForm(validateForm);
        return this;
    }

    default TextFormValidate validMinLength(int size) {
        String msg = StrUtil.format("{}长度不能少于{}字符 !", getFormItem().getLabelName(), size);
        return validMinLength(size, msg);
    }

    /**
     * @param size 验证输入长度
     */
    default TextFormValidate validMinLength(int size, String msg) {
        Validate validateForm = () -> {
            String inputText = getFormItem().getFieldValue();
            if (inputText != null && inputText.length() < size) {
                return ValidateStatus.fail(msg);
            }
            return ValidateStatus.ok();
        };
        getFormItem().addValidateForm(validateForm);
        return this;
    }
}
