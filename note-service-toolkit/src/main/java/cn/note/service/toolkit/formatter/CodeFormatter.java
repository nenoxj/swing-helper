package cn.note.service.toolkit.formatter;

/**
 * @description: 格式化工具
 * @author: jee
 * @time: 2022/2/28 16:47
 */
public interface CodeFormatter {


    /**
     * 返回格式化内容
     * 格式化失败时 必须返回 FormatterException 格式化失败得行数和列数
     *
     * @param originCode 原始code
     * @return
     */
    String format(String originCode) throws FormatterException;


}
