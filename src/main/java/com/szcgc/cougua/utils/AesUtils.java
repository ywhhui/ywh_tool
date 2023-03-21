package com.szcgc.cougua.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 用于报文加解密 敏感数据脱敏脚手架
 */
public class AesUtils {


    public static final String algorithm = "AES";
    // AES/CBC/NOPaddin
    // AES 默认模式
    // 使用CBC模式, 在初始化Cipher对象时, 需要增加参数, 初始化向量IV : IvParameterSpec iv = new
    // IvParameterSpec(key.getBytes());
    // NOPadding: 使用NOPadding模式时, 原文长度必须是8byte的整数倍   ECB模式是可重复解密的
    public static final String transformation = "AES/ECB/NOPadding";

    //填充类型
    public static final String AES_TYPE = "AES/CBC/PKCS5Padding";
    //偏移量,AES 为16bytes. DES 为8bytes
    private static final String IV = "1234567890123456";


    /**
     * AES加密
     *
     * @param str 将要加密的内容
     * @param key 密钥
     * @return 已经加密的字节数组内容 再 base64 之后的字符串
     */
    public static String encryptAes(String str, String key) throws Exception {
        byte[] data = str.getBytes("UTF-8");
        byte[] keyByte = key.getBytes("UTF-8");
        //不足16字节，补齐内容为差值
        int len = 16 - data.length % 16;
        for (int i = 0; i < len; i++) {
            byte[] bytes = {(byte) len};
            data = concat(data, bytes);
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] resultByte = cipher.doFinal(data);
            return Base64.encodeBase64String(resultByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES解密
     *
     * @param base64Str 将要解密的字节数组内容 的 base64编码后的字符串
     * @param key       密钥
     * @return 已经解密的内容
     */
    public static String decryptAes(String base64Str, String key) throws Exception {

        try {
            byte[] data = Base64.decodeBase64(base64Str);
            data = noPadding(data, -1);
            byte[] keyByte = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decryptData = cipher.doFinal(data);
            int len = 2 + byteToInt(decryptData[4]) + 3;
            byte[] resultByte = noPadding(decryptData, len);
            String resultStr = new String(resultByte);
            return resultStr.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 合并数组
     *
     * @param firstArray  第一个数组
     * @param secondArray 第二个数组
     * @return 合并后的数组
     */
    public static byte[] concat(byte[] firstArray, byte[] secondArray) {
        if (firstArray == null || secondArray == null) {
            return null;
        }
        byte[] bytes = new byte[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length,
                secondArray.length);
        return bytes;
    }


    /**
     * 去除数组中的补齐
     *
     * @param paddingBytes 源数组
     * @param dataLength   去除补齐后的数据长度
     * @return 去除补齐后的数组
     */
    public static byte[] noPadding(byte[] paddingBytes, int dataLength) {
        if (paddingBytes == null) {
            return null;
        }

        byte[] noPaddingBytes = null;
        if (dataLength > 0) {
            if (paddingBytes.length > dataLength) {
                noPaddingBytes = new byte[dataLength];
                System.arraycopy(paddingBytes, 0, noPaddingBytes, 0, dataLength);
            } else {
                noPaddingBytes = paddingBytes;
            }
        } else {
            int index = paddingIndex(paddingBytes);
            if (index > 0) {
                noPaddingBytes = new byte[index];
                System.arraycopy(paddingBytes, 0, noPaddingBytes, 0, index);
            }
        }

        return noPaddingBytes;
    }


    /**
     * 获取补齐的位置
     *
     * @param paddingBytes 源数组
     * @return 补齐的位置
     */
    private static int paddingIndex(byte[] paddingBytes) {
        for (int i = paddingBytes.length - 1; i >= 0; i--) {
            if (paddingBytes[i] != 0) {
                return i + 1;
            }
        }
        return -1;
    }


    public static int byteToInt(byte b) {
        return (b) & 0xff;
    }



    /**
     * AES加密
     *
     * @param str 将要加密的内容
     * @param key 密钥
     * @return 已经加密的字节数组内容 再 base64 之后的字符串
     */
    public static String encryptAesToString(String str, String key) throws Exception {
        if(StringUtils.isNotEmpty(str)){
            try {
                if(null != key && key.length()==16){
                    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                    IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
                    cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
                    byte[] resultByte = cipher.doFinal(str.getBytes());
                    return Base64.encodeBase64String(resultByte);
                }else {
                    System.out.println("密钥长度不够");
                    return str;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return str;
        }
    }


    /**
     * AES解密
     *
     * @param str 将要解密的字节数组内容 的 base64编码后的字符串
     * @param key       密钥
     * @return 已经解密的内容
     */
    public static String decryptAesToString(String str, String key) throws Exception {
        if(StringUtils.isNotEmpty(str)){
            try {
                if(null != key && key.length()==16){
                    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
                    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                    IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
                    cipher.init(Cipher.DECRYPT_MODE, skeySpec,iv);
                    byte[] resultByte = cipher.doFinal(Base64.decodeBase64(str));
                    return new String(resultByte);
                }else {
                    System.out.println("密钥长度不够");
                    return str;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return str;
        }
    }


    public static void main(String[] args) {
        try {
            String str = "aes测试";
            String key = "1234567812345678";
            //AES/ECB/NoPadding填充模式
            String ecbEn = encryptAes(str, key);
            System.out.println("ecb加密后"+ecbEn);
            String ecbDe = decryptAes(ecbEn, key);
            System.out.println("ecbDe解密后"+ecbDe);

            //AES/CBC/PKCS5Padding 填充模式
            String cbcEn = encryptAesToString(str, key);
            System.out.println("cbc加密后"+cbcEn);
            String cbdDe = decryptAesToString(cbcEn, key);
            System.out.println("cbc解密后"+cbdDe);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
