package cn.lemon.common.net;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class SchedulersTransformer<T> implements Observable.Transformer<T, T> {

    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.subscribeOn(Schedulers.io()) //不同于observeOn()，subscribeOn()的位置放在哪里都可以，但它是只能调用一次
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()); //指定 Subscriber 回调发生在主线程。
    }
}
