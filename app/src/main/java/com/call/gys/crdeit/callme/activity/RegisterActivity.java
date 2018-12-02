package com.call.gys.crdeit.callme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/11/8.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.com_name)
    EditText comName;
    @BindView(R.id.com_cor)
    EditText comCor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        hiedBar(this);
        new TitleBuder(this).setTitleText("企业信息");

    }

    @OnClick(R.id.register_next)
    public void onViewClicked() {
        String com = comName.getText().toString();
        String cor = comCor.getText().toString();
        if (com.isEmpty()){
            Toast.makeText(this,"请输入企业名称",Toast.LENGTH_LONG).show();
            return;
        }
        if (cor.isEmpty()){
            Toast.makeText(this,"请输入法人姓名",Toast.LENGTH_LONG).show();
            return;
        }
        RegisterActivity2.comName  = com;
        RegisterActivity2.comCor = cor;
        startActivity(new Intent(RegisterActivity.this, RegisterActivity2.class));
    }
}
