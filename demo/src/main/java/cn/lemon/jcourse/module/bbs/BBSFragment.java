package cn.lemon.jcourse.module.bbs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.view.RefreshRecyclerView;

/**
 * Created by linlongxin on 2016/9/14.
 */
@RequirePresenter(BBSPresenter.class)
public class BBSFragment extends SuperFragment<BBSPresenter> {

    private RefreshRecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bbs_fragment, container,false);
        mRecyclerView = (RefreshRecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }
}
