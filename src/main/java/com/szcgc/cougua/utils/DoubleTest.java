package com.szcgc.cougua.utils;

/**
 * "" 空指针异常
 */
public class DoubleTest {

    public static void main(String[] args) {
        //重现"" 报null异常
        double v = Double.parseDouble("");
        System.out.println(v);
    }
}
