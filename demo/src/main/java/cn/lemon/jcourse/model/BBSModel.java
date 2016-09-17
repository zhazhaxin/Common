package cn.lemon.jcourse.model;

import java.io.File;

import cn.lemon.common.base.model.SuperModel;
import cn.lemon.common.net.SchedulersTransformer;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.net.RetrofitModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by linlongxin on 2016/9/15.
 */

public class BBSModel extends SuperModel {

    public static BBSModel getInstance() {
        return getInstance(BBSModel.class);
    }

    public void getBBSList(int page, ServiceResponse<BBS[]> response) {
        RetrofitModel.getServiceAPI().getBBSList(page)
                .compose(new SchedulersTransformer<BBS[]>())
                .subscribe(response);
    }

    //上传图片
    public void uploadPicture(File file, ServiceResponse<Info> response) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/type"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
        RetrofitModel.getServiceAPI().uploadPicture(part)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }

    public void publishBBS(String picsJson, String title,String content, ServiceResponse<Info> response) {
        RetrofitModel.getServiceAPI().publishBBS(picsJson,title, content)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }

    public void getBBSDetail(int id,ServiceResponse<BBS> response){
        RetrofitModel.getServiceAPI().getBBSDetail(id)
                .compose(new SchedulersTransformer<BBS>())
                .subscribe(response);
    }

    public void comment(int bbsId,int objectId,String content,ServiceResponse<Info> response){
        RetrofitModel.getServiceAPI().comment(bbsId,objectId,content)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }
    public void follow(int id,ServiceResponse<Info> response){
        RetrofitModel.getServiceAPI().follow(id)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }
    public void unfollow(int id,ServiceResponse<Info> response){
        RetrofitModel.getServiceAPI().unfollow(id)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }
}
