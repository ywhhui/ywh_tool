package com.szcgc.cougua.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    public static String getDateStrByTimeStamp(long timeStamp){
        //当前时间毫秒的时间戳转换为日期
        Date millisecondDate= new Date(System.currentTimeMillis());
//格式化时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String millisecondStrings = formatter.format(millisecondDate);
        return millisecondStrings;
    }

    public static void main(String[] args) {
        getDateStrByTimeStamp(1l);
    }
}
