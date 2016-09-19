package cn.lemon.jcourse.module.java;

import android.content.Intent;

import cn.alien95.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.JavaCourseModel;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.module.account.LoginActivity;

/**
 * Created by linlongxin on 2016/8/18.
 */

public class TextDetailPresenter extends SuperPresenter<TextDetailActivity> {

    private JavaCourse mData;
    private boolean isStar = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mData = (JavaCourse) getView().getIntent().getSerializableExtra(Config.JAVA_COURSE_DETAIL);
        getView().setData(mData);
        setIsStar(mData.id);
        visit(mData.id);
    }

    public void setIsStar(int id) {
        if (AccountModel.getInstance().getAccount() != null) {
            JavaCourseModel.getInstance().getIsStar(id, new ServiceResponse<Info>() {
                @Override
                public void onNext(Info s) {
                    super.onNext(s);
                    if (s.star) {
                        getView().getStarView().setImageResource(R.drawable.ic_star);
                        isStar = true;
                    }
                }
            });
        }
    }

    public void starViewClick() {
        if (AccountModel.getInstance().getAccount() == null) {
            Utils.Toast("请先登录");
            getView().startActivity(new Intent(getView(), LoginActivity.class));
            return;
        }
        if (isStar) {
            JavaCourseModel.getInstance().unstarJCourse(mData.id, new ServiceResponse<Info>() {
                @Override
                public void onNext(Info info) {
                    super.onNext(info);
                    Utils.Toast("取消收藏");
                    getView().setStarNumDec();
                    getView().getStarView().setImageResource(R.drawable.ic_unstar);
                    isStar = false;
                }
            });
        } else {
            JavaCourseModel.getInstance().starJCourse(mData.id, new ServiceResponse<Info>() {
                @Override
                public void onNext(Info info) {
                    super.onNext(info);
                    Utils.Toast("收藏成功");
                    getView().setStarNumAdd();
                    getView().getStarView().setImageResource(R.drawable.ic_star);
                    isStar = true;
                }
            });
        }
    }

    public void visit(int id){
        JavaCourseModel.getInstance().visit(id);
    }
}
