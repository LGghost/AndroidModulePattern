package com.example.lib_common.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * 检查网络的一些常用工具类
 * Created by acer on 2018/3/22.
 */

public class TheInternet {

    /**
     * 检查当前是否有网
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.i("NetWorkState", "当前网络不可用！");
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.i("NetWorkState", "有网络");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * 打开网络设置界面请求
     */
    public static AlertDialog dialog_networkSettings;
    public static void openNetworkSettings(final Context content) {
        if (dialog_networkSettings == null) {
            dialog_networkSettings = new AlertDialog.Builder(content)
                    .setTitle("开启网络服务")
                    .setMessage("本软件需要使用网络资源，是否开启网络？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (android.os.Build.VERSION.SDK_INT > 13) {
                                content.startActivity(new Intent(
                                        android.provider.Settings.ACTION_SETTINGS));
                            } else {
                                content.startActivity(new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }else{
            if (!dialog_networkSettings.isShowing()) {
                dialog_networkSettings.show();
            }
        }
    }
}
