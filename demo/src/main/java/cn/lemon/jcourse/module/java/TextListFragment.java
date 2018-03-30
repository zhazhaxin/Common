package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.common.base.view.SuperFragment;
import cn.lemon.jcourse.R;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

/**
 * Created by linlongxin on 2016/8/6.
 */
@RequirePresenter(TextListPresenter.class)
public class TextListFragment extends SuperFragment<TextListPresenter> {

    private RefreshRecyclerView mRecyclerView;
    private TextAdapter mAdapter;

    public TextListFragment() {
        super(R.layout.java_fragment_text, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TextAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().refreshData();
            }
        });
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().loadMore();
            }
        });
    }

    @Override
    public void onErrorRetry(View v) {
        getPresenter().getData(true);
    }

    public TextAdapter getAdapter() {
        return mAdapter;
    }

    public RefreshRecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
