package cn.note.swing.core.view.form;

import cn.note.swing.core.event.change.ChangeDocumentListener;
import cn.note.swing.core.util.SwingCoreUtil;
import cn.note.swing.core.view.base.BorderBuilder;
import cn.note.swing.core.view.wrapper.TextFieldWrapper;
import cn.note.swing.core.view.theme.ThemeColor;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import java.awt.*;

/**
 * Input错误特殊提示样式
 *
 * @author: jee
 */
public class InputFormItem extends TextFormItem<JTextField> {

    private TextFieldWrapper textFieldWrapper;

    public InputFormItem(String labelName) {
        this(labelName, null);
    }


    public InputFormItem(String labelName, String placeHolder) {
        this(labelName, new JTextField(), placeHolder);
    }

    public InputFormItem(String labelName, JTextField field, String placeHolder) {
        super(labelName, field);

        if (placeHolder != null) {
            field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeHolder);
        }
    }

    /**
     * @param text 设置Input输入框的错误和文本
     */
    @Override
    public void setError(String text) {
        super.setError(text);
        getField().putClientProperty(FlatClientProperties.OUTLINE, new Color[]{ThemeColor.redColor, ThemeColor.redColor});
    }

    /**
     * 清除Input输入框的错误和文本
     */
    @Override
    public void clean() {
        super.clean();
        getField().putClientProperty(FlatClientProperties.OUTLINE, null);
    }


    public void useLineStyle(Color bgColor) {

        super.useLineStyle(bgColor);
        JTextField textField = getField();
        Border errorBorder = BorderBuilder.createWrapBorder(textField, BorderBuilder.bottomBorder(2, ThemeColor.dangerColor));

        // 控制校验颜色
        textField.getDocument().addDocumentListener(new ChangeDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                SwingCoreUtil.onceTimer(100, () -> {
                    if (InputFormItem.this.isError()) {
                        textField.setBorder(errorBorder);
                    } else {
                        textField.setBorder(gainBorder);
                    }
                });

            }
        });

    }
}
