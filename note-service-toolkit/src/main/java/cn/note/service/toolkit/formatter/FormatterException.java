package cn.note.service.toolkit.formatter;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description: 格式化异常
 * 并明确标注出错误行数和列数
 * @author: jee
 * @time: 2022/2/28 16:49
 */
@Setter
@Getter
@ToString
public class FormatterException extends Exception {

    /**
     * 错误行数
     */
    private int line;

    /**
     * 错误列数
     */
    private int column;


    /**
     * 错误内容
     */
    private String error;

    public FormatterException(String error, int line, int column) {
        super(error);
        this.line = line;
        this.column = column;
        this.error=error;
    }

    /**
     * 字符串异常转数值异常
     *
     * @param error  错误信息
     * @param line   错误行数
     * @param column 错误列数
     * @throws FormatterException
     */
    public static void throwError(String error, String line, String column) throws FormatterException {
        int errLine = 0;
        int errColumn = 0;
        try {
            if (StrUtil.isNotBlank(line)) {
                errLine = Integer.parseInt(line);
            }

            if (StrUtil.isNotBlank(column)) {
                errColumn = Integer.parseInt(column);
            }
        } catch (Exception e) {

        }
        throw new FormatterException(error, errLine, errColumn);
    }
}
