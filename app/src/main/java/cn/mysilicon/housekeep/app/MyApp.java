package cn.mysilicon.housekeep.app;

import android.app.Application;

import cn.mysilicon.housekeep.utils.ScreenUtils;
import cn.mysilicon.housekeep.utils.ToastUtils;


/**
 * Created by fySpring
 * Date : 2017/10/9
 * To do :
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        ToastUtils.init(this);
        ScreenUtils.init(this);
    }
}
