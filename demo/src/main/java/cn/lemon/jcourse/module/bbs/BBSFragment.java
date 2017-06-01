package cn.lemon.jcourse.module.bbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;

import cn.lemon.util.Utils;
import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.BBSModel;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.bean.Banner;
import cn.lemon.jcourse.module.account.LoginActivity;
import cn.lemon.jcourse.module.main.WebActivity;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

/**
 * Created by linlongxin on 2016/9/14.
 */
public class BBSFragment extends SuperFragment implements View.OnClickListener {

    private FloatingActionButton mAddBBS;
    private RefreshRecyclerView mRecyclerView;
    private BBSAdapter mAdapter;
    private RollPagerView mBanner;
    private BannerAdapter mBannerAdapter;
    private Banner[] mBannerData;
    private int mPage = 0;

    public BBSFragment() {
        super(R.layout.bbs_fragment, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new BBSAdapter(getActivity());
        mBannerAdapter = new BannerAdapter(getContext());
        mBanner = new RollPagerView(getContext());
        mBanner.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(190)));
        mBanner.setPlayDelay(3000);
        mAdapter.setHeader(mBanner);
        mBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mBannerData != null) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(Config.WEB_VIEW_BANNER, mBannerData[position]);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mAddBBS = (FloatingActionButton) findViewById(R.id.add_bbs);
        mAddBBS.setOnClickListener(this);
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
        getData(true);
        getBannerList();
    }

    public void getData(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 0;
            mAdapter.clear();
            mRecyclerView.dismissSwipeRefresh();
        } else {
            mPage++;
        }
        BBSModel.getInstance().getBBSList(mPage, new ServiceResponse<BBS[]>() {
            @Override
            public void onNext(BBS[] data) {
                showContent();
                if (data.length > 0) {
                    mAdapter.addAll(data);
                    if (data.length < 10) {
                        mAdapter.showNoMore();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showError();
            }
        });
    }

    public void getBannerList() {
        BBSModel.getInstance().getBannerList(new ServiceResponse<Banner[]>() {
            @Override
            public void onNext(Banner[] banners) {
                super.onNext(banners);
                mBannerData = banners;
                mBannerAdapter.addAll(banners);
                mBanner.setAdapter(mBannerAdapter);
            }
        });
    }

    @Override
    public void onClickErrorLoadData(View v) {
        super.onClickErrorLoadData(v);
        getBannerList();
        getData(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_bbs) {
            if (AccountModel.getInstance().isLogin()) {
                Intent intent = new Intent(getActivity(), PublishBBSActivity.class);
                startActivityForResult(intent, Config.REQUEST_PUBLISH_BBS);
            } else {
                Utils.Toast("请先登录");
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_PUBLISH_BBS && resultCode == Config.RESULT_PUBLISH_BBS) {
            mRecyclerView.showSwipeRefresh();
            getData(true);
        }
    }
}
