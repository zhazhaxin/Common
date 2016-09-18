package cn.lemon.jcourse.module.bbs;

import android.content.Intent;
import android.text.TextUtils;

import cn.alien95.util.Utils;
import cn.lemon.common.base.presenter.SuperPresenter;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.BBSModel;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.module.account.LoginActivity;

/**
 * Created by linlongxin on 2016/9/16.
 */

public class BBSDetailPresenter extends SuperPresenter<BBSDetailActivity> {

    private int bbsId;

    @Override
    public void onCreate() {
        super.onCreate();
        bbsId = getView().getIntent().getIntExtra(Config.BBS_DETAIL_ID, 0);
        getData();
    }

    public void getData() {
        if (bbsId == 0) {
            Utils.Toast("获取失败");
            return;
        }
        BBSModel.getInstance().getBBSDetail(bbsId, new ServiceResponse<BBS>() {
            @Override
            public void onNext(BBS bbs) {
                super.onNext(bbs);
                getView().setData(bbs);
            }
        });
    }

    public void comment(final int objectId, String content) {
        if (TextUtils.isEmpty(content)) {
            Utils.Toast("内容不能为空");
            return;
        }
        if (!AccountModel.getInstance().isLogin()) {
            Utils.Toast("请先登录");
            getView().startActivity(new Intent(getView(), LoginActivity.class));
            return;
        }
        if (objectId > 0) {  //有回复对象
            StringBuilder sb = new StringBuilder(content);
            sb.delete(0, getView().mObjectName.length() + 1);
            content = sb.toString();
        }
        final BBS.Comment comment = new BBS.Comment();
        comment.name = AccountModel.getInstance().getAccount().name;
        comment.avatar = AccountModel.getInstance().getAccount().avatar;
        comment.sign = AccountModel.getInstance().getAccount().sign;
        comment.time = System.currentTimeMillis() / 1000;
        comment.content = content;
        comment.objectName = getView().mObjectName;
        comment.objectId = objectId;
        BBSModel.getInstance().comment(bbsId, objectId, content,
                new ServiceResponse<Info>() {
                    @Override
                    public void onNext(Info info) {
                        super.onNext(info);
                        Utils.Toast("评论成功");
                        getView().clearText();
                        getView().addComment(comment);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Utils.Toast("评论失败");
                    }
                });
    }
}
