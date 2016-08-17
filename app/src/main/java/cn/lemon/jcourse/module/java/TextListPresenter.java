package cn.lemon.jcourse.module.java;

import cn.lemon.common.base.Presenter;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.module.ServiceResponse;

/**
 * Created by linlongxin on 2016/8/17.
 */

public class TextListPresenter extends Presenter<TextListFragment> {

    private int mPage = 0;

    @Override
    public void onCreate() {
        getData(true);
    }

    public void loadMore(){
        getData(false);
    }

    public void refreshData(){
        getData(true);
    }

    public void getData(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 0;
        } else {
            mPage++;
        }
        JavaCourseModel.getInstance().getTextJavaCourseList(mPage, 20, new ServiceResponse<JavaCourse[]>() {
            @Override
            public void onNext(JavaCourse[] javaCourses) {

                if (isRefresh) {
                    getView().getAdapter().clear();
                    getView().getRecyclerView().dismissRefresh();
                }
                if (javaCourses.length == 0 && mPage == 0) {
                    getView().showEmpty();
                }
                getView().showContent();
                getView().getAdapter().addAll(javaCourses);
                if (javaCourses.length < 20) {
                    getView().getRecyclerView().stopMore();
                }
                if (getView().getRecyclerView().getSwipeRefreshLayout().isRefreshing()) {
                    getView().getRecyclerView().getSwipeRefreshLayout().setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }
}
