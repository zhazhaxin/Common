package cn.lemon.jcourse.module.bbs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.Banner;

/**
 * Created by linlongxin on 2016/9/19.
 */

public class BannerAdapter extends StaticPagerAdapter {

    private Banner[] mData;
    private Context mContext;

    public BannerAdapter(Context context) {
        mContext = context;
    }

    public void addAll(Banner[] data){
        mData = data;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        Glide.with(mContext)
                .load(mData[position].imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .into(view);
        return view;
    }

    @Override
    public int getCount() {
        return mData.length;
    }
}
