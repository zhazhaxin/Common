package cn.lemon.jcourse.module;


import cn.alien95.util.Utils;
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
        Utils.Toast(e.getMessage());
    }

    @Override
    public void onNext(T t) {

    }
}
