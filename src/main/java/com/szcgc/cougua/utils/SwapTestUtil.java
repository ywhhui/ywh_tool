package com.szcgc.cougua.utils;

public class SwapTestUtil {
    
    public static String swap(int a) {
        StringBuilder str = new StringBuilder();
        if(a==0){
            return str.toString();
        }
        if(a%3==0){
            str.append("A");
        }
        if(a%5==0){
            str.append("B");
        }
        if(a%7==0){
            str.append("C");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            String swap = swap(i);
            System.out.println("结果："+swap +i);
        }
    }




}
