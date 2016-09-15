package cn.lemon.jcourse.model;

import cn.lemon.common.base.model.SuperModel;
import cn.lemon.common.net.SchedulersTransformer;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.net.RetrofitModel;

/**
 * Created by linlongxin on 2016/9/15.
 */

public class BBSModel extends SuperModel {

    public static BBSModel getInstance(){
        return getInstance(BBSModel.class);
    }

    public void getBBSList(int page, ServiceResponse<BBS[]> response){
        RetrofitModel.getServiceAPI().getBBSList(page)
                .compose(new SchedulersTransformer<BBS[]>())
                .subscribe(response);
    }
}
