package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.Exercise;
import cn.lemon.view.RefreshRecyclerView;

/**
 * Created by linlongxin on 2017.1.22.
 */

@RequirePresenter(ExercisePresenter.class)
public class ExerciseActivity extends ToolbarActivity<ExercisePresenter> {

    private RefreshRecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.java_activity_exercise_list);
        setToolbarHomeBack(true);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mAdapter = new ExerciseAdapter(this));
    }

    public void setData(Exercise[] exercises){
        mAdapter.addAll(exercises);
    }

}
