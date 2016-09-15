package cn.lemon.jcourse.module.bbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

/**
 * Created by linlongxin on 2016/9/14.
 */
@RequirePresenter(BBSPresenter.class)
public class BBSFragment extends SuperFragment<BBSPresenter> {

    private RefreshRecyclerView mRecyclerView;
    private BBSAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new BBSAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bbs_fragment, container,false);
        mRecyclerView = (RefreshRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        return view;
    }

    public void setData(BBS[] data,boolean isRefresh){
        if(data.length == 0 && isRefresh){
            showEmpty();
        }else if(isRefresh){
            mAdapter.clear();
        }
        mAdapter.addAll(data);
        if(data.length < 10){
            mAdapter.showNoMore();
        }
    }
}
