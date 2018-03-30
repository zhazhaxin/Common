package cn.lemon.jcourse.module.java;

import android.os.Bundle;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;

/**
 * Created by linlongxin on 2016/8/17.
 */

public class TextListPresenter extends SuperPresenter<TextListFragment> {

    private int mPage = 0;

    @Override
    public void onCreate(Bundle b) {
        getData(true);
    }

    public void loadMore() {
        getData(false);
    }

    public void refreshData() {
        getData(true);
    }

    public void getData(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 0;
        } else {
            mPage++;
        }
        ServiceResponse<JavaCourse[]> serviceResponse = new ServiceResponse<JavaCourse[]>() {
            @Override
            public void onNext(JavaCourse[] javaCourses) {

                if (isRefresh) {
                    getView().getAdapter().clear();
                    getView().getRecyclerView().dismissSwipeRefresh();
                }
                if (javaCourses.length == 0 && mPage == 0) {
                    getView().showEmpty();
                }
                getView().showContent();
                getView().getAdapter().addAll(javaCourses);
                if (javaCourses.length < 10) {
                    getView().getRecyclerView().showNoMore();
                }
                if (getView().getRecyclerView().getSwipeRefreshLayout().isRefreshing()) {
                    getView().getRecyclerView().getSwipeRefreshLayout().setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().showError();
            }
        };
        JavaCourseModel.getInstance().getTextJavaCourseList(mPage, serviceResponse);
        putDisposable(serviceResponse);
    }
}
