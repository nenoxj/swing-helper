package cn.note.swing.core.view.form;

/**
 * 实现规则校验
 */
@FunctionalInterface
public interface Validate {

    /**
     * @return 提供校验方法
     */
    ValidateStatus valid();

}
