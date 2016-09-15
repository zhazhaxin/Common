package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

@RequirePresenter(StarListPresenter.class)
public class StarListActivity extends ToolbarActivity<StarListPresenter> {

    private RefreshRecyclerView mRecyclerView;
    private TextAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(true);
        setContentView(R.layout.java_activity_star_list);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TextAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().refreshData();
            }
        });
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().loadMoreData();
            }
        });
    }

    public TextAdapter getAdapter() {
        return mAdapter;
    }

    public RefreshRecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
