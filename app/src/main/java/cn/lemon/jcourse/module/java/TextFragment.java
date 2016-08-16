package cn.lemon.jcourse.module.java;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.alien95.view.RefreshRecyclerView;
import cn.alien95.view.adapter.BaseViewHolder;
import cn.alien95.view.adapter.RecyclerAdapter;
import cn.alien95.view.callback.Action;
import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JavaCourse;
import rx.Subscriber;

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
        JavaCourseModel.getInstance().getTextJavaCourseList(mPage, 20, new Subscriber<JavaCourse[]>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showError();
            }

            @Override
            public void onNext(JavaCourse[] javaCourses) {
                showContent();
                if (isRefresh) {
                    mAdapter.clear();
                    mRecyclerView.dismissRefresh();
                }
                mAdapter.addAll(javaCourses);
                if (javaCourses.length < 20) {
                    mRecyclerView.stopMore();
                }
            }
        });
    }



}
