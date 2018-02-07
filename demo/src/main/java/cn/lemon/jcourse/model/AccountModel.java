package cn.lemon.jcourse.model;

import org.reactivestreams.Subscription;

import java.io.File;
import cn.lemon.common.base.model.SuperModel;
import cn.lemon.common.net.HeadersInterceptor;
import cn.lemon.common.net.SchedulersTransformer;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.BBS;
import cn.lemon.jcourse.model.bean.Banner;
import cn.lemon.jcourse.model.bean.Info;
import cn.lemon.jcourse.model.bean.Picture;
import cn.lemon.jcourse.model.net.RetrofitModel;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public void saveAccount(Account account) {
        putObject(Config.ACCOUNT, account);
        setAccount(account);
        setHeaders(account);
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
    public void setHeaders(Account account) {
        HeadersInterceptor.UID = account.id + "";
        HeadersInterceptor.TOKEN = account.token;
    }

    public void deleteAccount() {
        mAccount = null;
        clearCacheObject();
        HeadersInterceptor.UID = "";
        HeadersInterceptor.TOKEN = "";
    }

    public boolean isLogin(){
        if(getAccount() == null){
            return false;
        }else {
            return true;
        }
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
                .doOnNext(new Consumer<Account>() {
                    @Override
                    public void accept(Account account) throws Exception {
                        saveAccount(account);
                    }
                })
                .subscribe(subscriber);
    }

    public void prolongToken(ServiceResponse<Account> response) {
        RetrofitModel.getServiceAPI().prolongToken()
                .compose(new SchedulersTransformer<Account>())
                .subscribe(response);
    }

    public void updateAccountInfo(String name, String sign, String avatar, ServiceResponse<Account> response) {
        RetrofitModel.getServiceAPI().updateAccountInfo(name, sign, avatar)
                .compose(new SchedulersTransformer<Account>())
                .subscribe(response);
    }

    public void updateAvatar(File file, int width, int height, ServiceResponse<Picture> response) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/type"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), requestBody);
        RetrofitModel.getServiceAPI().uploadPicture(part, width, height)
                .compose(new SchedulersTransformer<Picture>())
                .subscribe(response);
    }

    public void getUserBBSList(int id, ServiceResponse<BBS[]> response){
        RetrofitModel.getServiceAPI().getUserBBSList(id)
                .compose(new SchedulersTransformer<BBS[]>())
                .subscribe(response);
    }

    public void getFollowList(ServiceResponse<Account[]> response){
        RetrofitModel.getServiceAPI().getFollowList()
                .compose(new SchedulersTransformer<Account[]>())
                .subscribe(response);
    }

    public void group(int page,ServiceResponse<BBS[]> response){
        RetrofitModel.getServiceAPI().group(page)
                .compose(new SchedulersTransformer<BBS[]>())
                .subscribe(response);
    }
    public void feedback(String content,String relation,ServiceResponse<Info> response){
        RetrofitModel.getServiceAPI().feedback(content,relation)
                .compose(new SchedulersTransformer<Info>())
                .subscribe(response);
    }
}
