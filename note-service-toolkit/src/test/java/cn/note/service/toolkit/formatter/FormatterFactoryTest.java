package cn.note.service.toolkit.formatter;

import org.junit.Test;

public class FormatterFactoryTest {


    @Test
    public void testJsonSuccess() {

        String jsonStr = "{\"product\":\"STARUML.V4\",\"licenseKey\":\"10ab367868767676000003434\",\"licenseSite\":\"CAMPUS\",\"licenseType\":\"vip\",\"quantity\":\"CAMPUS\",\"timestamp\":\"4077761808\",\"name\":\"0xcb\"}";
        testCodeFormatter(FormatterFactory.createJsonFormatter(),jsonStr);
    }

    @Test
    public void testJsonErr() {
        String jsonStr = "{\"product:\"STARUML.V4\",\"licenseKey\":\"10ab367868767676000003434\",\"licenseSite\":\"CAMPUS\",\"licenseType\":\"vip\",\"quantity\":\"CAMPUS\",\"timestamp\":\"4077761808\",\"name\":\"0xcb\"}";
        testCodeFormatter(FormatterFactory.createJsonFormatter(),jsonStr);
    }


    @Test
    public void testSqlSuccess(){
        String sql = "select id, name,age from stu where age<20 ";
        testCodeFormatter(FormatterFactory.createMysqlFormatter(),sql);
    }

    @Test
    public void testSqlErr(){
        String sql = "select id, name,age, from stu where age<20 ";
        testCodeFormatter(FormatterFactory.createMysqlFormatter(),sql);
    }



    private void testCodeFormatter(CodeFormatter formatter, String code) {
        try {
            String result = formatter.format(code);
            System.out.println(result);
        } catch (FormatterException e) {
            System.out.println(e.toString());
        }
    }
}