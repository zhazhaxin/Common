package cn.lemon.common.net;


import android.util.Log;

import rx.Subscriber;

/**
 * Created by linlongxin on 2016/8/8.
 */

public class ServiceResponse<T> extends Subscriber<T> {

    private final String TAG = "ServiceResponse";

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG,"onError : " + e.getMessage());
    }

    @Override
    public void onNext(T t) {

    }
}
