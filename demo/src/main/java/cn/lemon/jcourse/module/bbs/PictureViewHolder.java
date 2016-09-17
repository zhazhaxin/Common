package cn.lemon.jcourse.module.bbs;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.net.RotateTransformation;
import cn.lemon.view.adapter.BaseViewHolder;

public class PictureViewHolder extends BaseViewHolder<String> {

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
        Glide.with(itemView.getContext())
                .load(object)
                .placeholder(R.drawable.ic_place_holder)
                .transform(new RotateTransformation(itemView.getContext()))
                .into(mPicture);
    }
}