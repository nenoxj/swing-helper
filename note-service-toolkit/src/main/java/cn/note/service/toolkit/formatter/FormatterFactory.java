package cn.note.service.toolkit.formatter;

/**
 * 格式工场
 */
public class FormatterFactory {


    /**
     * 创建mysql格式化
     *
     * @return
     */
    public static CodeFormatter createMysqlFormatter() {
        return new SqlFormatter(SqlType.mysql);
    }


    /**
     * 创建json格式化
     * @return
     */
    public static CodeFormatter createJsonFormatter() {
        return new JsonFormatter();
    }
}
