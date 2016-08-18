package cn.lemon.jcourse.module.java;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.model.ServiceResponse;

/**
 * Created by linlongxin on 2016/8/17.
 */

public class JavaCourseUnitListPresenter extends SuperPresenter<JavaCourseUnitListActivity> {

    private int mUnit;
    private int mPage = 0;
    private String[] mTitles = {"第一章", "第二章", "第三章", "第四章", "第五章", "第六章", "第七章", "第八章", "第九章", "第十章"};

    @Override
    public void onCreate() {
        super.onCreate();
        mUnit = getView().getIntent().getIntExtra(Config.JAVA_COURSE_UNIT, 1);
        getView().setTitle(mTitles[mUnit - 1]);
        getData(true);
    }

    public void getData(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 0;
        } else {
            mPage++;
        }

        JavaCourseModel.getInstance().getJavaCourseFromDir(mUnit, mPage, new ServiceResponse<JavaCourse[]>() {
            @Override
            public void onNext(JavaCourse[] javaCourses) {
                super.onNext(javaCourses);
                getView().showContent();
                if (isRefresh) {
                    getView().getAdapter().clear();
                }
                if (javaCourses.length == 0 && mPage == 0) {
                    getView().showEmpty();
                } else {
                    getView().getAdapter().addAll(javaCourses);
                    if (javaCourses.length < 10) {
                        getView().getRecyclerView().showNoMore();
                    }
                }
                if (getView().getRecyclerView().getSwipeRefreshLayout().isRefreshing()) {
                    getView().getRecyclerView().getSwipeRefreshLayout().setRefreshing(false);
                }
            }
        });
    }
}
