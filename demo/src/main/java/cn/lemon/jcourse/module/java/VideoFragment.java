package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.JVideo;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by linlongxin on 2016/8/6.
 */

@RequirePresenter(VideoPresenter.class)
public class VideoFragment extends SuperFragment<VideoPresenter> {

    private RefreshRecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    public VideoFragment() {
        super(R.layout.java_fragment_video,true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new VideoAdapter(getActivity());
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

    public void setData(JVideo[] data,boolean isRefresh){
        if(isRefresh){
            mAdapter.clear();
            mRecyclerView.dismissSwipeRefresh();
        }
        mAdapter.addAll(data);
        if(data.length == 0 && isRefresh){
            showEmpty();
        }
        if(data.length < 10){
            mAdapter.showNoMore();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            JCVideoPlayer.releaseAllVideos();
        }
    }
}
