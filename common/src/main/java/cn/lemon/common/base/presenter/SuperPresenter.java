package cn.lemon.common.base.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * MVP模型中的presenter层，通过getView()方法直接调用对应的activity（View层）
 *
 * 继承SuperPresenter 必须保留一个无参的构造方法
 *
 * Created by linlongxin on 2016/8/16.
 */

public class SuperPresenter<V> {

    private CompositeDisposable mCompositeDisposable;

    private V mView;

    public void attachView(V mView) {
        this.mView = mView;
    }

    protected V getView() {
        return mView;
    }

    //在Activity的onStart之后回调，在Fragment的onCreateView之后回调
    public void onCreate(@Nullable Bundle savedInstanceState){}

    //在Activity的onResume之后回调，在Fragment的onResume中回调
    public void onResume(){}

    //在view的onDestroy中调用
    public void onDestroy(){
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    protected void putDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.remove(disposable);
        }
    }
}
