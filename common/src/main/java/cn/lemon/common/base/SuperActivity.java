package cn.lemon.common.base;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.annotation.Annotation;

import cn.lemon.common.R;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.base.widget.MaterialDialog;

/**
 * Activity顶级父类 : 添加各种状态(数据错误，数据为空，数据加载中)页的展示，
 * 自定义的MaterialDialog的显示，进度条dialog显示
 * <p>
 * MVP模型中把Activity作为view层，可通过getPresenter()调用对应的presenter实例
 * <p>
 * Created by linlongxin on 2016/8/3.
 */

public class SuperActivity<P extends SuperPresenter> extends AppCompatActivity {

    private final String TAG = "SuperActivity";
    private boolean isUseStatusPages = true;
    private boolean isShowLoading = true;
    private boolean isShowingContent = false;
    private boolean isShowingError = false;
    private boolean useAnimWithActivity = true;

    protected TextView mEmptyPage;
    protected TextView mLoadDataButton;
    protected LinearLayout mErrorPage;
    protected LinearLayout mLoadingPage;
    protected FrameLayout mSuperRealContent; //命名为了避免其子类中有相同
    protected View mCurrentShowView;

    private MaterialDialog mDialog;
    private AlertDialog mLoadingDialog;

    private ObjectAnimator mShowAnimator;
    private ObjectAnimator mHideAnimator;

    private P mPresenter;

    /**
     * 在setContentView()之前调用，如果activity的toolbar功能会受到其他view影响则不能使用状态页。
     * 如：DrawerLayout的ToolBar
     * 默认：显示状态页
     */

    public void useStatusPages(boolean show) {
        isUseStatusPages = show;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter();
    }

    public <V extends View> V $(@IdRes int id) {
        return (V) super.findViewById(id);
    }

    //在onStart之后回调
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onCreate();
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
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                        Log.i(TAG, "SuperActivity : " + e.getMessage());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        Log.i(TAG, "SuperActivity : " + e.getMessage());
                    }
                }
            }
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (isUseStatusPages) {
            addStatusPage(layoutResID);
        } else
            super.setContentView(layoutResID);
    }

    /**
     * 添加各种状态页
     *
     * @param contentID
     */
    private void addStatusPage(@LayoutRes int contentID) {
        FrameLayout mDecorContent = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
        getLayoutInflater().inflate(R.layout.base_status_page, mDecorContent, true);
        mSuperRealContent = (FrameLayout) mDecorContent.findViewById(R.id.super_real_content); //Activity的content
        getLayoutInflater().inflate(contentID, mSuperRealContent, true); //把activity要显示的xml加载到mContent布局里

        mEmptyPage = (TextView) mDecorContent.findViewById(R.id.empty_page); //事实说明view状态时GONE也可以findViewById()
        mLoadDataButton = (TextView) mDecorContent.findViewById(R.id.error_to_load_button);
        mErrorPage = (LinearLayout) mDecorContent.findViewById(R.id.error_page);
        mLoadingPage = (LinearLayout) mDecorContent.findViewById(R.id.loading_page);
        mCurrentShowView = mLoadingPage;

        mLoadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickErrorLoadData(v);
            }
        });
    }

    public boolean isUseStatusPages() {
        return isUseStatusPages;
    }

    public void onClickErrorLoadData(View v) {
        showLoading();
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
            isShowingError = false;
            isShowLoading = false;
        }
    }

    public void showLoading() {
        if (!isShowLoading) {
            showView(mLoadingPage);
            isShowingContent = false;
            isShowingError = false;
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
     *
     * @param view
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
     *
     * @param view
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
     * 显示进度条的dialog
     */
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

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /**
     * 展示一个对话框 : title,content,确定按钮，取消按钮
     */
    public void showDialog(String title,
                           DialogInterface.OnClickListener positiveListener) {
        showDialog(title, null, null, false, positiveListener, null);
    }

    public void showDialog(String title, boolean cancleAble,
                           DialogInterface.OnClickListener positiveListener) {
        showDialog(title, null, null, cancleAble, positiveListener, null);
    }

    public void showDialog(String title,
                           DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener passiveListener) {
        showDialog(title, null, null, false, positiveListener, passiveListener);
    }

    public void showDialog(String title, boolean cancleAble,
                           DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener passiveListener) {
        showDialog(title, null, null, cancleAble, positiveListener, passiveListener);
    }

    public void showDialog(String title, String positiveText, String passiveText, boolean cancelAble,
                           DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener passiveListener) {
        if (mDialog == null) {
            mDialog = new MaterialDialog.Builder(this)
                    .setTitle(title)
                    .setPositiveText(positiveText)
                    .setPassiveText(passiveText)
                    .setOnPositiveClickListener(positiveListener)
                    .setOnPassiveClickListener(passiveListener)
                    .setCancelable(cancelAble)
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

//    @Override
//    public void startActivity(Intent intent) {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
//        } else {
//            super.startActivity(intent);
//            if (useAnimWithActivity) {
//                this.overridePendingTransition(0, R.anim.start_activity);
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
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

    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
