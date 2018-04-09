package cn.lemon.common.base.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import java.lang.annotation.Annotation;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.common.base.presenter.SuperPresenter;


/**
 * <p>
 * MVP模型中把Fragment作为view层，可通过getPresenter()调用对应的presenter实例
 * <p>
 * Created by linlongxin on 2016/8/6.
 */

public class SuperFragment<T extends SuperPresenter> extends BaseFragment {

    private final String TAG = getClass().getSimpleName();

    private T mPresenter;

    public SuperFragment() {
    }

    @SuppressLint("ValidFragment")
    public SuperFragment(int layoutResID) {
        super(layoutResID);
    }

    @SuppressLint("ValidFragment")
    public SuperFragment(int layoutResID, boolean isUseStatusPages) {
        super(layoutResID, isUseStatusPages);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        attachPresenter();
    }

    // onCreateView 之后回调
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        onInitViews();
        if (mPresenter != null) {
            mPresenter.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    private void attachPresenter() {
        if (mPresenter != null) {
            return;
        }
        Annotation[] annotations = getClass().getAnnotations();
        if (annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequirePresenter) {
                    RequirePresenter presenter = (RequirePresenter) annotation;
                    try {
                        mPresenter = (T) presenter.value().newInstance();
                        mPresenter.attachView(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG,"SuperFragment : " + e.getMessage());
                    }
                }
            }
        }
    }

    public T getPresenter() {
        attachPresenter();
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }

    @Override
    public void onInitViews() {

    }
}
