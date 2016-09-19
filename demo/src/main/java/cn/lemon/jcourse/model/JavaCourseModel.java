package cn.lemon.jcourse.model;

import cn.lemon.common.base.model.SuperModel;
import cn.lemon.common.net.SchedulersTransformer;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.bean.JVideo;
import cn.lemon.jcourse.model.bean.JavaCourse;
import cn.lemon.jcourse.model.net.RetrofitModel;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class JavaCourseModel extends SuperModel {

    public static JavaCourseModel getInstance() {
        return getInstance(JavaCourseModel.class);
    }

    public void getTextJavaCourseList(int page, ServiceResponse<JavaCourse[]> subscriber) {
        RetrofitModel.getServiceAPI().getTextJavaCourseList(page)
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

    public void getJavaCourseFromDir(int unit, int page, ServiceResponse<JavaCourse[]> response) {
        RetrofitModel.getServiceAPI().getJavaCourseFromDir(unit, page)
                .compose(new SchedulersTransformer<JavaCourse[]>())
                .subscribe(response);
    }

    public void getVideoList(int page, ServiceResponse<JVideo[]> response){
        RetrofitModel.getServiceAPI().getVideoList(page)
                .compose(new SchedulersTransformer<JVideo[]>())
                .subscribe(response);
    }

    public void visit(int id){
        RetrofitModel.getServiceAPI().visit(id)
                .compose(new SchedulersTransformer<Info>())
                .subscribe();
    }
}
