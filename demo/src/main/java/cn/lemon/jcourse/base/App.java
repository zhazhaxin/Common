package cn.lemon.jcourse.base;

import android.app.Application;

import cn.alien95.util.Utils;
import cn.lemon.common.base.model.SuperModel;
import cn.lemon.jcourse.BuildConfig;

/**
 * Created by linlongxin on 2016/7/31.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SuperModel.initialize(this);
        Utils.initialize(this);
        if(BuildConfig.DEBUG){
            Utils.setDebug(true,"JCourse");
        }
    }
}
