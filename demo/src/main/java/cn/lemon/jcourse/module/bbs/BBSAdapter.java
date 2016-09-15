package cn.lemon.jcourse.module.bbs;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.BBS;
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


    class BBSViewHolder extends BaseViewHolder<BBS>{

        private ImageView mAvatar;
        private TextView mName;
        private TextView mSign;
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
            mContent = findViewById(R.id.content);
            mMultiView = findViewById(R.id.multi_view);
        }

        @Override
        public void setData(BBS bbs) {
            Glide.with(mContext)
            .load(bbs.avatar)
            .into(mAvatar);
            mContent.setText(bbs.content);
            mName.setText(bbs.name);
            mSign.setText(bbs.sign);
//            mMultiView.setImages();
        }
    }
}
