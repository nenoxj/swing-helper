package cn.note.swing.core.view.form;

import javax.swing.*;
import java.util.List;

/**
 * 表单构建
 *
 * @author :jee
 */
public interface FormItem<E> {

    /**
     * label 符号
     */
    public static final String COLON_CHAR = ":";

    /**
     * @return 执行校验规则
     */
    boolean valid();

    /**
     * @return 获取form视图
     */
    JComponent getField();


    /**
     * @return 获取属性值
     */
    public E getFieldValue();

    /**
     * @param value 设置属性值
     */
    public void setFieldValue(E value);


    /**
     * @return 组件视图
     */
    public JComponent getView();
}
