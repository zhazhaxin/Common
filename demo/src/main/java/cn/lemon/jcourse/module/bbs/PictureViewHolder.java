package cn.lemon.jcourse.module.bbs;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.Picture;
import cn.lemon.multi.ui.ViewImageActivity;
import cn.lemon.view.adapter.BaseViewHolder;

public class PictureViewHolder extends BaseViewHolder<Picture> {

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
    public void setData(Picture data) {
        super.setData(data);
        if (!mData.contains(data.url)) {
            mData.add(data.url);
        }
//        float ratio = (float)data.height / data.width;
//        mPicture.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,));
        Glide.with(itemView.getContext())
                .load(data.url)
                .placeholder(R.drawable.ic_place_holder)
                .into(mPicture);
    }

    @Override
    public void onItemViewClick(Picture data) {
        Intent intent = new Intent(itemView.getContext(), ViewImageActivity.class);
        intent.putExtra(ViewImageActivity.IMAGE_NUM, getAdapterPosition() - 1);
        intent.putExtra(ViewImageActivity.IMAGES_DATA_LIST, (Serializable) mData);
        itemView.getContext().startActivity(intent);
    }
}