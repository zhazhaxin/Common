package cn.lemon.jcourse.module.bbs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.MultiTypeAdapter;

@RequirePresenter(BBSDetailPresenter.class)
public class BBSDetailActivity extends ToolbarActivity<BBSDetailPresenter> {

    private RefreshRecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_activity_detail);

        mAdapter = new MultiTypeAdapter(this);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData();
            }
        });
    }

    public void setData(BBS bbs) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> pics = gson.fromJson(bbs.pictures, listType);
        mAdapter.clear();
        mAdapter.add(ContentViewHolder.class, bbs);
        mAdapter.addAll(PictureViewHolder.class, pics);
        mAdapter.addAll(CommentViewHolder.class, bbs.comments);
        mRecyclerView.dismissSwipeRefresh();
        mRecyclerView.showNoMore();
    }
}
