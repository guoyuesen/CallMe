package com.call.gys.crdeit.callme.updatedownload;

/**
 * Created by 郭月森 on 2018/8/23.
 */

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.view.RoundProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2018/3/9.
 */

public class DownFileHelper {

    Handler handler;
    Context mContext;
    NotificationManager mNotifyManager;
    Notification.Builder builder;
    RoundProgressBar bar;
    TextView ctext;
    AlertDialog dialog;
    Handler diahandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bar.setProgress(msg.what);
        }
    };
    public DownFileHelper(Context mContext, Handler handler) {
        this.handler = handler;
        this.mContext = mContext;
    }
    /**
     * 下载最新版本的apk
     *
     * @param path apk下载地址
     */
    public void downFile(final String path,String str) {
        mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap btm = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.loging_icon);//可以换成你的app的logo
        dialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_roundprogressbar, null);
        bar = view.findViewById(R.id.bar);
        ctext = view.findViewById(R.id.bar_context);
        ctext.setText(str);
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();
        if (Build.VERSION.SDK_INT >= 26) {
            //创建 通知通道  channelid和channelname是必须的（自己命名就好）
            NotificationChannel channel = new NotificationChannel("123",
                    "金陀螺", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN);//小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            mNotifyManager.createNotificationChannel(channel);

            builder = new Notification.Builder(mContext, "1");
            //设置通知显示图标、文字等
            builder.setSmallIcon(R.mipmap.loging_icon)//可以换成你的app的logo
                    .setLargeIcon(btm)
                    .setTicker("正在下载")
                    .setContentTitle("我的app")
                    .setAutoCancel(true)
                    .build();
            mNotifyManager.notify(1, builder.build());

        } else {
            builder = new Notification.Builder(mContext);
            builder.setSmallIcon(R.mipmap.loging_icon)//可以换成你的app的logo
                    .setLargeIcon(btm)
                    .setTicker("正在下载")
                    .setContentTitle("我的app")
                    .setAutoCancel(true)//可以滑动删除通知栏
                    .build();
            mNotifyManager.notify(1, builder.build());
        }
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept-Encoding", "identity");
                    con.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
                    if (con.getResponseCode() == 200) {
                        int length = 3500000;// 获取文件大小
                        InputStream is = con.getInputStream();

                        FileOutputStream fileOutputStream = null;
                        if (is != null) {
                            //对apk进行保存
                            File file = new File(Environment.getExternalStorageDirectory()
                                    .getPath(), "callmi.apk");
                            fileOutputStream = new FileOutputStream(file);
                            byte[] buf = new byte[1024];
                            int ch;
                            int process = 0;
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            // 设置精确到小数点后2位
                            numberFormat.setMaximumFractionDigits(2);
                            String result;
                            float r;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);
                                process += ch;
                                if (process > length) {
                                    //更新进度条
                                    result = numberFormat.format((float) 99.6);
                                    r = 99.6f;
                                } else {
                                    //更新进度条
                                    result = numberFormat.format((float) process / (float) length * 100);
                                    r = (float) process / (float) length * 100;
                                }
                                Message m = new Message();
                                m.what = (int) r;
                                diahandler.sendMessage(m);
                                builder.setContentText("下载进度：" + result + "%");
                                builder.setProgress(length, process, false);
                                mNotifyManager.notify(1, builder.build());
                            }
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        //apk下载完成，使用Handler()通知安装apk
                        builder.setProgress(length, length, false);
                        builder.setContentText("已经下载完成");
                        mNotifyManager.notify(1, builder.build());
                        mNotifyManager.cancelAll();
                        handler.sendEmptyMessage(0);
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

}
