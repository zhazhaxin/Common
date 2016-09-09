package cn.lemon.common.base.presenter;

/**
 * Created by linlongxin on 2016/8/16.
 */

public class SuperPresenter<V> {

    private V mView;

    public void attachView(V mView) {
        this.mView = mView;
    }

    protected V getView() {
        return mView;
    }

    //在Activity的onStart之后回调，在Fragment的onCreateView之后回调
    public void onCreate(){};

    //在Activity的onResume之后回调，在Fragment的onResume中回调
    public void onResume(){}

    //在view的onDestroy中调用
    public void onDestroy(){}
}
