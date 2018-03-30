package cn.lemon.jcourse.module.java;

import android.os.Bundle;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.JVideo;

/**
 * Created by linlongxin on 2016/9/14.
 */

public class VideoPresenter extends SuperPresenter<VideoFragment> {

    private int page = 0;
    @Override
    public void onCreate(Bundle b) {
        getData(true);
    }

    public void getData(final boolean isRefresh){
        if(isRefresh){
            page = 0;
        }
        JavaCourseModel.getInstance().getVideoList(page,new ServiceResponse<JVideo[]>(){
            @Override
            public void onNext(JVideo[] jVideos) {
                super.onNext(jVideos);
                getView().showContent();
                getView().setData(jVideos,isRefresh);
                page++;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().showError();
            }
        });
    }
}
