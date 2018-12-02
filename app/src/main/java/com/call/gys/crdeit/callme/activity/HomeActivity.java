package com.call.gys.crdeit.callme.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.adapter.FragmentAdapter;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.fragment.CallRecordFragment;
import com.call.gys.crdeit.callme.fragment.CustomerFragment;
import com.call.gys.crdeit.callme.fragment.RemindFragment;
import com.call.gys.crdeit.callme.fragment.TaskFragment;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.PhoneReceiver;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.updatedownload.DownFileHelper;
import com.call.gys.crdeit.callme.updatedownload.InstallApk;
import com.call.gys.crdeit.callme.utils.Tools;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 郭月森 on 2018/10/22.
 */

public class HomeActivity extends BaseActivity {


    @BindView(R.id.v4_listview)
    ListView v4Listview;
    @BindView(R.id.v4_drawerlayout)
    DrawerLayout v4Drawerlayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private String[] titles = new String[]{"拨号", "客户", "提醒", "任务"};
    private List<String> mTitles;
    private FragmentAdapter adapter;
    //ViewPage选项卡页面列表
    private List<Fragment> mFragments;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (Build.VERSION.SDK_INT >= 26) {
                        boolean b = HomeActivity.this.getPackageManager().canRequestPackageInstalls();
                        if (b) {
                            new InstallApk(HomeActivity.this)
                                    .installApk(new File(Environment.getExternalStorageDirectory(), "callmi.apk"));
                        } else {
                            //请求安装未知应用来源的权限
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 10102);
                        }
                    } else {
                        new InstallApk(HomeActivity.this)
                                .installApk(new File(Environment.getExternalStorageDirectory(), "callmi.apk"));
                    }

                    break;
                case 1:

                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        hiedBar(this);
        initView();
        getV();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initView() {
        if (UserMode.StaffManage.equals("2")) {
            new TitleBuder(this).setLeftImage(R.mipmap.home_title_left).setRightImage(R.mipmap.home_title_reft)
                    .setLeftListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDrawerLayout();
                        }
                    }).setRightListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(HomeActivity.this,StaffManageActivity.class));
                }
            }).setTitleText(UserMode.CompanyName);
        }else {
            new TitleBuder(this).setLeftImage(R.mipmap.home_title_left)
                    .setLeftListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDrawerLayout();
                        }
                    }).setTitleText(UserMode.CompanyName);
        }
        initDate();
     /*   mTitles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTitles.add(titles[i]);
        }*/
        mTitles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mTitles.add(titles[i]);
        }
        mFragments = new ArrayList<>();
        mFragments.add(new CallRecordFragment());
        mFragments.add(new CustomerFragment());
        mFragments.add(new RemindFragment());
        mFragments.add(new TaskFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewpager.setAdapter(adapter);//给ViewPager设置适配器
        tablayout.setupWithViewPager(viewpager);//将TabLayout和ViewPager关联起来
        try {
            settab();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void settab() throws NoSuchFieldException, IllegalAccessException {
        Class<?> mtablayout = tablayout.getClass();
        Field tabStrip = mtablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tablayout);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.setMarginStart(60);
            params.setMarginEnd(60);
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
    private void initDate() {
        View view = LayoutInflater.from(this).inflate(R.layout.home_list_herd,null);
        TextView textView = view.findViewById(R.id.home_list_herd_name);
        textView.setText(UserMode.StaffName);
        TextView textView1 = view.findViewById(R.id.home_list_herd_phone);
        textView1.setText(UserMode.Phone);
        v4Listview.addHeaderView(view);
        final List<String> list = new ArrayList<String>();
        list.add("通话统计");
        list.add("榜上有名");
        list.add("我的服务");
        list.add("客服电话");
        list.add("设置");
        final List<Integer> list1 = new ArrayList<Integer>();
        list1.add(R.mipmap.zuocecaidan02);
        list1.add(R.mipmap.zuocecaidan03);
        list1.add(R.mipmap.zuocecaidan04);
        list1.add(R.mipmap.zuocecaidan05);
        list1.add(R.mipmap.zuocecaidan06);
        ThisAdapter thisAdapter = new ThisAdapter(this,list,list1);
        v4Listview.setAdapter(thisAdapter);
        v4Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        startActivity(new Intent(HomeActivity.this,ConverseCountActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this,RankingListActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this,MyserviceActivity.class));
                        break;
                    case 4:
                        //startActivity(new Intent(HomeActivity.this,MyserviceActivity.class));
                        Toast.makeText(HomeActivity.this,"该功能暂未开放",Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                        break;
                }
                showDrawerLayout();
            }
        });
        //v4Drawerlayout.openDrawer(Gravity.LEFT);//侧滑打开  不设置则不会默认打开
    }

    private void showDrawerLayout() {
        if (!v4Drawerlayout.isDrawerOpen(Gravity.LEFT)) {
            v4Drawerlayout.openDrawer(Gravity.LEFT);
        } else {
            v4Drawerlayout.closeDrawer(Gravity.LEFT);
        }
    }
    class ThisAdapter extends BaseAdapter{
        Context context;
        List<String> list1;
        List<Integer> list2;

        public ThisAdapter(Context context, List<String> list1, List<Integer> list2) {
            this.context = context;
            this.list1 = list1;
            this.list2 = list2;
        }

        @Override
        public int getCount() {
            return list1.size();
        }

        @Override
        public Object getItem(int i) {
            return list1.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ThisItem item = null;
            if (view == null){
                item = new ThisItem();
                view = LayoutInflater.from(context).inflate(R.layout.item_caidan,null);
                item.imageView = view.findViewById(R.id.caidan_img);
                item.textView = view.findViewById(R.id.caidan_text);
                view.setTag(item);
            }else {
                item = (ThisItem) view.getTag();
            }
            item.imageView.setImageResource(list2.get(i));
            item.textView.setText(list1.get(i));
            return view;
        }
        class ThisItem{
            ImageView imageView;
            TextView textView;
        }
    }
    public void getV() {
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET,Action.getVsion, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    final JSONObject data = jsonObject.getJSONObject("data");
                    Log.d("-----", data.getInt("code") + "----" + Tools.getVersion(HomeActivity.this));
                    if (data.getInt("code") > Tools.getVersion(HomeActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this).setTitle("提示")
                                .setMessage("发现新版本，请更新!")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        XXPermissions.with(HomeActivity.this)
                                                .permission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,})
                                                .request(new OnPermission() {

                                                    @Override
                                                    public void hasPermission(List<String> granted, boolean isAll) {
                                                        if (isAll) {
                                                            try {
                                                                new DownFileHelper(HomeActivity.this, handler)
                                                                        .downFile("http://www.mchomes.cn/Talk.apk", data.getString("updateNote"));
                                                                Toast.makeText(HomeActivity.this, "更新包下载任务已开启，请耐心等待", Toast.LENGTH_LONG).show();
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void noPermission(List<String> denied, boolean quick) {

                                                    }
                                                });

                                        dialogInterface.dismiss();

                                    }
                                });
                        if (data.getInt("flag") == 0) {
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                        builder.create().show();

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new InstallApk(HomeActivity.this)
                            .installApk(new File(Environment.getExternalStorageDirectory(), "your_app_name.apk"));
                    Log.d(">_<", "0");
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this).setMessage("为了您的信息安全，我们的安装需要您的授权！")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(HomeActivity.this, "更新失败，请前往官网重新下载或授权安装", Toast.LENGTH_LONG).show();
                                    dialogInterface.dismiss();
                                }
                            }).setNeutralButton("去设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                    startActivityForResult(intent, 10103);
                                    Log.d(">_<", "1");
                                    dialogInterface.dismiss();
                                }
                            }).create();
                    dialog.show();
                }

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10103) {
            handler.sendEmptyMessage(0);
            Log.d(">_<", "2");
        }
    }



}
