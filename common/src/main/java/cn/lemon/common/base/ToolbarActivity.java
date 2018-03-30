package cn.lemon.common.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.lemon.common.R;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.base.view.SuperActivity;


/**
 * Activity 顶部可添加一个标准的 toolbar，只需在 xml 中添加一个 @+id = toolbar 的 Toolbar 即可，样式需自定义
 * <p>
 * Created by linlongxin on 2016/7/31.
 */

public class ToolbarActivity<T extends SuperPresenter> extends SuperActivity<T> {

    private boolean isHomeBack = true;
    private Toolbar mToolbar;

    //设置 toolbar 是否显示返回键
    public void setToolbarHomeBack(boolean isHomeBack) {
        this.isHomeBack = isHomeBack;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (isUseStatusPages) { //添加状态页到activity
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            FrameLayout mDecorView = (FrameLayout) getWindow().getDecorView();
            FrameLayout mDecorContent = (FrameLayout) mDecorView.findViewById(android.R.id.content);
            ViewGroup mLayoutView = (ViewGroup) getLayoutInflater().inflate(layoutResID, null);
            mToolbar = (Toolbar) mLayoutView.findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(isHomeBack);
            }
            mLayoutView.removeView(mToolbar);
            if (mToolbar.getParent() != null) {
                ((ViewGroup) mToolbar.getParent()).removeView(mToolbar);
            }
            linearLayout.addView(mToolbar);

            getLayoutInflater().inflate(R.layout.base_status_page, linearLayout, true);
            mRealContent = (FrameLayout) linearLayout.findViewById(R.id.super_real_content);
            mRealContent.addView(mLayoutView);
            mDecorContent.addView(linearLayout);
            initStatusPages(linearLayout);
        } else {
            super.setContentView(layoutResID);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(isHomeBack);
            }
        }
    }

    public void initStatusPages(LinearLayout parent) {
        mEmptyPage = (TextView) parent.findViewById(R.id.empty_page);
        mLoadDataButton = (TextView) parent.findViewById(R.id.error_to_load_button);
        mErrorPage = (LinearLayout) parent.findViewById(R.id.error_page);
        mLoadingPage = (LinearLayout) parent.findViewById(R.id.loading_page);
        mCurrentPage = mLoadingPage;
        mLoadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorRetry(v);
            }
        });
    }

    @Override
    public View findViewById(int id) {
        if (isUseStatusPages) {
            return mRealContent.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && isHomeBack) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
