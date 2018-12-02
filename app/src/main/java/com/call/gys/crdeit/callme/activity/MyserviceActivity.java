package com.call.gys.crdeit.callme.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;

import butterknife.ButterKnife;

/**
 * Created by 郭月森 on 2018/11/20.
 */

public class MyserviceActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        hiedBar(this);
        ButterKnife.bind(this);
        new TitleBuder(this).setTitleText("我的服务").setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
