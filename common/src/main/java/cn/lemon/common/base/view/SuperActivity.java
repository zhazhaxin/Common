package cn.lemon.common.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import java.lang.annotation.Annotation;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.common.base.presenter.SuperPresenter;


/**
 * <p>
 * MVP模型中把Activity作为view层，可通过getPresenter()调用对应的presenter实例
 * <p>
 * Created by linlongxin on 2016/8/3.
 */

public class SuperActivity<P extends SuperPresenter> extends BaseActivity {

    private final String TAG = "SuperActivity";
    private P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter();
    }

    /**
     * 在 setContentView 之后回调
     */
    @Override
    public void onInitViews() {}

    public <V extends View> V $(@IdRes int id) {
        return (V) findViewById(id);
    }

    //在onStart之后回调
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onCreate(savedInstanceState);
        }
    }

    //在onResume之后回调
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    public void attachPresenter() {
        Annotation[] annotations = getClass().getAnnotations();
        if (annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequirePresenter) {
                    RequirePresenter presenter = (RequirePresenter) annotation;
                    try {
                        mPresenter = (P) presenter.value().newInstance();
                        mPresenter.attachView(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG, "SuperActivity attachPresenter : " + e.getMessage());
                    }
                }
            }
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
