package cn.note.swing.core.view.form;

import cn.hutool.core.util.StrUtil;
import cn.note.swing.core.view.base.BorderBuilder;
import cn.note.swing.core.view.theme.ThemeColor;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 快速的Form表单构建
 * <p>
 * name: form 格式
 * 默认的构造方法中:
 * name 占用40的宽度
 * <p>
 * 附带错误提示
 *
 * @author :jee
 */
public abstract class AbstractFormItem<E, T extends JComponent> extends JPanel implements FormItem<E>, ValidateFormItem {

    /**
     * label名称 不带colon
     */
    private String labelName;
    /**
     * 左侧label
     */
    private JLabel label;
    /**
     * 右侧form元素
     */
    private T field;
    /**
     * 错误文本
     */
    private JLabel errorText;
    /**
     * 是否开启 :
     */
    private boolean colon;

    /**
     * 验证规则器
     */
    private List<Validate> validateForms;

    /* mig布局器*/
    private MigLayout migLayout;


    public AbstractFormItem(String labelName, T field) {
        this(labelName, field, new MigLayout("insets 0,gapy 0", "[60::][grow]", "[grow]"), true);
    }

    public AbstractFormItem(String labelName, T field, MigLayout migLayout, boolean colon) {

        this.migLayout = migLayout;
        this.labelName = labelName;
        if (colon) {
            labelName += COLON_CHAR;
        }
        label = new JLabel(labelName);
//        label.setLineWrap(true);
        this.field = field;
        // 错误提示
        errorText = new JLabel();
        errorText.setForeground(ThemeColor.redColor);
        validateForms = new ArrayList<>();
        this.render();
    }

    public void setLabelWidth(int width) {
        if (migLayout != null) {
            migLayout.setColumnConstraints("[" + width + "][grow]");
        }
    }


    private void render() {
        super.setLayout(migLayout);
        super.add(label, "right");
        super.add(field, "grow,wrap");
        super.add(errorText, "skip 1,grow");
    }

    /**
     * @param text 设置错误提示
     */
    public void setError(String text) {
        errorText.setText(text);
    }


    /**
     * @return 是否错误
     */
    public boolean isError() {
        return StrUtil.isNotBlank(errorText.getText());
    }

    /**
     * 清除错误
     */
    public void clean() {
        errorText.setText("");
    }


    /**
     * @return 获取label名称
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * @return 获取泛型转换的真实组件
     */
    public T getField() {
        return field;
    }


    /**
     * 添加验证规则
     * AbstractFormItem 子类应该享有 统一的添加规则方法
     *
     * @param validate 验证规则
     */
    @Override
    public void addValidateForm(Validate validate) {
        validateForms.add(validate);
    }


    /**
     * 移除验证规则
     * AbstractFormItem 子类应该享有 统一的移除规则方法
     *
     * @param validate 验证规则
     */
    @Override
    public void removeValidateForm(Validate validate) {
        validateForms.remove(validate);
    }


    /**
     * @return 获取所有的验证规则
     */
    @Override
    public List<Validate> getValidateForms() {
        return validateForms;
    }

    /**
     * @param validateList 添加多个验证规则
     */
    @Override
    public void addValidateForms(List<Validate> validateList) {
        this.validateForms.addAll(validateList);
    }


    /**
     * @return 执行校验规则
     */
    @Override
    public boolean valid() {
        boolean error = false;
        for (Validate validateForm : getValidateForms()) {
            ValidateStatus status = validateForm.valid();
            error = status.isError();
            if (error) {
                setError(status.getErrorText());
                break;
            }
            clean();
        }
        return error;
    }


    @Override
    public JPanel getView() {
        return this;
    }


    /**
     * 获取焦点边框
     */
    protected Border gainBorder;


    /**
     * 失去焦点边框
     */
    protected Border lostBorder;


    /**
     * 使用线样式风格
     *
     * @param bgColor 组件背景色
     */
    public void useLineStyle(Color bgColor) {
        getField().setBackground(bgColor);
        gainBorder = BorderBuilder.createWrapBorder(getField(), BorderBuilder.bottomBorder(1, ThemeColor.primaryColor));
        lostBorder = BorderBuilder.createWrapBorder(getField(), BorderBuilder.bottomBorder(1, ThemeColor.grayColor));
        getField().setBorder(lostBorder);
        // 控制焦点颜色
        getField().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                getField().setBorder(gainBorder);
            }

            @Override
            public void focusLost(FocusEvent e) {
                getField().setBorder(lostBorder);
            }
        });

    }

}
