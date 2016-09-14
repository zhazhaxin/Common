package cn.lemon.jcourse.module.bbs;

import android.content.Context;
import android.view.ViewGroup;

import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

/**
 * Created by linlongxin on 2016/9/14.
 */

public class BBSAdapter extends RecyclerAdapter<BBS> {

    public BBSAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<BBS> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
