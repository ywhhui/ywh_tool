package com.szcgc.cougua.utils;

import java.util.*;

/**
 * 常用操作总结
 */
public class MapUtils {


    public static void main(String[] args) {
        try {
            Map<String, Integer> map = new HashMap();
            map.put("a1", 8);
            map.put("b2", 32);
            map.put("c3", 53);
            map.put("d4", 33);
            map.put("e5", 11);
            map.put("g7", 3);
            map.put("f6", 3);
            List<Map.Entry<String,Integer>> list = new ArrayList(map.entrySet());
            Collections.sort(list, (o1, o2) -> (o1.getValue() - o2.getValue()));//升序
            String key = list.get(0).getKey();
            System.out.println("最小的key:"+key);
        } catch (Exception e) {
            System.out.println("异常e:"+e);
        }

    }
}
