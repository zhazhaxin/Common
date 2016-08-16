package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import cn.alien95.view.RefreshRecyclerView;
import cn.alien95.view.callback.Action;
import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.module.ServiceResponse;

/**
 * Created by linlongxin on 2016/8/6.
 */

public class TextFragment extends SuperFragment {

    private RefreshRecyclerView mRecyclerView;
    private JavaTextAdapter mAdapter;
    private int mPage = 0;

    public TextFragment() {
        super(R.layout.java_fragment_text_course_list, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new JavaTextAdapter(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh(new Action() {
            @Override
            public void onAction() {
                getList(true);
            }
        });
        mRecyclerView.loadMore(new Action() {
            @Override
            public void onAction() {
                getList(false);
            }
        });
        getList(true);
    }


    public void getList(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 0;
        } else {
            mPage++;
        }
        JavaCourseModel.getInstance().getTextJavaCourseList(mPage, 20, new ServiceResponse<JavaCourse[]>() {
            @Override
            public void onNext(JavaCourse[] javaCourses) {

                if (isRefresh) {
                    mAdapter.clear();
                    mRecyclerView.dismissRefresh();
                }
                if (javaCourses.length == 0 && mPage == 0) {
                    showEmpty();
                }
                showContent();
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
            }
        });
    }


}
