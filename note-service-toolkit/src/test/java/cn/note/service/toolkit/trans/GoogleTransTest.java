package cn.note.service.toolkit.trans;

import cn.note.service.toolkit.common.ResultData;
import org.junit.Test;

public class GoogleTransTest {


    @Test
    public void testQuery() {

//        ResultData<String> r = GoogleTrans.query("hello world");
//        System.out.println(r.getData());

        ResultData<String> r = YouDaoTrans.query("hello world");
        System.out.println(r.getData());
    }

}