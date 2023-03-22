package com.szcgc.cougua.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 常用操作总结
 */
public class ListUtils {


    public static void main(String[] args) {
        try {
           List<String> list = new ArrayList<>();
           list.add("1");
           list.add("11");
           list.add("111");
           Random random = new Random();
           System.out.println("随机一个 str:"+list.get(random.nextInt(list.size())));
        } catch (Exception e) {
            System.out.println("异常e:"+e);
        }

    }
}
