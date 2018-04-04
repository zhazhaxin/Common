package cn.lemon.common.base.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.lemon.common.R;

/**
 *  Activity 顶级父类 : 添加各种状态 ( 数据错误，数据为空，数据加载中 ) 页的展示，
 * 自定义的 MaterialDialog 的显示，进度条 dialog 显示
 * @author linlongxin 2018/3/27.
 */
public abstract class BaseActivity extends AppCompatActivity implements ISuperFunction {

    protected final String TAG = BaseActivity.class.getSimpleName();
    private static final int ANIMATION_DURATION = 300;
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

    private AlertDialog mLoadingDialog;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (isUseStatusPages) {
            onInitPages(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
        onInitViews();
    }

    protected void setTitleBarContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void onInitPages(@LayoutRes int contentId) {
        FrameLayout decorContent = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
        View root = getLayoutInflater().inflate(R.layout.base_status_page, decorContent, false);
        // 把 activity 要显示的 xml 加载到 mContent 布局里
        mRealContent = getLayoutInflater().inflate(contentId, null);
        if (root instanceof FrameLayout) {
            ((FrameLayout) root).addView(mRealContent);
        }
        decorContent.addView(root);

        mEmptyPage = (TextView) decorContent.findViewById(R.id.empty_page);
        mLoadDataButton = (TextView) decorContent.findViewById(R.id.error_to_load_button);
        mErrorPage = (LinearLayout) decorContent.findViewById(R.id.error_page);
        mLoadingPage = (LinearLayout) decorContent.findViewById(R.id.loading_page);
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
    public void onInitPages(LayoutInflater inflater, int contentId) {

    }

    @Override
    public View findViewById(int id) {
        if (isUseStatusPages) {
            return mRealContent.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }

    /**
     * 是否开启状态页
     * @param show
     */
    public void setStatusPageEnable(boolean show) {
        isUseStatusPages = show;
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

    private void showView(View view) {
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
        mHideAnimator = view.animate()
                .alpha(0f)
                .setDuration(400);
        mHideAnimator.start();
        mHideAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 显示进度条的dialog
     */
    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            ProgressBar progressBar = new ProgressBar(this);
            progressBar.setPadding(dp2px(16), dp2px(16), dp2px(16), dp2px(16));
            progressBar.setBackgroundResource(android.R.color.transparent);
            mLoadingDialog = new AlertDialog.Builder(this)
                    .setView(progressBar)
                    .create();
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        if (mHideAnimator != null) {
            mHideAnimator.cancel();
        }
    }
}
