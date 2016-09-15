package cn.lemon.jcourse.module.bbs;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.BBSModel;
import cn.lemon.jcourse.model.bean.BBS;

/**
 * Created by linlongxin on 2016/9/14.
 */

public class BBSPresenter extends SuperPresenter<BBSFragment> {

    private int mPage = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        getData(true);
    }

    public void getData(final boolean isRefresh){
        if(isRefresh){
            mPage = 0;
        }
        BBSModel.getInstance().getBBSList(mPage,new ServiceResponse<BBS[]>(){
            @Override
            public void onNext(BBS[] bbses) {
                getView().setData(bbses,isRefresh);
                mPage++;
            }
        });
    }
}
