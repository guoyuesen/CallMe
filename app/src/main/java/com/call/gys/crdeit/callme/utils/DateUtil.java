package com.call.gys.crdeit.callme.utils;


import com.call.gys.crdeit.callme.model.CalendarMode;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {
    public static String dataTostr(Date date){
        String str="";
        java.text.DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        str = format1.format(date);
        return str;
    }
    public static String timeStamp2Date(String seconds) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(Long.valueOf(seconds)));
    }
    public static CalendarMode getCalendar(){
        CalendarMode mode = new CalendarMode();
        Calendar cal=Calendar.getInstance();
        mode.setY(cal.get(Calendar.YEAR));
        mode.setM(cal.get(Calendar.MONTH)+1);
        mode.setD(cal.get(Calendar.DATE));
        mode.setW(cal.get(Calendar.DAY_OF_WEEK));
        /*h=cal.get(Calendar.HOUR_OF_DAY);
        mi=cal.get(Calendar.MINUTE);
        s=cal.get(Calendar.SECOND);

        System.out.println("现在时刻是"+y+"年"+m+"月"+d+"日"+h+"时"+mi+"分"+s+"秒");
        String c = y+"-"+m+"-"+d;*/
        return mode;
    }
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    /*
    获取当日0点0分时间戳
     */
    public static long getMidnight(){
        CalendarMode mode = getCalendar();
        String str = mode.getY()+"-"+mode.getM()+"-"+mode.getD();

        try {
            return format.parse(str).getTime();
        } catch (ParseException e) {
            return new Date().getTime();
        }
    }
    /*
    获取昨日0点0分时间戳
     */
    public static String getYesterday(){
        CalendarMode mode = getCalendar();
        String str = mode.getY()+"-"+mode.getM()+"-"+mode.getD();
        System.out.println("昨日:"+str);
        try {
            return format.parse(str).getTime()-24*60*60*1000+"";
        } catch (ParseException e) {
            return new Date().getTime()+"";
        }
    }
    /*
    获取当月1日时间戳
     */
    public static String getMonth(){
        CalendarMode mode = getCalendar();
        String str = mode.getY()+"-"+mode.getM()+"-01";
        System.out.println("当月1日:"+str);
        try {
            return format.parse(str).getTime()+"";
        } catch (ParseException e) {
            return new Date().getTime()+"";
        }
    }
    /*
   获取本周一时间戳
    */
    public static String getWeek(){
        CalendarMode mode = getCalendar();
        String str = mode.getY()+"-"+mode.getM()+"-"+mode.getD();
        System.out.println("当日:"+str);
        System.out.println("星期几:"+mode.getW());
        try {
            return format.parse(str).getTime()-(mode.getW()==1?6:mode.getW()-2)*24*60*60*1000+"";
        } catch (ParseException e) {
            return new Date().getTime()+"";
        }
    }

    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        String xq = "";
        switch (hour){
            case 1:
                xq = "周日";
                break;
            case 2:
                xq = "周一";
                break;
            case 3:
                xq = "周二";
                break;
            case 4:
                xq = "周三";
                break;
            case 5:
                xq = "周四";
                break;
            case 6:
                xq = "周五";
                break;
            case 7:
                xq = "周六";
                break;
        }
        return xq;
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
}
