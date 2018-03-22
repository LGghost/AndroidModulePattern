package application;

import android.content.Context;


import com.alibaba.android.arouter.launcher.ARouter;

import com.example.lib_common.base.BaseApplication;
import com.example.lib_common.utils.Utils;

/**
 * <p>这里仅需做一些初始化的工作</p>
 *
 * @author 张华洋 2017/2/15 20:14
 * @version V1.2.0
 * @name MyApplication
 */

public class MyApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
        //崩溃日志记录初始化
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // dex突破65535的限制

    }




}
