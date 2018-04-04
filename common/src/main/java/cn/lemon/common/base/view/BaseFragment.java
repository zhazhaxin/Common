package cn.lemon.common.base.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.lemon.common.R;

/**
 * Fragment顶级父类 : 添加各种状态(数据错误，数据为空，数据加载中)页的展示，
 * 自定义的MaterialDialog的显示，进度条dialog显示
 * @author linlongxin 2018/3/27.
 */
public abstract class BaseFragment extends Fragment implements ISuperFunction {

    private final String TAG = BaseFragment.class.getSimpleName();

    private static final int ANIMATION_DURATION = 300;
    private int mLayoutResId;

    protected boolean isUseStatusPages = false;

    private boolean isShowLoading = true;
    private boolean isShowingContent = false;
    private boolean isShowingError = false;
    private boolean isShowingEmpty = false;

    private ViewPropertyAnimator mShowAnimator;
    private ViewPropertyAnimator mHideAnimator;

    // status page
    protected TextView mEmptyPage;
    protected LinearLayout mErrorPage;
    protected LinearLayout mLoadingPage;

    protected View mRealContent;
    // 当前显示的 page
    protected View mCurrentPage;
    protected TextView mLoadDataButton;

    private View mRoot;

    public BaseFragment() {
    }

    @SuppressLint("ValidFragment")
    public BaseFragment(View fragment) {
        mRoot = fragment;
    }

    @SuppressLint("ValidFragment")
    public BaseFragment(@LayoutRes int layoutResID) {
        this(layoutResID, false);
    }

    @SuppressLint("ValidFragment")
    public BaseFragment(@LayoutRes int layoutResID, boolean isUseStatusPages) {
        this.mLayoutResId = layoutResID;
        this.isUseStatusPages = isUseStatusPages;
    }

    @Nullable
    @Override //container ---> activity
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (isUseStatusPages) {
            onInitPages(inflater, mLayoutResId);
            return mRoot;
        } else {
            if (mLayoutResId != 0) {
                mRoot = inflater.inflate(mLayoutResId, container, false);
            }
            return mRoot;
        }
    }

    public <V extends View> V findViewById(@IdRes int resId) {
        return (V) mRoot.findViewById(resId);
    }

    /**
     * 添加各种状态页
     */
    private void addStatusPage(LayoutInflater inflater, @Nullable ViewGroup container) {

    }

    @Override
    public void onInitPages(int contentId) {

    }

    @Override
    public void onInitPages(LayoutInflater inflater, int contentId) {
        mRoot = inflater.inflate(R.layout.base_status_page, null);
        mRoot.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRealContent = inflater.inflate(contentId, null);
        if (mRoot instanceof FrameLayout) {
            ((FrameLayout) mRoot).addView(mRealContent);
        }

        mEmptyPage = findViewById(R.id.empty_page);
        mLoadDataButton = findViewById(R.id.error_to_load_button);
        mErrorPage = findViewById(R.id.error_page);
        mLoadingPage = findViewById(R.id.loading_page);
        mRealContent.setVisibility(View.GONE);
        mCurrentPage = mLoadingPage;
        mLoadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorRetry(v);
            }
        });
    }

    @Override
    public void showEmpty() {
        Log.d(TAG, "showEmpty");
        if (!isShowingEmpty) {
            showView(mEmptyPage);
            isShowingError = false;
            isShowingContent = false;
            isShowLoading = false;
            isShowingEmpty = true;
        }
    }

    @Override
    public void showError() {
        Log.d(TAG, "showError");
        if (!isShowingError) {
            showView(mErrorPage);
            isShowingError = true;
            isShowingContent = false;
            isShowLoading = false;
            isShowingEmpty = false;
        }
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading");
        if (!isShowLoading) {
            showView(mLoadingPage);
            isShowingError = false;
            isShowingContent = false;
            isShowLoading = true;
            isShowingEmpty = false;
        }
    }

    @Override
    public void showContent() {
        Log.d(TAG, "showContent");
        if (!isShowingContent) {
            showView(mRealContent);
            isShowingContent = true;
            isShowingError = false;
            isShowLoading = false;
            isShowingEmpty = false;
        }
    }


    @Override
    public void onErrorRetry(View v) {
        showLoading();
    }

    public void showView(View view) {
        hideViewWithAnimation(mCurrentPage);
        mCurrentPage = view;
        showViewWithAnimation(view);
    }

    /**
     * 展示状态页添加动画
     *
     * @param view
     */
    private void showViewWithAnimation(final View view) {
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        mShowAnimator = view.animate().alpha(1).setDuration(ANIMATION_DURATION);
        mShowAnimator.start();
    }

    /**
     * 隐藏状态页添加动画
     *
     * @param view
     */
    private void hideViewWithAnimation(final View view) {
        if (mHideAnimator != null) {
            mHideAnimator.setListener(null);
            mHideAnimator.cancel();
        }
        view.setAlpha(1f);
        mHideAnimator = view.animate().alpha(0f).setDuration(ANIMATION_DURATION);
        mHideAnimator.start();
        mHideAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

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
    }
}
