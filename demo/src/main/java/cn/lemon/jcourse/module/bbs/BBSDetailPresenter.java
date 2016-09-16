package cn.lemon.jcourse.module.bbs;

import cn.alien95.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.BBSModel;
import cn.lemon.jcourse.model.bean.BBS;

/**
 * Created by linlongxin on 2016/9/16.
 */

public class BBSDetailPresenter extends SuperPresenter<BBSDetailActivity> {

    private int bbsId;

    @Override
    public void onCreate() {
        super.onCreate();
        bbsId = getView().getIntent().getIntExtra(Config.BBS_DETAIL_ID,0);
        getData();
    }

    public void getData(){
        if(bbsId == 0){
            Utils.Toast("获取失败");
            return;
        }
        BBSModel.getInstance().getBBSDetail(bbsId,new ServiceResponse<BBS>(){
            @Override
            public void onNext(BBS bbs) {
                super.onNext(bbs);
                getView().setData(bbs);
            }
        });
    }
}
