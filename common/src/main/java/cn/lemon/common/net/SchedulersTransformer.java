package cn.lemon.common.net;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class SchedulersTransformer<T> implements ObservableTransformer<T, T> {

    @Override
    public ObservableSource<T> apply(Observable<T> observable) {
        //不同于observeOn()，subscribeOn()的位置放在哪里都可以，但它是只能调用一次，调用多次以第一次为准
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()); //指定 Subscriber 回调发生在主线程。
    }
}
