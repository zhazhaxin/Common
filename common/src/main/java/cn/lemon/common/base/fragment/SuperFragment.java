package cn.lemon.common.base.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Annotation;

import cn.lemon.common.R;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.base.widget.MaterialDialog;

/**
 * Fragment顶级父类 : 添加各种状态(数据错误，数据为空，数据加载中)页的展示，
 * 自定义的MaterialDialog的显示，进度条dialog显示
 * <p>
 * MVP模型中把Fragment作为view层，可通过getPresenter()调用对应的presenter实例
 * <p>
 * Created by linlongxin on 2016/8/6.
 */

public class SuperFragment<T extends SuperPresenter> extends Fragment {

    private final String TAG = "SuperFragment";
    private boolean isUseStatusPages = false;
    private boolean isShowLoading = true;
    private boolean isShowingContent = false;
    private boolean isShowingError = false;
    private int mLayoutResId;
    private View mView;

    private TextView mEmptyPage;
    private TextView mLoadDataButton;
    private LinearLayout mErrorPage;
    private LinearLayout mLoadingPage;
    private FrameLayout mSuperRealContent;  //命名为了避免其子类中有相同
    private View mCurrentShowView;
    private MaterialDialog mDialog;

    private ObjectAnimator mShowAnimator;
    private ObjectAnimator mHideAnimator;

    private T mPresenter;

    public SuperFragment(){}

    @SuppressLint("ValidFragment")
    public SuperFragment(View fragment) {
        mView = fragment;
    }

    @SuppressLint("ValidFragment")
    public SuperFragment(@LayoutRes int layoutResID) {
        this(layoutResID, false);
    }

    @SuppressLint("ValidFragment")
    public SuperFragment(@LayoutRes int layoutResID, boolean isUseStatusPages) {
        this.mLayoutResId = layoutResID;
        this.isUseStatusPages = isUseStatusPages;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter();
    }

    //onCreateView之后
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null)
            mPresenter.onCreate();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.onResume();
    }

    public void attachPresenter() {
        Annotation[] annotations = getClass().getAnnotations();
        if (annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequirePresenter) {
                    RequirePresenter presenter = (RequirePresenter) annotation;
                    try {
                        mPresenter = (T) presenter.value().newInstance();
                        mPresenter.attachView(this);
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                        Log.i(TAG,"SuperFragment : " + e.getMessage());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        Log.i(TAG,"SuperFragment : " + e.getMessage());
                    }
                }
            }
        }
    }

    public T getPresenter() {
        return mPresenter;
    }

    @Nullable
    @Override //container ---> activity
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isUseStatusPages) {
            addStatusPage(inflater, container);
            return mView;
        } else {
            if (mLayoutResId != 0) {
                mView = inflater.inflate(mLayoutResId, container, false);
            }
            return mView;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        if (mHideAnimator != null) {
            mHideAnimator.cancel();
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mPresenter = null;
    }

    /**
     * 添加各种状态页
     */
    private void addStatusPage(LayoutInflater inflater, @Nullable ViewGroup container) {
        mView = inflater.inflate(R.layout.base_status_page, null);
        mView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSuperRealContent = (FrameLayout) mView.findViewById(R.id.super_real_content);
        inflater.inflate(mLayoutResId, mSuperRealContent, true);

        mEmptyPage = findViewById(R.id.empty_page);
        mLoadDataButton = findViewById(R.id.error_to_load_button);
        mErrorPage = findViewById(R.id.error_page);
        mLoadingPage = findViewById(R.id.loading_page);
        mCurrentShowView = mLoadingPage;

        mLoadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickErrorLoadData(v);
            }
        });
    }

    public void onClickErrorLoadData(View v) {
        showLoading();
    }

    public <V extends View> V findViewById(@IdRes int resId) {
        return (V) mView.findViewById(resId);
    }

    public void showEmpty() {
        showView(mEmptyPage);
        isShowingError = false;
        isShowingContent = false;
        isShowLoading = false;
    }

    public void showError() {
        if (!isShowingError) {
            showView(mErrorPage);
            isShowingError = true;
            isShowingContent = false;
            isShowLoading = false;
        }

    }

    public void showLoading() {
        if (!isShowLoading) {
            showView(mLoadingPage);
            isShowingError = false;
            isShowingContent = false;
            isShowLoading = true;
        }
    }

    public void showContent() {
        if (!isShowingContent) {
            showView(mSuperRealContent);
            isShowingContent = true;
            isShowingError = false;
            isShowLoading = false;
        }
    }

    public void showView(View view) {
        hideViewWithAnimation(mCurrentShowView);
        mCurrentShowView = view;
        view.setVisibility(View.VISIBLE);
        showViewWithAnimation(view);
    }

    /**
     * 展示状态页添加动画
     */
    public void showViewWithAnimation(View view) {
        if (mShowAnimator != null) {
            mShowAnimator.end();
            mShowAnimator.cancel();
            mShowAnimator = null;
        }
        mShowAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        mShowAnimator.setDuration(400);
        mShowAnimator.start();
    }

    /**
     * 隐藏状态页添加动画
     */
    public void hideViewWithAnimation(View view) {
        if (mHideAnimator != null) {
            mHideAnimator.end();
            mHideAnimator.cancel();
            mHideAnimator = null;
        }
        mHideAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        mHideAnimator.setDuration(400);
        mHideAnimator.start();
        view.setVisibility(View.GONE);
    }

    /**
     * 展示一个对话框 : title,content,确定按钮，取消按钮
     */
    public void showDialog(String title, String content, DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener passiveListener) {
        showDialog(title, content, null, null, positiveListener, passiveListener);
    }

    public void showDialog(String content, String positiveText, String passiveText, DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener passiveListener) {
        showDialog(null, content, positiveText, passiveText, positiveListener, passiveListener);
    }

    public void showDialog(String title, String content, String positiveText, String passiveText, DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener passiveListener) {
        if (mDialog == null) {
            mDialog = new MaterialDialog.Builder(getActivity())
                    .setTitle(title)
                    .setPositiveText(positiveText)
                    .setPassiveText(passiveText)
                    .setOnPositiveClickListener(positiveListener)
                    .setOnPassiveClickListener(passiveListener)
                    .create();
        }
        mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
