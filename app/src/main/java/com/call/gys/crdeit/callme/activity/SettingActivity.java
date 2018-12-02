package com.call.gys.crdeit.callme.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/11/20.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.version)
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        hiedBar(this);
        ButterKnife.bind(this);
        new TitleBuder(this).setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).setTitleText("设置");
    }

    @OnClick({R.id.update_password, R.id.update_tousu, R.id.update_back_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update_password:
                startActivity(new Intent(this,UpdatePassActivity.class));
                break;
            case R.id.update_tousu:
                break;
            case R.id.update_back_login:
                //UserModel.remov();
                SharedPreferences sp = getSharedPreferences("SP_PEOPLE", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("KEY_LOGING_PHONE", "") ; //存入json串
                editor.putString("KEY_LOGING_PASS", "") ;
                editor.commit() ; //提交
                Intent intent = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
