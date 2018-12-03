package com.db.util;

import java.util.*;

public class DateUtil {


    public static List<Map<String, Object>> completeWeek(List<Map<String, Object>> list) {
        Calendar calendar = Calendar.getInstance();
        //获得当前日期是一个星期的第几天
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (1 == dayOfWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //获得当前日期是一个星期的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        //根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
        int maxLength = 7;
        List<Map<String, Object>> weekList = new ArrayList<>(7);
        List<Map<String, Object>> newList = new ArrayList<>(7);
        for (int i = 1; i <= maxLength; i++) {
            int week = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Map<String, Object> map = new HashMap<>(1);
            map.put("time", week);
            weekList.add(map);
        }
        int listSize = list.size();
        if (listSize > 0) {
            boolean a = false;
            for (int i = 0; i < weekList.size(); i++) {
                int week = (Integer) weekList.get(i).get("time");
                //如果有值
                for (int j = 0; j < list.size(); j++) {
                    if (list.get(j).containsValue(week)) {
                        Map<String, Object> map = new HashMap<>(1);
                        map.putAll(list.get(j));
                        newList.add(map);
                        a = true;
                        break;
                    }
                }
                if (a) {
                    a = false;
                    continue;
                }
                Map<String, Object> map = new HashMap<>(1);
                map.putAll(weekList.get(i));
                newList.add(map);
            }
        } else {
            return weekList;
        }
        return newList;
    }
}
