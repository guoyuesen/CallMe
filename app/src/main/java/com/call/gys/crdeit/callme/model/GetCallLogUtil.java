package com.call.gys.crdeit.callme.model;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 郭月森 on 2018/11/5.
 */

public class GetCallLogUtil {
    /**
     * 利用系统CallLog获取通话历史记录
     * @param activity
     * @param num  要读取记录的数量
     * @return
     */
   /* public static int getCallHistoryList(Activity activity, int num) {
        Cursor cs;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CALL_LOG}, 1000);

        }
        cs = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION,   //通话时长
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_today = simpleDateFormat.format(date);
            int callDuration = 0;
            for (cs.moveToFirst(); (!cs.isAfterLast()) && i < num; cs.moveToNext(), i++) {
                String callName = cs.getString(0);  //名称
                String callNumber = cs.getString(1);  //号码
                //如果名字为空，在通讯录查询一次有没有对应联系人
                if (callName == null || callName.equals("")){
                    String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
                    //设置查询条件
                    String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='"+callNumber+"'";
                    Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            cols, selection, null, null);
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    if (cursor.getCount()>0){
                        cursor.moveToFirst();
                        callName = cursor.getString(nameFieldColumnIndex);
                    }
                    cursor.close();
                }
                //通话类型
                int callType = Integer.parseInt(cs.getString(2));
                String callTypeStr = "";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "来电";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "去电";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "不知道";
                        break;
                    default:
                        //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
                        Log.i("ssss",""+callType);
                        i--;
                        continue;
                }
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date callDate = new Date(Long.parseLong(cs.getString(3)));
                String callDateStr = sdf.format(callDate);
                if (callDateStr.equals(date_today)) { //判断是否为今天
                    sdf = new SimpleDateFormat("HH:mm");
                    callDateStr = sdf.format(callDate);
                } else if (date_today.contains(callDateStr.substring(0, 7))) { //判断是否为当月
                    sdf = new SimpleDateFormat("dd");
                    int callDay = Integer.valueOf(sdf.format(callDate));

                    int day = Integer.valueOf(sdf.format(date));
                    if (day - callDay == 1) {
                        callDateStr = "昨天";
                    } else {
                        sdf = new SimpleDateFormat("MM-dd");
                        callDateStr = sdf.format(callDate);
                    }
                } else if (date_today.contains(callDateStr.substring(0, 4))) { //判断是否为当年
                    sdf = new SimpleDateFormat("MM-dd");
                    callDateStr = sdf.format(callDate);
                }

                //通话时长
                callDuration = Integer.parseInt(cs.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = "";
                if (sec > 0) {
                    if (min > 0) {
                        callDurationStr = min + "分" + sec + "秒";
                    } else {
                        callDurationStr = sec + "秒";
                    }
                }

                *//**
                 * callName 名字
                 * callNumber 号码
                 * callTypeStr 通话类型
                 * callDateStr 通话日期
                 * callDurationStr 通话时长
                 * 请在此处执行相关UI或存储操作，之后会查询下一条通话记录
                 *//*
                Log.i("Msg","callName"+callName);
                Log.i("Msg","callNumber"+callNumber);
                Log.i("Msg","callTypeStr"+callTypeStr);
                Log.i("Msg","callDateStr"+callDateStr);
                Log.i("Msg","callDurationStr"+callDurationStr);

            }
            return callDuration;
        }else {
            return 0;
        }

    }*/

    public static int getRecordForMiui(Activity activity){
        /*if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CALL_LOG}, 1000);

        }*/
        ContentResolver contentResolver = activity.getContentResolver();
        //List<RecordEntity> mRecordList = new ArrayList<RecordEntity>();
        int co = 0;
        //使用具体的路径而不是引用CallLog.Calls.CONTENT_URI
        Uri uri = Uri.parse("content://call_log/calls");
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(
                    // CallLog.Calls.CONTENT_URI, Columns, null,
                    // null,CallLog.Calls.DATE+" desc");
                    uri, null, null, null,
                    CallLog.Calls.DATE + " desc");
            if (cursor == null)
                return 0;
            if (cursor.moveToNext()){
                co = (int) cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
            }
            /*while (cursor.moveToNext()) {
                RecordEntity record = new RecordEntity();
                record.name = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.CACHED_NAME));
                record.number = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                record.type = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                record.lDate = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                record.duration = cursor.getLong(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));
                record._new = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
                Log.e("aaa", record.toString());
//                      int photoIdIndex = cursor.getColumnIndex(CACHED_PHOTO_ID);
//                      if (photoIdIndex >= 0) {
//                          record.cachePhotoId = cursor.getLong(photoIdIndex);
//                      }

                mRecordList.add(record);
            }*/
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return co;
        }
    }

}
