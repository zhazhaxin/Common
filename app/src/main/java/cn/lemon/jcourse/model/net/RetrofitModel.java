package cn.lemon.jcourse.model.net;

import java.util.concurrent.TimeUnit;

import cn.alien95.util.Utils;
import cn.lemon.common.net.HeadersInterceptor;
import cn.lemon.common.net.LogInterceptor;
import cn.lemon.jcourse.BuildConfig;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.ServiceAPI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class RetrofitModel {

    private static ServiceAPI mServiceAPI;
    private static Retrofit mRetrofit;

    public static ServiceAPI getServiceAPI(){
        if(mServiceAPI == null){
            Utils.Log("UID : " + HeadersInterceptor.UID);
            Utils.Log("token : " + HeadersInterceptor.TOKEN);
            mServiceAPI = getRetrofit().create(ServiceAPI.class);
        }
        return mServiceAPI;
    }

    private static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
                //debug模式下添加日志拦截器
                LogInterceptor logInterceptor = new LogInterceptor()
                        .setLevel(LogInterceptor.Level.BASIC)
                        .setLogTag(Config.NET_LOG_TAG).setDebugMode(true);
                mOkHttpClientBuilder
                        .addInterceptor(logInterceptor);
            }
            mOkHttpClientBuilder.addInterceptor(new HeadersInterceptor());
            mOkHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
            mOkHttpClientBuilder.readTimeout(20, TimeUnit.SECONDS);
            mOkHttpClientBuilder.writeTimeout(20, TimeUnit.SECONDS);
            mOkHttpClientBuilder.retryOnConnectionFailure(true);
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mOkHttpClientBuilder.build())
                    .build();
        }
        return mRetrofit;
    }
}
