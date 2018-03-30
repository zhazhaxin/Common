package cn.lemon.jcourse.module.java;

import android.os.Bundle;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.Exercise;

/**
 * Created by linlongxin on 2017.1.22.
 */

public class ExercisePresenter extends SuperPresenter<ExerciseActivity> {

    @Override
    public void onCreate(Bundle b) {
        int id = getView().getIntent().getIntExtra(Config.EXERCISE_ID, -1);
        getData(id);
    }

    public void getData(int id) {
        JavaCourseModel.getInstance().getExerciseList(id, new ServiceResponse<Exercise[]>() {
            @Override
            public void onNext(Exercise[] exercises) {
                super.onNext(exercises);
                getView().setData(exercises);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().showError();
            }
        });
    }
}
