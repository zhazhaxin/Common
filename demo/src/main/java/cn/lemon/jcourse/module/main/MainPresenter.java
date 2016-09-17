package cn.lemon.jcourse.module.main;

import cn.alien95.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.module.account.LoginActivity;

/**
 * Created by linlongxin on 2016/9/17.
 */

public class MainPresenter extends SuperPresenter<MainActivity> {

    public void checkoutLogin(){
        if (!AccountModel.getInstance().isLogin()) {
            Utils.Toast("请先登录");
            getView().startActivity(LoginActivity.class);
        }
    }
}
