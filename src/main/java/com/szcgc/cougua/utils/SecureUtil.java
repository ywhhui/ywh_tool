package com.szcgc.cougua.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class SecureUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecureUtil.class);
    /**
     * 获取图片SHA256
     *
     * @param file
     * @return
     */
    private static String getSHA256(File file) {
        String sha256Hex = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            sha256Hex = DigestUtils.sha256Hex(inputStream);
            return sha256Hex;
        } catch (IOException e) {
            logger.error("文件获取SHA256失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    public static String checkStrSHA256(String str,String SHA256){
        return DigestUtils.sha256Hex(str);
    }
    /**
     * 检查图片的SHA256 是否正确
     *
     * @param file   文件
     * @param SHA256 SHA256结果值
     * @return
     */
    private static boolean checkSHA256(File file, String SHA256) {
        String sha256Hex = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            sha256Hex = DigestUtils.sha256Hex(inputStream);
            if (sha256Hex.equals(SHA256)) {
                return true;
            }
        } catch (IOException e) {
            logger.error("SHA256检查文件完整性失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     *
     * @param str   字符串
     * @return 返回加密后的字符串
     */
    public static String getSHA256(String str){
        return DigestUtils.sha256Hex(str);
    }

    private static boolean checkSHA256(String targetStr, String SHA256) {
        String sha256 = getSHA256(targetStr);
        return SHA256.equals(sha256)?true:false;
    }

    /**
     * SHA256加密
     * @param str
     * @return
     */
    public static String getSHA256Str(String str){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            String result  = bytes2HexString(messageDigest.digest());
            System.out.println("getSHA256Str(SHA256)----"+result);
            return result;
        } catch (Exception e) {
            logger.info("getSHA256Str e:{}",e);
        }
        return str;
    }

    /**
     * 数组转成十六进制字符串
     * @param bytes
     * @return
     */
    public static String bytes2HexString(byte[] bytes){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i){
            buffer.append(toHexString1(bytes[i]));
        }
        return buffer.toString();
    }
    //得到一位的进行补0操作
    public static String toHexString1(byte bytes){
        String s = Integer.toHexString(bytes & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
    }

    /**
     * 数组转成十六进制字符串 方法2
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f'};
        // 一个字节对应两个16进制数，所以长度为字节数组乘2
        char[] resultCharArray = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            resultCharArray[index++] = hexDigits[b>>>4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    /**
     * md5加密 不可逆
     * @param str
     * @return
     */
    public static String encrypt2MD5(String str){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(str.getBytes("UTF-8"));
//            String result = bytesToHex(digest);
            String result = bytes2HexString(digest);
            System.out.println("encrypt2MD5(md5)----"+result);
            return result;
        } catch (Exception e) {
            logger.info("encrypt2MD5 e:{}",e);
        }
        return str;
    }



    //调用样例
    public static void main(String[] args) throws IOException {
        String jiamiStr = "ywh"+"666";


        //不可逆加密
//        String s = encrypt2MD5(jiamiStr);
//        System.out.println("encrypt2MD5--"+s);
//        System.out.println("getSHA256Str(jiamiStr)----"+getSHA256Str(s));
//
//        File file = new File("C:\\Users\\wjw\\Desktop\\22.jpg");
//        String result = getSHA256(file);
//        System.out.println(result);
//        //校验文件是否被篡改
//        Boolean  ss=checkSHA256(file,"27dd54736aed8a3bbec9c39712f2a6e864e5c29b252bca88e01e1222bb268419");
//        System.out.println(ss);
//
//        String targetStr="1308302812048012821047";
//        String res =getSHA256(targetStr);
//        System.out.println(res);
//        Boolean  aa=checkSHA256(targetStr,"9eff139ce1ac9baa5f1586cfbbf05cded99d49dbc54e13176426c1de2ec540f4");
//        System.out.println(aa);

    }

}
