package cn.lemon.jcourse.module.bbs;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.view.adapter.BaseViewHolder;

public class ContentViewHolder extends BaseViewHolder<BBS> {

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
            super.setData(bbs);
            Glide.with(itemView.getContext())
                    .load(bbs.avatar)
                    .transform(new GlideCircleTransform(itemView.getContext()))
                    .into(mAvatar);
            mName.setText(bbs.name);
            mSign.setText(bbs.sign);
            mTitle.setText(bbs.title);
            mContent.setText(bbs.content);
        }
    }