package cn.lemon.jcourse.module.account;

import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;

/**
 * Created by linlongxin on 2016/9/17.
 */

public class FollowListPresenter extends SuperPresenter<FollowListActivity> {

    @Override
    public void onCreate() {
        super.onCreate();
        getData();
    }

    public void getData(){
        AccountModel.getInstance().getFollowList(new ServiceResponse<Account[]>(){
            @Override
            public void onNext(Account[] accounts) {
                super.onNext(accounts);
                getView().setData(accounts);
            }
        });
    }
}
