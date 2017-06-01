package cn.lemon.jcourse.module.bbs;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.lemon.util.TimeTransform;
import cn.lemon.util.Utils;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.CircleTransform;
import cn.lemon.jcourse.module.account.LoginActivity;
import cn.lemon.jcourse.module.account.UserBBSListActivity;
import cn.lemon.view.adapter.BaseViewHolder;

public class CommentViewHolder extends BaseViewHolder<BBS.Comment> implements View.OnClickListener {

    private final int COLOR_BLUE = 0xff5677fc;
    private TextView mName;
    private TextView mSign;
    private TextView mTime;
    private TextView mContent;
    private ImageView mAvatar;
    private TextView mCommentBtn;
    public static Context mContext;

    private BBS.Comment mComment;

    public CommentViewHolder(ViewGroup parent) {
        super(parent, R.layout.bbs_holder_detail_comment);
    }

    @Override
    public void onInitializeView() {
        mName = findViewById(R.id.name);
        mSign = findViewById(R.id.sign);
        mTime = findViewById(R.id.time);
        mContent = findViewById(R.id.content);
        mAvatar = findViewById(R.id.avatar);
        mCommentBtn = findViewById(R.id.comment);
    }

    @Override
    public void setData(BBS.Comment comment) {
        super.setData(comment);
        mComment = comment;
        Glide.with(itemView.getContext())
                .load(comment.avatar)
                .transform(new CircleTransform(itemView.getContext()))
                .into(mAvatar);
        mName.setText(comment.name);
        mSign.setText(comment.sign);
        mTime.setText(TimeTransform.getRecentlyDate(comment.time * 1000));

        if (comment.objectId > 0) {
            Spannable targetStr = new SpannableString("@" + comment.objectName + ": " + comment.content);
            targetStr.setSpan(new ForegroundColorSpan(COLOR_BLUE), 0, comment.objectName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContent.setText(targetStr);
        } else {
            mContent.setText(comment.content);
        }
        mCommentBtn.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mName.setOnClickListener(this);
        mSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                if (!AccountModel.getInstance().isLogin()) {
                    Utils.Toast("请先登录");
                    v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
                    return;
                }
                ((BBSDetailActivity) mContext).mCommentContent.setHint("回复：" + mComment.name);
                ((BBSDetailActivity) mContext).mObjectName = mComment.name;
                if (mComment.id == 0) { //连续评论自己刚添加的评论bug
                    ((BBSDetailActivity) mContext).mObjectId = AccountModel.getInstance().getAccount().id;
                } else {
                    ((BBSDetailActivity) mContext).mObjectId = mComment.id;
                }
                Utils.showSoftInput(mContext, ((BBSDetailActivity) mContext).mCommentContent);
                break;
            case R.id.avatar:
            case R.id.name:
            case R.id.sign:
                if (AccountModel.getInstance().isLogin() && mComment.id == 0) {
                    mComment.id = AccountModel.getInstance().getAccount().id;
                } else {
                    Intent intent = new Intent(itemView.getContext(), UserBBSListActivity.class);
                    intent.putExtra(Config.USER_BBS_LIST, mComment.id);
                    itemView.getContext().startActivity(intent);
                }
                break;
        }
    }
}