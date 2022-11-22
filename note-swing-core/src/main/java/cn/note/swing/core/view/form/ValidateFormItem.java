package cn.note.swing.core.view.form;

import java.util.List;

/**
 * 实现表单校验
 *
 * @author jee
 */
public interface ValidateFormItem {


    /**
     * 清除错误
     */
    void clean();

    /**
     * 添加验证规则
     *
     * @param validate 验证规则
     */
    void addValidateForm(Validate validate);


    /**
     * 移除验证规则
     *
     * @param validate 验证规则
     */
    void removeValidateForm(Validate validate);


    /**
     * @return 获取所有的验证规则
     */
    List<Validate> getValidateForms();


    /**
     * @param validateList 添加多个验证规则
     */
    void addValidateForms(List<Validate> validateList);

}
