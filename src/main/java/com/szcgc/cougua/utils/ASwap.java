package com.szcgc.cougua.utils;

public class ASwap implements SwapTest {

    public String swap(int a) {
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
}
