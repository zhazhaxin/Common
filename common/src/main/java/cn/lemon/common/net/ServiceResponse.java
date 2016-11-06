package cn.lemon.common.net;


import rx.Subscriber;

/**
 * Created by linlongxin on 2016/8/8.
 */

public class ServiceResponse<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(T t) {

    }
}
