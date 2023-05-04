package com.szcgc.cougua.utils;

import com.szcgc.cougua.vo.DashBoardEvent;
import com.szcgc.cougua.vo.DashBoardEventValue;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 常用操作总结
 */
public class ListUtils {


    public static void main(String[] args) {
        try {
//            List<String> list = new ArrayList<>();
//            list.add("1");
//            list.add("11");
//            list.add("111");
//            Random random = new Random();
//            System.out.println("随机一个 str:" + list.get(random.nextInt(list.size())));
            List<DashBoardEvent> list = new ArrayList<>();
            List<DashBoardEventValue> index = new ArrayList<>();
            DashBoardEventValue dashBoardEventValue = new DashBoardEventValue();
            dashBoardEventValue.setIndexType("1");
            dashBoardEventValue.setIndexValue("1");
            DashBoardEventValue dashBoardEventValue1 = new DashBoardEventValue();
            dashBoardEventValue1.setIndexType("1");
            dashBoardEventValue1.setIndexValue("2");
            index.add(dashBoardEventValue);
            index.add(dashBoardEventValue1);
//            index.add(dashBoardEventValue);

            DashBoardEvent dashBoardEvent = new DashBoardEvent();
            dashBoardEvent.setIndex(index);
            list.add(dashBoardEvent);

            DashBoardEvent dashBoardEvent1 = new DashBoardEvent();
            dashBoardEvent1.setIndex(index);
            list.add(dashBoardEvent1);

            derecomBinationIndex2(list);
        } catch (Exception e) {
            System.out.println("异常e:" + e);
        }

    }

    /**
     * list去重 并合并相同key的值 方法一 list正反循环去去重 匹配相同的合并
     * @param list
     * @return
     */
    private static List derecomBinationIndex(List<DashBoardEvent> list) {

        if (!CollectionUtils.isEmpty(list)) {
            //根据appID和source 去重合并
            for (int i = 0; i < list.size(); i++) {
                List<DashBoardEventValue> dashBoardEventValueList = list.get(i).getIndex();
                //判断appID和source是否相等 相等进行去重合并
                if (!CollectionUtils.isEmpty(dashBoardEventValueList)) {
                    for (int y = 0; y < dashBoardEventValueList.size() - 1; y++) {
                        for (int k = dashBoardEventValueList.size() - 1; k > y; k--) {
                            if (dashBoardEventValueList.get(k).getIndexType().equals(dashBoardEventValueList.get(y).getIndexType())) {
                                dashBoardEventValueList.get(y).setIndexValue(dashBoardEventValueList.get(k).getIndexValue() + dashBoardEventValueList.get(y).getIndexValue());
                                dashBoardEventValueList.remove(k);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * list去重 并合并相同key的值 方法二 先转map 去重+合并 再把map转list
     * @param list
     * @return
     */
    private static List derecomBinationIndex2(List<DashBoardEvent> list) {
        List<DashBoardEvent> listResult = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            //根据appID和source 去重合并
            for (int i = 0; i < list.size(); i++) {
                List<DashBoardEventValue> dashBoardEventValueList = list.get(i).getIndex();
                System.out.println(dashBoardEventValueList);
                //判断appID和source是否相等 相等进行去重合并
                if (!CollectionUtils.isEmpty(dashBoardEventValueList)) {
                    //先转map 过滤重复key 并合并相同key的val1+val2
                    Map<String, String> map = dashBoardEventValueList.stream().collect(Collectors.toMap(DashBoardEventValue::getIndexType, DashBoardEventValue::getIndexValue, (val1, val2) -> val1+val2));
                    //再map转list
                    System.out.println(map);
                    List<DashBoardEventValue> collect = map.entrySet().stream().map(m ->{
                        DashBoardEventValue value = new DashBoardEventValue();
                        value.setIndexType(m.getKey());
                        value.setIndexValue(m.getValue());
                        return value;
                            }
                      ).collect(Collectors.toList());
                    System.out.println(collect);
                    DashBoardEvent dashBoardEvent = new DashBoardEvent();
                    dashBoardEvent.setIndex(collect);
                    listResult.add(dashBoardEvent);
                }
            }
        }
        return listResult;
    }

}