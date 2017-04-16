package cn.lemon.jcourse.module.java;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.bean.Exercise;

/**
 * Created by linlongxin on 2017.1.22.
 */

@RequirePresenter(ExercisePresenter.class)
public class ExerciseActivity extends ToolbarActivity<ExercisePresenter> {

    private RecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.java_activity_exercise_list);
        setToolbarHomeBack(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new ExerciseAdapter());
    }

    public void setData(Exercise[] exercises){
        if(exercises.length == 0){
            showEmpty();
        }else {
            showContent();
            mAdapter.addAll(exercises);
        }
    }

}
