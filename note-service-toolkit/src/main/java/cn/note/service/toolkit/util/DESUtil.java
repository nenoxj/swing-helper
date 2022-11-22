package cn.note.service.toolkit.util;

import cn.hutool.core.codec.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * DES加密不如AES快, 不建议使用, 使用AESUtil
 *
 * @author jee
 * @see AESUtil
 */
@Deprecated
public class DESUtil {
    private static final Logger log = LoggerFactory.getLogger(DESUtil.class);
    private final static String secretKey = "1mixky-cms-8encrypt-5key";
    private final static String iv = "01234567";
    private final static String encoding = "UTF-8";
    private static final String MODE_DESEDE = "desede";

    /**
     * 加密文本
     */
    @Deprecated
    public static String encrypt(String plainText) {
        try {
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MODE_DESEDE);
            deskey = keyFactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
            return Base64.encode(encryptData);
        } catch (Exception e) {
            log.error("加密异常!", e);
        }

        return null;

    }

    /**
     * 解密文本
     */
    @Deprecated
    public static String decrypt(String encryptText) {
        try {
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(MODE_DESEDE);
            deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/NoPadding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
            return new String(decryptData, encoding).trim();
        } catch (Exception e) {
            log.error("解密异常", e);
        }
        return null;
    }
}
