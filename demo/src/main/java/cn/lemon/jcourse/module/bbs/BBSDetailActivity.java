package cn.lemon.jcourse.module.bbs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.BaseViewHolder;
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
    }

    public void setData(BBS bbs) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> pics = gson.fromJson(bbs.pictures, listType);
        mAdapter.add(ContentViewHolder.class, bbs);
        mAdapter.addAll(PictureViewHolder.class, pics);
        mAdapter.addAll(CommentViewHolder.class, bbs.comments);
    }


    class ContentViewHolder extends BaseViewHolder<BBS> {

        private TextView mName;
        private TextView mSign;
        private TextView mTitle;
        private TextView mContent;
        private ImageView mAvatar;


        public ContentViewHolder(ViewGroup parent) {
            super(parent, R.layout.bbs_holder_detail_content);
        }

        @Override
        public void onInitializeView() {
            mName = findViewById(R.id.name);
            mSign = findViewById(R.id.sign);
            mTitle = findViewById(R.id.title);
            mContent = findViewById(R.id.content);
            mAvatar = findViewById(R.id.avatar);
        }

        @Override
        public void setData(BBS bbs) {
            Glide.with(BBSDetailActivity.this)
                    .load(bbs.avatar)
                    .transform(new GlideCircleTransform(BBSDetailActivity.this))
                    .into(mAvatar);
            mName.setText(bbs.name);
            mSign.setText(bbs.sign);
            mTitle.setText(bbs.title);
            mContent.setText(bbs.content);
        }
    }

    class PictureViewHolder extends BaseViewHolder<String> {

        private ImageView mPicture;

        public PictureViewHolder(ViewGroup parent) {
            super(parent, R.layout.bbs_holder_detail_picture);
        }

        @Override
        public void onInitializeView() {
            mPicture = findViewById(R.id.picture);
        }

        @Override
        public void setData(String object) {
            Glide.with(BBSDetailActivity.this)
                    .load(object)
                    .into(mPicture);
        }
    }

    class CommentViewHolder extends BaseViewHolder<BBS.Comment> {

        private TextView mName;
        private TextView mSign;
        private TextView mContent;
        private ImageView mAvatar;

        public CommentViewHolder(ViewGroup parent) {
            super(parent, R.layout.bbs_holder_detail_comment);
        }

        @Override
        public void onInitializeView() {
            mName = findViewById(R.id.name);
            mSign = findViewById(R.id.sign);
            mContent = findViewById(R.id.content);
            mAvatar = findViewById(R.id.avatar);
        }

        @Override
        public void setData(BBS.Comment comment) {
            Glide.with(BBSDetailActivity.this)
                    .load(comment.avatar)
                    .transform(new GlideCircleTransform(BBSDetailActivity.this))
                    .into(mAvatar);
            mName.setText(comment.name);
            mSign.setText(comment.sign);
            mContent.setText(comment.content);
        }
    }
}
