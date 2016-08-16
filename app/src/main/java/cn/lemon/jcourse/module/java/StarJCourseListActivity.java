package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import cn.alien95.view.RefreshRecyclerView;
import cn.alien95.view.callback.Action;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.module.ServiceResponse;

public class StarJCourseListActivity extends ToolbarActivity {

    private RefreshRecyclerView mRecyclerView;
    private JavaTextAdapter mAdapter;
    private int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(true);
        setContentView(R.layout.java_activity_star_list);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new JavaTextAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.getSwipeRefreshLayout().setRefreshing(true);
                getList(true);
            }
        });
        mRecyclerView.refresh(new Action() {
            @Override
            public void onAction() {
                getList(true);
            }
        });
    }

    public void getList(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 0;
        }
        JavaCourseModel.getInstance().getStarJCourseList(mPage, new ServiceResponse<JavaCourse[]>() {
            @Override
            public void onNext(JavaCourse[] javaCourses) {
                super.onNext(javaCourses);
                if (isRefresh) {
                    showContent();
                    mAdapter.clear();
                }
                mAdapter.addAll(javaCourses);
                if (javaCourses.length < 20) {
                    mRecyclerView.stopMore();
                }
                if (mRecyclerView.getSwipeRefreshLayout().isRefreshing()) {
                    mRecyclerView.getSwipeRefreshLayout().setRefreshing(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showError();
            }
        });
    }

}
