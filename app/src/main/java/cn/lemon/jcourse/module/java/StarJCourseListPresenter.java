package cn.lemon.jcourse.module.java;

import cn.lemon.common.base.Presenter;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.module.ServiceResponse;

/**
 * Created by linlongxin on 2016/8/16.
 */

public class StarJCourseListPresenter extends Presenter<StarJCourseListActivity> {

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
                } else if (javaCourses.length < 20) {
                    getView().getRecyclerView().stopMore();
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
