package cn.lemon.jcourse.module.bbs;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.net.RotateTransformation;
import cn.lemon.multi.ui.ViewImageActivity;
import cn.lemon.view.adapter.BaseViewHolder;

public class PictureViewHolder extends BaseViewHolder<String> {

    private ImageView mPicture;
    private static List<String> mData = new ArrayList<>();

    public PictureViewHolder(ViewGroup parent) {
        super(parent, R.layout.bbs_holder_detail_picture);
    }

    public static void clearPictures(){
        mData.clear();
    }

    @Override
    public void onInitializeView() {
        mPicture = findViewById(R.id.picture);
    }

    @Override
    public void setData(String object) {
        super.setData(object);
        if (!mData.contains(object)) {
            mData.add(object);
        }
        Glide.with(itemView.getContext())
                .load(object)
                .transform(new RotateTransformation(itemView.getContext()))
                .placeholder(R.drawable.ic_place_holder)
                .into(mPicture);
    }

    @Override
    public void onItemViewClick(String object) {
        Intent intent = new Intent(itemView.getContext(), ViewImageActivity.class);
        intent.putExtra(ViewImageActivity.IMAGE_NUM, getAdapterPosition() - 1);
        intent.putExtra(ViewImageActivity.IMAGES_DATA_LIST, (Serializable) mData);
        itemView.getContext().startActivity(intent);
    }
}