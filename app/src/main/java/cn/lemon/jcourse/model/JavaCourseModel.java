package cn.lemon.jcourse.model;

import cn.lemon.common.base.model.SuperModel;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.model.net.RetrofitModel;
import cn.lemon.jcourse.model.net.SchedulersTransformer;
import cn.lemon.jcourse.module.ServiceResponse;
import rx.Subscriber;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class JavaCourseModel extends SuperModel {

    public static JavaCourseModel getInstance() {
        return getInstance(JavaCourseModel.class);
    }

    public void getTextJavaCourseList(int page, int pageNum, Subscriber<JavaCourse[]> subscriber) {
        RetrofitModel.getServiceAPI().getTextJavaCourseList(page, pageNum)
                .compose(new SchedulersTransformer<JavaCourse[]>())
                .subscribe(subscriber);
    }

    public void getIsStar(int id, ServiceResponse<Info> response) {
        RetrofitModel.getServiceAPI().getIsStar(id)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }

    public void starJCourse(int id, ServiceResponse<Info> response) {
        RetrofitModel.getServiceAPI().starJCourse(id)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }

    public void unstarJCourse(int id, ServiceResponse<Info> response) {
        RetrofitModel.getServiceAPI().unstarJCourse(id)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }

    public void getStarJCourseList(int page, ServiceResponse<JavaCourse[]> response) {
        RetrofitModel.getServiceAPI().getStarJCourseList(page)
                .compose(new SchedulersTransformer<JavaCourse[]>())
                .subscribe(response);
    }

}
