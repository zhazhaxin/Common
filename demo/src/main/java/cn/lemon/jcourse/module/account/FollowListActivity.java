package cn.lemon.jcourse.module.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

@RequirePresenter(FollowListPresenter.class)
public class FollowListActivity extends ToolbarActivity<FollowListPresenter> {

    private RefreshRecyclerView mRecyclerView;
    private FollowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(true);
        setContentView(R.layout.account_activity_followers);

        mAdapter = new FollowAdapter(this);
        mRecyclerView = $(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData();
            }
        });
    }

    public void setData(Account[] accounts) {
        mAdapter.clear();
        mRecyclerView.dismissSwipeRefresh();
        if (accounts.length == 0) {
            showEmpty();
        } else {
            mAdapter.addAll(accounts);
            mRecyclerView.showNoMore();
        }
    }
}
