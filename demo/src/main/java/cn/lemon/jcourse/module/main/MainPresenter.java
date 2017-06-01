package cn.lemon.jcourse.module.main;

import android.content.DialogInterface;

import org.greenrobot.eventbus.EventBus;

import cn.lemon.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.module.account.FollowListActivity;
import cn.lemon.jcourse.module.account.LoginActivity;
import cn.lemon.jcourse.module.account.UserBBSListActivity;
import cn.lemon.jcourse.module.java.StarListActivity;

/**
 * Created by linlongxin on 2016/9/17.
 */

public class MainPresenter extends SuperPresenter<MainActivity> {

    public void checkLogin(Class activity) {
        if (!AccountModel.getInstance().isLogin()) {
            Utils.Toast("请先登录");
            getView().startActivity(LoginActivity.class);
        } else {
            getView().startActivity(activity);

        }
    }

    //退出登录
    public void loginOut() {
        if (!AccountModel.getInstance().isLogin()) {
            Utils.Toast("请先登录");
            getView().startActivity(LoginActivity.class);
        } else {
            getView().showDialog("确定要退出？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils.Toast("已退出");
                    getView().dismissDialog();
                    AccountModel.getInstance().deleteAccount();
                    getView().updateAccountInfo();
                    EventBus.getDefault().post(Config.CHECK_STATUS_FOR_GROUP_FRAGMENT);
                }
            }, null);
        }
    }

    //跳转收藏列表
    public void jumpStarList() {
        checkLogin(StarListActivity.class);
    }

    //跳转到BBS列表
    public void jumpBBSList() {
        checkLogin(UserBBSListActivity.class);
    }

    //跳转关注的人列表
    public void jumpFollowList() {
        checkLogin(FollowListActivity.class);
    }
}
