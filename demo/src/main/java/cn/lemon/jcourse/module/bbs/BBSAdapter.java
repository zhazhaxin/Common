package cn.lemon.jcourse.module.bbs;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.multi.MultiView;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


/**
 * Created by linlongxin on 2016/9/14.
 */

public class BBSAdapter extends RecyclerAdapter<BBS> {

    private Context mContext;

    public BBSAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public BaseViewHolder<BBS> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new BBSViewHolder(parent);
    }


    class BBSViewHolder extends BaseViewHolder<BBS> {

        private ImageView mAvatar;
        private TextView mName;
        private TextView mSign;
        private TextView mTitle;
        private TextView mContent;
        private MultiView mMultiView;

        public BBSViewHolder(ViewGroup parent) {
            super(parent, R.layout.bbs_holder_list);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mAvatar = findViewById(R.id.avatar);
            mName = findViewById(R.id.name);
            mSign = findViewById(R.id.sign);
            mTitle = findViewById(R.id.title);
            mContent = findViewById(R.id.content);
            mMultiView = findViewById(R.id.multi_view);
        }

        @Override
        public void setData(BBS bbs) {
            Glide.with(mContext)
                    .load(bbs.avatar)
                    .transform(new GlideCircleTransform(mContext))
                    .into(mAvatar);
            mName.setText(bbs.name);
            mSign.setText(bbs.sign);
            mTitle.setText(bbs.title);
            mContent.setText(bbs.content);
            if (bbs.pictures.length() > 0) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<String>>() {}.getType();
                List<String> pics = gson.fromJson(bbs.pictures, listType);
                mMultiView.setImages(pics);
            }
        }
    }
}
