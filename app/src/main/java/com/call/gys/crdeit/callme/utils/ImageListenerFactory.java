package com.call.gys.crdeit.callme.utils;

import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 郭月森 on 2018/6/21.
 */

public class ImageListenerFactory {
    private static final String TAG="ImageListenerFactory";

    public static ImageLoader.ImageListener getImageListener(final ImageView view,
                                                             final int defaultImageResId, final int errorImageResId){
        return new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(errorImageResId!=0){
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if(response.getBitmap()!=null){
                    if(view.getTag().toString().equals(response.getRequestUrl())){
                        view.setImageBitmap(response.getBitmap());
                    }
                }
                else if(defaultImageResId!=0){
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }
}
