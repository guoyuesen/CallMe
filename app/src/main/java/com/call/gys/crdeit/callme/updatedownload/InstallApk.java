package com.call.gys.crdeit.callme.updatedownload;

/**
 * Created by 郭月森 on 2018/8/23.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.call.gys.crdeit.callme.BuildConfig;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */

public class InstallApk {


    Activity context;

    public InstallApk(Activity context) {
        this.context = context;
    }

    public void installApk(final File apkFile) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if (b) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(),
                        BuildConfig.APPLICATION_ID+".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                XXPermissions.with(context)
                        .permission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(),
                                        BuildConfig.APPLICATION_ID+".fileProvider", apkFile);
                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                                context.startActivity(intent);
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {

                            }
                        });
               /* //请求安装未知应用来源的权限
                ConfirmDialog confirmDialog =new ConfirmDialog(context);
                confirmDialog.setStyle("安装权限","Android8.0安装应用需要打开未\n知来源权限，请去设置中开启权限",
                        "去设置","取消");
                confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        String[] mPermissionList = new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES};
                        ActivityCompat.requestPermissions(context, mPermissionList, 2);
                    }
                });
                confirmDialog.show();*/
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(),
                        BuildConfig.APPLICATION_ID+".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

    }
}
