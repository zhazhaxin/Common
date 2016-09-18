package cn.lemon.jcourse.module.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.alien95.util.Utils;
import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.module.bbs.BBSAdapter;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

/**
 * Created by linlongxin on 2016/9/18.
 */

public class GroupFragment extends SuperFragment implements View.OnClickListener {

    private RefreshRecyclerView mRecyclerView;
    private TextView mLogin;
    private RelativeLayout mHolder;
    private BBSAdapter mAdapter;
    private int mPage = 0;

    public GroupFragment() {
        super(R.layout.account_fragment_group, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new BBSAdapter(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mLogin = (TextView) findViewById(R.id.login);
        mHolder = (RelativeLayout) findViewById(R.id.holder);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getData(true);
            }
        });
        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getData(false);
            }
        });
        checkLogin();
    }

    public void checkLogin() {
        showContent();
        if (!AccountModel.getInstance().isLogin()) {
            mRecyclerView.setVisibility(View.GONE);
            mHolder.setVisibility(View.VISIBLE);
            mLogin.setOnClickListener(this);
        } else {
            mHolder.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            getData(true);
        }
    }

    public void getData(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 0;
        }
        AccountModel.getInstance().group(mPage, new ServiceResponse<BBS[]>() {
            @Override
            public void onNext(BBS[] bbses) {
                super.onNext(bbses);
                if (bbses.length == 0 && isRefresh) {
                    showEmpty();
                }
                if (isRefresh) {
                    mAdapter.clear();
                    mRecyclerView.dismissSwipeRefresh();
                }
                mAdapter.addAll(bbses);
                if (bbses.length < 10) {
                    mAdapter.showNoMore();
                }
                mPage++;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, Config.REQUEST_LOGIN_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Config.RESULT_LOGIN_CODE) {
            Utils.Log("GroupFragment--onActivityResult : 检查状态");
            checkLogin();
        }
    }
}
