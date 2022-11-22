package cn.note.swing.core.view.form;

import cn.hutool.core.util.StrUtil;

/**
 * @description:
 * @author: jee
 */

public class ValidateStatus {
    private boolean error;
    private String errorText;

    public ValidateStatus(boolean error, String errorText) {
        this.error = error;
        this.errorText = errorText;
    }

    public static ValidateStatus ok() {
        return new ValidateStatus(false, null);
    }

    public static ValidateStatus fail(String template, Object... params) {
        String errorText = StrUtil.format(template, params);
        return new ValidateStatus(true, errorText);
    }

    public boolean isError() {
        return error;
    }

    public String getErrorText() {
        return errorText;
    }
}
