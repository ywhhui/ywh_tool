package com.szcgc.cougua.utils;

public class FiveSwap implements SwapTest {

    public String swap(int a) {
        if(a%5==0){
            return "B";
        }
        return null;
    }
}
