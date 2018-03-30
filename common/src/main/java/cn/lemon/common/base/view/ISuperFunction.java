package cn.lemon.common.base.view;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

public interface ISuperFunction {

    void showEmpty();

    void showError();

    void showLoading();

    void showContent();

    void showLoadingDialog();

    void dismissLoadingDialog();

    void onInitPages(@LayoutRes int contentId);
    void onInitPages(LayoutInflater inflater, @LayoutRes int contentId);

    void onInitViews();

    void onErrorRetry(View v);
}
