package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.lemon.common.base.fragment.SuperFragment;
import cn.lemon.jcourse.R;
import cn.lemon.view.RefreshRecyclerView;

/**
 * Created by linlongxin on 2016/8/6.
 */

public class VideoFragment extends SuperFragment {

    private RefreshRecyclerView mRecyclerView;

    public VideoFragment() {
        super(R.layout.java_fragment_video_course_list,true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        showEmpty();
    }
}
