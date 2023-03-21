package com.szcgc.comm.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author liaohong
 * @create 2021/5/2 15:07
 */
public class CryptUtils {

    private static final byte[] DFT_AES_KEY = "80E3Ge3@f3!e*78c".getBytes(); // 必须16位
    private static final byte[] DFT_DES_KEY = "~Q!4@m*s".getBytes(); // 必须8位
    private static final byte[] DFT_DES_IVPS = { 10, 21, 39, 42, 15, 63, 74, 58 };

    /**
     * aes加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encryptAes(String content, byte[] key) {
        if (StringUtils.isEmpty(content))
            return content;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] ciphertext = cipher.doFinal(content.getBytes());
            return Base64.encodeBase64String(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * aes加密
     *
     * @param content
     * @return
     */
    public static String encryptAes(String content) {
        return encryptAes(content, DFT_AES_KEY);
    }

    /**
     * aes解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decryptAes(String content, byte[] key) {
        if (StringUtils.isEmpty(content))
            return content;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] ciphertext = cipher.doFinal(Base64.decodeBase64(content));
            return new String(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * aes解密
     *
     * @param content
     * @return
     */
    public static String decryptAes(String content) {
        return decryptAes(content, DFT_AES_KEY);
    }
}
