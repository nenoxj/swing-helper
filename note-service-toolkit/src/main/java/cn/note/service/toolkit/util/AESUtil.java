package cn.note.service.toolkit.util;

import cn.hutool.core.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

public class AESUtil {
    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);
    private static final String AES = "AES";
    private static final String CRYPT_KEY = "2000123--2050123";
    private static final String IV_STRING = "2049000--2000000";

    /**
     * 加密
     */
    public static String encrypt(String content) {
        byte[] encryptedBytes = new byte[0];
        try {
            byte[] byteContent = content.getBytes(Charset.defaultCharset());
            // 注意，为了能与 iOS 统一 // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = CRYPT_KEY.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
        } catch (Exception e) {
            log.error("加密异常!", e);
        }
        return Base64.encode(encryptedBytes);
    }

    /**
     * 解密
     */

    public static String decrypt(String content) { // base64 解码
        try {
            byte[] encryptedBytes = Base64.decode(content);
            byte[] enCodeFormat = CRYPT_KEY.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = IV_STRING.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, Charset.defaultCharset());
        } catch (Exception e) {
            log.error("解密异常", e);
        }
        return null;
    }

}
