package com.szcgc.cougua.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 用于报文加解密 敏感数据脱敏脚手架
 */
public class AesThreadSafeUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static ThreadLocal<SecretKeySpec> threadLocalKeySpec = new ThreadLocal<>();
    private static ThreadLocal<Cipher> threadLocalCipher = new ThreadLocal<>();
    //偏移量,AES 为16bytes. DES 为8bytes
    private static final String IV = "1234567890123456";

    /**
     * AES加密
     *
     * @param input 将要加密的内容
     * @param key 密钥
     * @return 已经加密的字节数组内容 再 base64 之后的字符串
     */
    public static String encrypt(byte[] input, byte[] key) throws Exception {
        SecretKeySpec keySpec = threadLocalKeySpec.get();
        if (keySpec == null) {
            keySpec = new SecretKeySpec(key, ALGORITHM);
            threadLocalKeySpec.set(keySpec);
        }
        Cipher cipher = threadLocalCipher.get();
        if (cipher == null) {
            cipher = Cipher.getInstance(TRANSFORMATION);
            threadLocalCipher.set(cipher);
        }
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return Base64.encodeBase64String(cipher.doFinal(input));
    }

    /**
     *
     * @param input
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String input, byte[] key) throws Exception {
        SecretKeySpec keySpec = threadLocalKeySpec.get();
        if (keySpec == null) {
            keySpec = new SecretKeySpec(key, ALGORITHM);
            threadLocalKeySpec.set(keySpec);
        }
        Cipher cipher = threadLocalCipher.get();
        if (cipher == null) {
            cipher = Cipher.getInstance(TRANSFORMATION);
            threadLocalCipher.set(cipher);
        }
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] result = cipher.doFinal(Base64.decodeBase64(input));
        return new String(result,"utf-8");
    }


    public static void main(String[] args) {
        try {
            String str = "aes测试";
            String key = "1234567812345678";
            //AES/ECB/NoPadding填充模式
            String ecbEn = null;
            try {
                ecbEn = encrypt(str.getBytes("UTF-8"), key.getBytes("UTF-8"));
                System.out.println("ecb加密后"+ecbEn);
                String ecbDe = decrypt(ecbEn, key.getBytes("UTF-8"));
                System.out.println("ecbDe解密后"+ecbDe);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            String str22 = decryptAesToString(ecbEn, key);
//            System.out.println("str22解密后"+str22);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
