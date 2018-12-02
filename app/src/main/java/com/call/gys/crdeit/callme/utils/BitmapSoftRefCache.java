package com.call.gys.crdeit.callme.utils;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by 郭月森 on 2018/6/21.
 */

public class BitmapSoftRefCache implements ImageLoader.ImageCache {
    private static final String TAG="BitmapSoftRefCache";
    //创建一个集合保存Bitmap
    private LinkedHashMap<String,SoftReference<Bitmap>> map;

    public BitmapSoftRefCache(){
        map=new LinkedHashMap<>();
    }

    /**
     * 根据图片url从缓存中拿出bitmap
     */
    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap=null;
        SoftReference<Bitmap> softRef=map.get(url);
        if(softRef!=null){
            bitmap=softRef.get();
            if(bitmap==null){
                //从集合中移除
                map.remove(url);
            }
        }
        return null;
    }

    /**
     * 把图片放进缓存中
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        SoftReference<Bitmap> softRef=new SoftReference<Bitmap>(bitmap);
        map.put(url, softRef);
    }

}
