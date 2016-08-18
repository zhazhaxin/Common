package cn.lemon.jcourse.module.java;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.model.ServiceResponse;

/**
 * Created by linlongxin on 2016/8/16.
 */

public class StarJCourseListPresenter extends SuperPresenter<StarJCourseListActivity> {

    private int mPage = 0;

    @Override
    public void onCreate() {
        getData(true);
    }

    public void refreshData() {
        getData(true);
    }

    public void getData(final boolean isRefresh) {
        JavaCourseModel.getInstance().getStarJCourseList(mPage, new ServiceResponse<JavaCourse[]>() {
            @Override
            public void onNext(JavaCourse[] javaCourses) {
                super.onNext(javaCourses);
                if (isRefresh) {
                    getView().showContent();
                    getView().getAdapter().clear();
                }
                getView().getAdapter().addAll(javaCourses);
                if (javaCourses.length == 0 && mPage == 0) {
                    getView().showEmpty();
                } else if (javaCourses.length < 10) {
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
        });
    }
}
