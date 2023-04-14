package com.szcgc.cougua.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 密码破解
 */
public class PassWordUtil {

    /**
     * 数字
     */
    public static final String NUMBER = "0123456789";
    /**
     * 字母
     */
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwyxz";
    /**
     * 符号
     */
    public static final String SYMBOL = "~!@#$%^&*()_+[]{};,.<>?-=";

    /**
     * 获取指定的字符
     *
     * @param includeNumber   是否包含数字
     * @param includeAlphabet 是否包含字母
     * @param includeSymbol   是否包含字符
     * @param length          字符长度
     * @return
     */
    public static List<String> getStr(boolean includeNumber, boolean includeAlphabet, boolean includeSymbol, int length) {

        List<String> result = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        if (includeNumber) {
            sb.append(NUMBER);
        }
        if (includeAlphabet) {
            sb.append(ALPHABET);
            //字母大写
//            sb.append(ALPHABET.toUpperCase());
        }
        if (includeSymbol) {
            sb.append(SYMBOL);
        }
        if (sb.length() <= length) {
            result.add(sb.toString());
        }
        char[] chars = sb.toString().toCharArray();
        String[] strings = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            strings[i] = String.valueOf(chars[i]);
        }
        String[] allLists = getAllLists(strings, length);
        return Arrays.asList(allLists);
    }

    /**
     * 获取指定位数的数据集合
     *
     * @param elements 基类字符数组
     * @param length   指定字符串位数
     * @return
     */
    public static String[] getAllLists(String[] elements, int length) {
        String[] allLists = new String[(int) Math.pow(elements.length, length)];
        if (length == 1) {
            return elements;
        } else {
            String[] allSublists = getAllLists(elements, length - 1);
            int arrayIndex = 0;
            for (int i = 0; i < elements.length; i++) {
                for (int j = 0; j < allSublists.length; j++) {
                    allLists[arrayIndex] = elements[i] + allSublists[j];
                    arrayIndex++;
                }
            }
            return allLists;
        }
    }

    /**
     * 获取全部字符集合，包含数字，字母，特殊字符
     *
     * @param length
     * @return
     */
    public static List<String> getFullStr(int length) {
        return getStr(true, true, true, length);
    }

    /**
     * 获取数字字符集合
     *
     * @param length
     * @return
     */
    public static List<String> getNumberStr(int length) {
        return getStr(true, false, false, length);
    }

    /**
     * 获取字母字符集合
     *
     * @param length
     * @return
     */
    public static List<String> getAlphabetStr(int length) {
        return getStr(false, true, false, length);
    }

    /**
     * 获取特殊符号字符集合
     *
     * @param length
     * @return
     */
    public static List<String> getSymbolStr(int length) {
        return getStr(false, false, true, length);
    }


    /**
     * 获取数字+字母字符集合
     *
     * @param length
     * @return
     */
    public static List<String> getNoA(int length) {
        return getStr(true, true, false, length);
    }
    public static void main(String[] args) {
//        final List<String> str = getNumberStr(4);
//        System.out.println(str.toString());
        List<String> noA = getNoA(4);
        System.out.println("shuzi----"+noA.size());
//        List<String> numberStr8 = getNumberStr(8);
//        System.out.println("shuzi8"+numberStr8);

//        List<String> noA2 = getNoA(5);
//        System.out.println("noA2----"+noA2.size());

        List<String> alphabetStr = getAlphabetStr(4);
        System.out.println(alphabetStr.size());
        List<String> alphabetStr1 = getAlphabetStr(5);
        System.out.println(alphabetStr1.size());
    }
}
