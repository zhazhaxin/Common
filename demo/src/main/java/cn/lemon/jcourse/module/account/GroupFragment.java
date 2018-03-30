package cn.lemon.jcourse.module.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import cn.lemon.common.base.view.SuperFragment;
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
        EventBus.getDefault().register(this);
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
        mRecyclerView.addRefreshAction(new Action() {
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
        checkStatus();
    }

    public void checkStatus() {
        if (!AccountModel.getInstance().isLogin()) {
            showContent();
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
            mRecyclerView.dismissSwipeRefresh();
        }
        AccountModel.getInstance().group(mPage, new ServiceResponse<BBS[]>() {
            @Override
            public void onNext(BBS[] bbses) {
                super.onNext(bbses);

                if (bbses.length == 0 && isRefresh) {
                    showEmpty();
                    return;
                } else if (isRefresh) {
                    mAdapter.clear();
                }
                showContent();
                mAdapter.addAll(bbses);
                if (bbses.length < 10) {
                    mAdapter.showNoMore();
                }
                mPage++;
                if(isRefresh){
                    mRecyclerView.getRecyclerView().scrollToPosition(0);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showError();
            }
        });
    }

    @Override
    public void onErrorRetry(View v) {
        getData(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(String checkStatus){
        if(checkStatus.equals(Config.CHECK_STATUS_FOR_GROUP_FRAGMENT)){
            checkStatus();
        }
    }
}
