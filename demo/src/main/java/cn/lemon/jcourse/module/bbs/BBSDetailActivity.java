package cn.lemon.jcourse.module.bbs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.CircleTransform;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.MultiTypeAdapter;

@RequirePresenter(BBSDetailPresenter.class)
public class BBSDetailActivity extends ToolbarActivity<BBSDetailPresenter> implements View.OnClickListener {

    private RefreshRecyclerView mRecyclerView;
    private MultiTypeAdapter mAdapter;
    public EditText mCommentContent;
    public String mObjectName = "";
    public int mObjectId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bbs_activity_detail);

        ImageView mAvatar = $(R.id.avatar);
        mCommentContent = $(R.id.comment_content);
        ImageView mSend = $(R.id.send);
        mRecyclerView = $(R.id.recycler_view);

        mAdapter = new MultiTypeAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData();
            }
        });

        if (AccountModel.getInstance().isLogin()) {
            Glide.with(this)
                    .load(AccountModel.getInstance().getAccount().avatar)
                    .transform(new CircleTransform(this))
                    .into(mAvatar);
        }
        mSend.setOnClickListener(this);
    }

    public void setData(BBS bbs) {
        showContent();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> pics = gson.fromJson(bbs.pictures, listType);
        mAdapter.clear();
        mAdapter.add(ContentViewHolder.class, bbs);
        mAdapter.addAll(PictureViewHolder.class, pics);
        if (bbs.comments.length > 0) {
            CommentViewHolder.mContext = this;
            mAdapter.addAll(CommentViewHolder.class, bbs.comments);
        }
        mRecyclerView.dismissSwipeRefresh();
        mRecyclerView.showNoMore();
    }

    @Override
    public void onClick(View v) {
        getPresenter().comment(mObjectId, mCommentContent.getText().toString());
    }

    public void clearText() {
        mCommentContent.setText("");
        Utils.hideSoftInput(this);
    }

    public void addComment(BBS.Comment comment) {
        mAdapter.openLoadMore();
        mAdapter.add(CommentViewHolder.class, comment);
        mAdapter.showNoMore();
        CommentViewHolder.mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureViewHolder.clearPictures();
        CommentViewHolder.mContext = null;
    }
}
