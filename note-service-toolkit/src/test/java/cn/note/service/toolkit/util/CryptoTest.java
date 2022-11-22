package cn.note.service.toolkit.util;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class CryptoTest {


    @Test
    public void testAes() {
        System.out.println(AESUtil.encrypt("123456"));
        System.out.println(AESUtil.decrypt("LpY2fLigzvlfbvsZ5oKgWA=="));
    }

    @Test
    public void testDes() {
        System.out.println(DESUtil.encrypt("123456"));
        System.out.println(DESUtil.decrypt("c4rNBLVUFhE="));
    }
}