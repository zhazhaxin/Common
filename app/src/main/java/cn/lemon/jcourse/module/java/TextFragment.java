package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import cn.alien95.view.RefreshRecyclerView;
import cn.alien95.view.callback.Action;
import cn.lemon.common.base.RequirePresenter;
import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.jcourse.R;

/**
 * Created by linlongxin on 2016/8/6.
 */
@RequirePresenter(TextPresenter.class)
public class TextFragment extends SuperFragment<TextPresenter> {

    private RefreshRecyclerView mRecyclerView;
    private JavaTextAdapter mAdapter;

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
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.refresh(new Action() {
            @Override
            public void onAction() {
                getPresenter().refreshData();
            }
        });
        mRecyclerView.loadMore(new Action() {
            @Override
            public void onAction() {
                getPresenter().loadMore();
            }
        });
    }

    public JavaTextAdapter getAdapter() {
        return mAdapter;
    }

    public RefreshRecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
