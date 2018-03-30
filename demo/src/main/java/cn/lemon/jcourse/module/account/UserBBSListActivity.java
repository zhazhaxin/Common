package cn.lemon.jcourse.module.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import cn.lemon.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.module.bbs.BBSAdapter;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

public class UserBBSListActivity extends ToolbarActivity {

    private RefreshRecyclerView mRecyclerView;
    private BBSAdapter mAdapter;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_activity_user_bbslist);

        mId = getIntent().getIntExtra(Config.USER_BBS_LIST, 0);
        if (mId == 0) {
            mId = AccountModel.getInstance().getAccount().id;
        } else if (!AccountModel.getInstance().isLogin() ||
                (mId != AccountModel.getInstance().getAccount().id)) {
            setTitle("他的话题");
        }

        mAdapter = new BBSAdapter(this);
        mAdapter.setCancleJumpToUserBBS();
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addRefreshAction(new Action() {
            @Override
            public void onAction() {
                getData();
            }
        });
        getData();
    }

    public void getData() {
        if (mId == 0) {
            Utils.Toast("获取失败");
            showError();
            return;
        }
        AccountModel.getInstance().getUserBBSList(mId, new ServiceResponse<BBS[]>() {
            @Override
            public void onNext(BBS[] bbses) {
                super.onNext(bbses);
                showContent();
                mRecyclerView.dismissSwipeRefresh();
                if (bbses.length == 0) {
                    showEmpty();
                } else {
                    mAdapter.clear();
                    mAdapter.addAll(bbses);
                    mAdapter.showNoMore();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRecyclerView.dismissSwipeRefresh();
                showError();
            }
        });
    }
}
