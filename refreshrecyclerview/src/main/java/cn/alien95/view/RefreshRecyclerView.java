package cn.alien95.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;

import cn.alien95.view.adapter.RecyclerAdapter;
import cn.alien95.view.callback.Action;
import cn.alien95.view.util.Util;


/**
 * Created by linlongxin on 2016/1/24.
 */
public class RefreshRecyclerView extends FrameLayout {

    private final String TAG = "RefreshRecyclerView";
    private boolean isStopLoadMore = false;   //是否允许加载更多，和StopMore()配合使用
    private boolean isAllowRefresh = true;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter mAdapter;
    private View mLoadMoreView;
    private View mNoMoreView;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Util.init(getContext());
        initView();
    }

    public void initView() {
        View view = inflate(getContext(), R.layout.view_refresh_recycler, this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.$_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.$_recycler_view);

        //默认设置停止加载view
        TextView stop = new TextView(getContext());
        stop.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(48)));
        stop.setGravity(Gravity.CENTER);
        stop.setText("没有更多了O(∩_∩)O~");
        mNoMoreView = stop;

        //默认设置加载更多view
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(48)));
        linearLayout.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setLayoutParams(new LayoutParams(Util.dip2px(24), Util.dip2px(24)));
        TextView moreView = new TextView(getContext());
        moreView.setGravity(Gravity.CENTER);
        moreView.setText("正在加载......");
        linearLayout.addView(progressBar);
        linearLayout.addView(moreView);
        mLoadMoreView = linearLayout;
    }

    /**
     * 设置LayoutManager
     *
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        this.layoutManager = layoutManager;
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(RecyclerAdapter adapter) {
        recyclerView.setAdapter(adapter);
        this.mAdapter = adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    /**
     * 设置下拉刷新颜色
     *
     * @param colors
     */
    public void setRefreshColorSchemeResources(@ColorRes int... colors) {
        swipeRefreshLayout.setColorSchemeResources(colors);
    }

    /**
     * 设置下拉刷新颜色
     *
     * @param colors
     */
    public void setRefreshColorSchemeColors(@ColorInt int... colors) {
        swipeRefreshLayout.setColorSchemeColors(colors);
    }

    /**
     * 显示SwipeRefreshLayout刷新状态
     */
    public void showRefresh() {
        swipeRefreshLayout.setRefreshing(true);
    }

    /**
     * 隐藏SwipeRefreshLayout刷新状态
     */
    public void dismissRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 刷新Adapter数据
     *
     * @param action
     */
    public void refresh(final Action action) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isAllowRefresh) {
                    return;
                }
                isStopLoadMore = false;
                mAdapter.openAdapterLoadData();
                action.onAction();
            }
        });
    }

    /**
     * 关闭下拉刷新
     */
    public void closeRefresh() {
        isAllowRefresh = false;
        if (swipeRefreshLayout.getChildAt(0).getClass().getSimpleName().equals("CircleImageView")) {
            swipeRefreshLayout.getChildAt(0).setAlpha(0);
        }
    }

    /**
     * 打开下拉刷新
     */
    public void openRefresh() {
        isAllowRefresh = true;
        if (swipeRefreshLayout.getChildAt(0).getClass().getSimpleName().equals("CircleImageView")) {
            swipeRefreshLayout.getChildAt(0).setAlpha(1);
        }
    }

    /**
     * 加载更多数据
     *
     * @param view
     * @param action
     */
    public void loadMore(View view, Action action) {
        setLoadMoreView(view);
        this.loadMore(action);
    }

    /**
     * 加载更多
     *
     * @param action
     */
    public void loadMore(final Action action) {

        if (!isStopLoadMore) {
            mAdapter.setFooter(mLoadMoreView);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;    //向下滑动

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (isStopLoadMore) {
                    stopMore();
                    return;
                }

                int lastVisibleItem;
                int totalItemCount = layoutManager.getItemCount();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (layoutManager instanceof LinearLayoutManager) {
                        //滑动到了底部Bottom
                        lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        if (lastVisibleItem >= totalItemCount - 1 && isSlidingToLast) {
                            action.onAction();
                        }
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] last = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                        //滑动到了底部Bottom
                        if (last[0] != RecyclerView.NO_POSITION) {
                            if (getMax(last) >= totalItemCount - 1 && isSlidingToLast) {
                                action.onAction();
                            }
                        }
                        Log.i(TAG, "last:" + Arrays.toString(last));
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isStopLoadMore) {
                    isSlidingToLast = false;
                    return;
                }
                if (dy > 0 || dx > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }

            }
        });
    }

    private int getMax(int[] data) {
        int max = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }


    /**
     * 停止加载更多
     */
    public void stopMore() {
        mAdapter.setFooter(mNoMoreView);
        isStopLoadMore = true;
        mAdapter.stopAdapterLoadData();
    }

    /**
     * 设置停止加载view
     *
     * @param view
     */
    public void setNoMoreView(View view) {
        mNoMoreView = view;
    }

    /**
     * 设置加载更多view
     *
     * @param view
     */
    public void setLoadMoreView(View view) {
        mLoadMoreView = view;
    }

    /**
     * 设置itemView的间隔距离：px
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setItemSpace(int left, int top, int right, int bottom) {
        recyclerView.addItemDecoration(new SpaceItemDecoration(left, top, right, bottom));
    }

    /**
     * 给recyclerView添加一个ItemDecoration
     *
     * @param itemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

}
