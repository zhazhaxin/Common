package cn.lemon.jcourse.module.bbs;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.view.adapter.BaseViewHolder;

public class CommentViewHolder extends BaseViewHolder<BBS.Comment> {

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
            Glide.with(itemView.getContext())
                    .load(comment.avatar)
                    .transform(new GlideCircleTransform(itemView.getContext()))
                    .into(mAvatar);
            mName.setText(comment.name);
            mSign.setText(comment.sign);
            mContent.setText(comment.content);
        }
    }