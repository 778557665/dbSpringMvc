package com.db.tk;

import com.zhibi.taoke.constant.TimeUnit;
import com.zhibi.taoke.enums.DateTypeEnum;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 时间处理类
 */
public class DateUtil {
    public static SimpleDateFormat dd = new SimpleDateFormat("dd");
    public static SimpleDateFormat YM = new SimpleDateFormat("yyyy_MM");
    public static SimpleDateFormat YMd = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat YMd_noSpli = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat YMd_cn = new SimpleDateFormat("yyyy年MM月dd日");
    public static SimpleDateFormat YMdhm_cn = new SimpleDateFormat("yyyy年M月d日 H时m分");
    public static SimpleDateFormat YMdhms_cn = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
    public static SimpleDateFormat Hms_cn = new SimpleDateFormat("HH时mm分ss秒");
    public static SimpleDateFormat YMdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat YMdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat YMdhms_noSpli = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat YMdhmsS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static SimpleDateFormat YMdhmsS_noSpli = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static SimpleDateFormat YMdhm_noSpli = new SimpleDateFormat("yyyyMMddHHmm");
    private static ThreadLocal<Map<String, SimpleDateFormat>> sdfTL = new ThreadLocal();

    public DateUtil() {
    }

    public static Date strToDate(String str) {
        return strToDate(str, YMdhms);
    }

    public static Date strToDate(String str, SimpleDateFormat sdf) {
        Map<String, SimpleDateFormat> sdfMap = (Map) sdfTL.get();
        if (sdfMap == null) {
            sdfMap = generateSdfMap();
            sdfTL.set(sdfMap);
        }

        Date date = null;
        if (str != null) {
            try {
                date = ((SimpleDateFormat) sdfMap.get(sdf.toPattern())).parse(str);
            } catch (Exception var5) {
                throw new RuntimeException("DateFormat Exception!", var5);
            }
        }

        return date;
    }

    public static String dateToStr() {
        return dateToStr(new Date(), YMdhms);
    }

    public static String dateToStr(Date date) {
        return dateToStr(date, YMdhms);
    }

    public static String dateToStr(Date date, SimpleDateFormat sdf) {
        if (date != null && sdf != null) {
            Map<String, SimpleDateFormat> sdfMap = (Map) sdfTL.get();
            if (sdfMap == null) {
                sdfMap = generateSdfMap();
                sdfTL.set(sdfMap);
            }

            try {
                return ((SimpleDateFormat) sdfMap.get(sdf.toPattern())).format(date);
            } catch (Exception var4) {
                throw new RuntimeException("DateFormat Exception!", var4);
            }
        } else {
            return null;
        }
    }

    public static Date addDayToDate(Date date, int amount) {
        if (null == date) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(5, amount);
            return c.getTime();
        }
    }

    public static Date addHourToDate(Date date, int amount) {
        if (null == date) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(11, amount);
            return c.getTime();
        }
    }

    public static Date addMinuteToDate(Date date, int amount) {
        if (null == date) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(12, amount);
            return c.getTime();
        }
    }

    public static Date addSecondToDate(Date date, int amount) {
        if (null == date) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(13, amount);
            return c.getTime();
        }
    }

    public static Date getTheEndOfADay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        c.set(14, 0);
        return c.getTime();
    }

    public static Date getTheBeginOfADay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        return c.getTime();
    }

    public static Integer getTheHourOfADay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(11);
    }

    public static int daysBetween(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / 86400000L);
    }

    public static int dayNumberBetween(Date start, Date end) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(start);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        int day1 = cal1.get(6);
        int day2 = cal2.get(6);
        int year1 = cal1.get(1);
        int year2 = cal2.get(1);
        int result;
        if (year1 != year2) {
            int timeDistance = 0;

            for (int i = year1; i < year2; ++i) {
                if ((i % 4 != 0 || i % 100 == 0) && i % 400 != 0) {
                    timeDistance += 365;
                } else {
                    timeDistance += 366;
                }
            }

            result = timeDistance + (day2 - day1);
        } else {
            result = day2 - day1;
        }

        return result;
    }

    public static long calculateTimeToMidnight() {
        long t1 = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.add(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        long t2 = c.getTimeInMillis();
        return t2 - t1;
    }

    public static Date generateRandomDateBeforeToday(int offset) {
        Date date = new Date();
        Random random = new Random();
        date = addDayToDate(date, -random.nextInt(offset));
        return date;
    }

    public static Date getYesterday() {
        Calendar c = Calendar.getInstance();
        c.add(5, -1);
        return c.getTime();
    }

    public static Date getYesterdayTimeStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, -1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date getYesterdayTimeEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, -1);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 999);
        return calendar.getTime();
    }

    public static Date getYesterdayTimeEnd2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, -1);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTime();
    }

    public static Date getTodayTimeStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date getTodayTimeEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 999);
        return calendar.getTime();
    }

    public static Date getTodayTimeEnd2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        return calendar.getTime();
    }

    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        } else {
            int yearNow = cal.get(1);
            int monthNow = cal.get(2);
            int dayOfMonthNow = cal.get(5);
            cal.setTime(birthDay);
            int yearBirth = cal.get(1);
            int monthBirth = cal.get(2);
            int dayOfMonthBirth = cal.get(5);
            int age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        --age;
                    }
                } else {
                    --age;
                }
            }

            return age;
        }
    }

    private static Map<String, SimpleDateFormat> generateSdfMap() {
        Map<String, SimpleDateFormat> map = new HashMap();
        map.put("yyyy_MM", YM);
        map.put("yyyy-MM-dd", YMd);
        map.put("yyyyMMdd", YMd_noSpli);
        map.put("yyyy年MM月dd日", YMd_cn);
        map.put("yyyy年M月d日 H时m分", YMdhm_cn);
        map.put("yyyy年MM月dd日 HH时mm分ss秒", YMdhms_cn);
        map.put("HH时mm分ss秒", Hms_cn);
        map.put("yyyy-MM-dd HH:mm", YMdhm);
        map.put("yyyy-MM-dd HH:mm:ss", YMdhms);
        map.put("yyyyMMddHHmmss", YMdhms_noSpli);
        map.put("yyyy-MM-dd HH:mm:ss.SSS", YMdhmsS);
        map.put("yyyyMMddHHmmssSSS", YMdhmsS_noSpli);
        map.put("yyyyMMddHHmm", YMdhm_noSpli);
        return map;
    }

    public static String getBeginTimeStr(Date time) {
        return dateToStr(getTheBeginOfADay(time));
    }

    public static String getEndTimeStr(Date time) {
        return dateToStr(getTheEndOfADay(time));
    }

    public static String getWeekBeginTimeStr() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getBeginTimeStr(calendar.getTime());
    }

    public static int getWeekBeginDay() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getWeekEndTimeStr() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return getEndTimeStr(calendar.getTime());
    }

    public static String getMouthBeginTimeStr() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getBeginTimeStr(calendar.getTime());
    }

    public static String getMouthEndTimeStr() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int maxCurrentMonthDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
        return getEndTimeStr(calendar.getTime());
    }

    /**
     * 获取一个月的总天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Map<String, String> getTimeByDateTypeEnum(Integer dateType) {
        Map<String, String> map = new HashMap<>();
        String begin;
        String end;
        Date time;
        String timeUnit = TimeUnit.HOUR;
        DateTypeEnum dateTypeEnum = DateTypeEnum.getEnumByCode(dateType);
        switch (dateTypeEnum) {
            case TODAY:
                time = new Date();
                begin = getBeginTimeStr(time);
                end = getEndTimeStr(time);
                break;
            case YESTERDAY:
                time = getYesterday();
                begin = getBeginTimeStr(time);
                end = getEndTimeStr(time);
                break;
            case THIS_WEEK:
                begin = getWeekBeginTimeStr();
                end = getWeekEndTimeStr();
                timeUnit = TimeUnit.DAY;
                break;
            default:
                begin = getMouthBeginTimeStr();
                end = getMouthEndTimeStr();
                timeUnit = TimeUnit.DAY;
                break;
        }
        map.put("begin", begin);
        map.put("end", end);
        map.put("timeUnit", timeUnit);
        return map;
    }

    public static List<Map<String, Object>> completeTime(Integer dateType, Map<String, String> timeMap, List<Map<String, Object>> list) {
        Date now = new Date();
        if (dateType > DateTypeEnum.YESTERDAY.getCode()) {
            return addDayForNull(list, strToDate(timeMap.get("begin")), strToDate(timeMap.get("end")));
        }
        if (dateType.equals(DateTypeEnum.YESTERDAY.getCode())) {
            now = strToDate(timeMap.get("end"));
        }
        int prevTime = -1;
        int time = -1;
        int maxTime = 24;
        List<Map<String, Object>> timeList = new ArrayList<>(24);
        int field = Calendar.HOUR_OF_DAY;
        // 给未到当前时间的,无注册记录的时间点添加记录
        for (Map<String, Object> map : list) {
            Integer hour = (Integer) map.get("time");
            if (null == hour) {
                list = new ArrayList<>();
                break;
            }
            while (hour - prevTime > 1) {
                prevTime++;
                Map<String, Object> hourMap = new HashMap<>(1);
                hourMap.put("time", prevTime);
                timeList.add(hourMap);
            }
            timeList.add(map);
            prevTime = hour;
        }
        if (timeList.size() < maxTime) {
            if (!CollectionUtils.isEmpty(list)) {
                Map<String, Object> map = list.get(list.size() - 1);
                time = (Integer) map.get("time");
            }

            Calendar c = Calendar.getInstance();
            c.setTime(getTheBeginOfADay(now));
            c.set(field, ++time);
            while (c.getTime().before(now) && time < maxTime) {
                Map<String, Object> hourMap = new HashMap<>(1);
                hourMap.put("time", c.get(field));
                timeList.add(hourMap);
                c.add(field, 1);
                time++;
            }
        }
        return timeList;
    }

    public static List<Map<String, Object>> addDayForNull(List<Map<String, Object>> oldList, Date start, Date end) {
        long n = daysBetween(start, end);
        List<Map<String, Object>> newList = new ArrayList<>();
        int num = oldList.size();
        int temp = 0;
        for (int i = 0; i <= n; i++) {
            if (temp < num) {
                Date dateStr = strToDate((String) (oldList.get(temp).get("time")), dd);
                //如果开始日期小于当前记录日期则增加空白数据
                if (start.compareTo(dateStr) < 0) {
                    Map<String, Object> map = new HashMap<>();
                    start.getTime();
                    map.put("time", dd.format(start));
                    newList.add(map);
                }
                //原数据加入
                if (start.compareTo(dateStr) == 0) {
                    newList.add(oldList.get(temp));
                    System.out.println(oldList.get(temp));
                    temp++;
                }
                //加入空数据直到结束时间
            } else if (temp >= num && start.compareTo(end) <= 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("time", dd.format(start));
                newList.add(map);
            }
            //开始时间向前加一天
            start = addDateOneDay(start);
        }
        return newList;
    }

    public static Date addDateOneDay(Date date) {
        if (null == date) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.DATE, 1); //日期加1天
//     c.add(Calendar.DATE, -1); //日期减1天
        date = c.getTime();
        return date;
    }
}
