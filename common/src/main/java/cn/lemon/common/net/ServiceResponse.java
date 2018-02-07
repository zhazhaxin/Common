package cn.lemon.common.net;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by linlongxin on 2016/8/8.
 */

public class ServiceResponse<T> extends DisposableObserver<T> {

    private final String TAG = "ServiceResponse";

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

}
