package cn.lemon.jcourse.module.bbs;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.alien95.util.TimeTransform;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.jcourse.module.account.UserBBSListActivity;
import cn.lemon.view.adapter.BaseViewHolder;

public class ContentViewHolder extends BaseViewHolder<BBS> implements View.OnClickListener {

    private TextView mName;
    private TextView mSign;
    private TextView mTitle;
    private TextView mContent;
    private ImageView mAvatar;
    private TextView mTime;
    private TextView mFellow;

    private BBS mBBS;

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
        mTime = findViewById(R.id.time);
        mFellow = findViewById(R.id.fellow);
    }

    @Override
    public void setData(BBS bbs) {
        super.setData(bbs);
        mBBS = bbs;
        Glide.with(itemView.getContext())
                .load(bbs.avatar)
                .transform(new GlideCircleTransform(itemView.getContext()))
                .into(mAvatar);
        mName.setText(bbs.name);
        mSign.setText(bbs.sign);
        mTitle.setText(bbs.title);
        mContent.setText(bbs.content);
        mTime.setText(TimeTransform.getRecentlyDate(bbs.time * 1000));

        mName.setOnClickListener(this);
        mSign.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mFellow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fellow) {

        } else {
            Intent intent = new Intent(itemView.getContext(), UserBBSListActivity.class);
            intent.putExtra(Config.USER_BBS_LIST, mBBS.authorId);
            itemView.getContext().startActivity(intent);
        }
    }
}