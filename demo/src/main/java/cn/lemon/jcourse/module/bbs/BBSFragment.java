package cn.lemon.jcourse.module.bbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import cn.alien95.util.Utils;
import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.module.account.LoginActivity;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

/**
 * Created by linlongxin on 2016/9/14.
 */
@RequirePresenter(BBSPresenter.class)
public class BBSFragment extends SuperFragment<BBSPresenter> implements View.OnClickListener {

    private FloatingActionButton mAddBBS;
    private RefreshRecyclerView mRecyclerView;
    private BBSAdapter mAdapter;

    public BBSFragment() {
        super(R.layout.bbs_fragment, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddBBS = findViewById(R.id.add_bbs);
        mAddBBS.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BBSAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData(true);
            }
        });
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData(false);
            }
        });
    }

    public void setData(BBS[] data, boolean isRefresh) {
        showContent();
        if (data.length == 0 && isRefresh) {
            showEmpty();
        } else if (isRefresh) {
            mAdapter.clear();
            mRecyclerView.dismissSwipeRefresh();
        }
        mAdapter.addAll(data);
        if (data.length < 10) {
            mAdapter.showNoMore();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_bbs) {
            if(AccountModel.getInstance().isLogin()){
                Intent intent = new Intent(getActivity(), PublishBBSActivity.class);
                startActivityForResult(intent,Config.REQUEST_PUBLISH_BBS);
            }else {
                Utils.Toast("请先登录");
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Config.REQUEST_PUBLISH_BBS && resultCode == Config.RESULT_PUBLISH_BBS){
            mRecyclerView.showSwipeRefresh();
            getPresenter().getData(true);
        }
    }
}
