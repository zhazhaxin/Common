package cn.lemon.jcourse.model;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.lemon.common.base.model.SuperModel;
import cn.lemon.common.net.HeadersInterceptor;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.Banner;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.net.RetrofitModel;
import cn.lemon.jcourse.model.net.SchedulersTransformer;
import cn.lemon.jcourse.module.ServiceResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.functions.Action1;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class AccountModel extends SuperModel {

    private Account mAccount;

    public AccountModel() {
    }

    public static AccountModel getInstance() {
        return getInstance(AccountModel.class);
    }

    public void saveAccount(Account account, boolean isUpdate) {
        putObject(Config.ACCOUNT, account);
        setAccount(account);
        setHeaders(account);
        if (isUpdate)
            EventBus.getDefault().post(Config.UPDATE_ACCOUNT_ON_DRAWER);
    }

    public void saveAccount(Account account) {
        saveAccount(account, true);
    }

    public Account getAccount() {
        if (mAccount == null) {
            mAccount = (Account) AccountModel.getInstance().getObject(Config.ACCOUNT);
        }
        return mAccount;
    }

    //把account加载到内存
    private void setAccount(Account account) {
        mAccount = account;
    }

    //设置请求头 --- UID，token
    private void setHeaders(Account account) {
        HeadersInterceptor.UID = account.id + "";
        HeadersInterceptor.TOKEN = account.token;
    }

    public void deleteAccount() {
        mAccount = null;
        clearCacheObject();
        HeadersInterceptor.UID = "";
        HeadersInterceptor.TOKEN = "";
        EventBus.getDefault().post(Config.UPDATE_ACCOUNT_ON_DRAWER);
    }

    public void getBanner(ServiceResponse<Banner> response) {
        RetrofitModel.getServiceAPI().getBanner()
                .compose(new SchedulersTransformer<Banner>())
                .subscribe(response);
    }

    public void register(String name, String password, ServiceResponse<Info> subscriber) {
        RetrofitModel.getServiceAPI().register(name, password)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(subscriber);
    }

    public void login(String name, String password, ServiceResponse<Account> subscriber) {
        RetrofitModel.getServiceAPI().login(name, password)
                .compose(new SchedulersTransformer<Account>())
                .doOnNext(new Action1<Account>() {
                    @Override
                    public void call(Account account) {
                        saveAccount(account);
                    }
                })
                .subscribe(subscriber);
    }

    public void prolongToken(ServiceResponse<Account> response) {
        if (mAccount != null) {
            setHeaders(mAccount);
        }
        RetrofitModel.getServiceAPI().prolongToken()
                .compose(new SchedulersTransformer<Account>())
                .subscribe(response);
    }

    public void updateAccountInfo(String name, String sign, String avatar, ServiceResponse<Account> response) {
        RetrofitModel.getServiceAPI().updateAcconuntInfo(name, sign, avatar)
                .compose(new SchedulersTransformer<Account>())
                .subscribe(response);
    }

    public void updateAvatar(File file, ServiceResponse<Info> response) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/type"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
        RetrofitModel.getServiceAPI().updateAvatar(part)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }
}
