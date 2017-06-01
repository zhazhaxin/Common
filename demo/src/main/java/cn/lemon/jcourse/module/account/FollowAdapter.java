package cn.lemon.jcourse.module.account;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.lemon.util.TimeTransform;
import cn.lemon.util.Utils;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.BBSModel;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.net.CircleTransform;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by linlongxin on 2016/9/17.
 */

public class FollowAdapter extends RecyclerAdapter<Account> {

    public FollowAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<Account> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new FollowerViewHolder(parent);
    }

    class FollowerViewHolder extends BaseViewHolder<Account> implements View.OnClickListener {

        private ImageView mAvatar;
        private TextView mName;
        private TextView mSign;
        private TextView mTime;
        private TextView mUnfollow;
        private Account mAccount;

        public FollowerViewHolder(ViewGroup parent) {
            super(parent, R.layout.account_holder_follow);
        }

        @Override
        public void onInitializeView() {
            super.onInitializeView();
            mName = findViewById(R.id.name);
            mSign = findViewById(R.id.sign);
            mAvatar = findViewById(R.id.avatar);
            mTime = findViewById(R.id.time);
            mUnfollow = findViewById(R.id.un_follow);
        }

        @Override
        public void setData(Account object) {
            super.setData(object);
            mAccount = object;
            Glide.with(itemView.getContext())
                    .load(object.avatar)
                    .transform(new CircleTransform(itemView.getContext()))
                    .into(mAvatar);
            mName.setText(object.name);
            mSign.setText(object.sign);
            mTime.setText(TimeTransform.getRecentlyDate(object.time * 1000) + " 创建");
            mUnfollow.setOnClickListener(this);
            mName.setOnClickListener(this);
            mSign.setOnClickListener(this);
            mAvatar.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if(v.getId() == R.id.un_follow){
                BBSModel.getInstance().unfollow(mAccount.id, new ServiceResponse<Info>() {
                    @Override
                    public void onNext(Info info) {
                        super.onNext(info);
                        Utils.Toast(info.info);
                        mUnfollow.setTextColor(v.getContext().getResources().getColor(R.color.grey));
                        mUnfollow.setBackgroundResource(R.drawable.bg_frame_grey);
                        mUnfollow.setClickable(false);
                    }

                });
            }else {
                Intent intent = new Intent(itemView.getContext(), UserBBSListActivity.class);
                intent.putExtra(Config.USER_BBS_LIST, mAccount.id);
                itemView.getContext().startActivity(intent);
            }

        }
    }
}
