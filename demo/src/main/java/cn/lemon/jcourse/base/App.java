package cn.lemon.jcourse.base;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

import cn.alien95.resthttp.request.RestHttp;
import cn.alien95.util.Utils;
import cn.lemon.common.base.model.SuperModel;
import cn.lemon.jcourse.BuildConfig;
import cn.lemon.jcourse.model.AccountModel;

/**
 * Created by linlongxin on 2016/7/31.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RestHttp.initialize(this);
        SuperModel.initialize(this);
        Utils.initialize(this);
        if(BuildConfig.DEBUG){
            Utils.setDebug(true,"JCourse");
        }
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this,"57c3fd24e0f55a756700263e","wandoujia");
        MobclickAgent. startWithConfigure(config);

        //查看是否登录过，如果登录过则设置全局的Headers
        if(AccountModel.getInstance().getAccount() != null){
            AccountModel.getInstance().setHeaders(AccountModel.getInstance().getAccount());
        }
    }
}
