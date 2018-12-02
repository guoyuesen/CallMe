package com.call.gys.crdeit.callme.service;

/**
 * Created by 郭月森 on 2018/11/4.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.call.gys.crdeit.callme.utils.MyPhoneStateListener;

import java.util.Date;

public class PhoneReceiver extends BroadcastReceiver {
    // 设置一个监听器
    MyPhoneStateListener listener = null;
    NullNotf nullNotf = null;
    public PhoneReceiver() {
    }

    public PhoneReceiver(MyPhoneStateListener listener,NullNotf nullNotf) {
        this.listener = listener;
        this.nullNotf = nullNotf;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是去电
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

            //if (PlayerService.player!=null && PlayerService.player.musicIsPlaying()) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Service.TELEPHONY_SERVICE);
                tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            //}

        } else {
            // 查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
            // 如果我们想要监听电话的拨打状况，需要这么几步 :
            //if (player.player!=null && player.musicIsPlaying()) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Service.TELEPHONY_SERVICE);
                try {
                    tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
                }catch (NullPointerException e){
                    //nullNotf.notf();
                }
            //}

        }
    }


    public interface NullNotf{
        void notf();
    }

}

