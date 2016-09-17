package cn.lemon.jcourse.module.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.Banner;

public class SplashActivity extends Activity {

    private ImageView mBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_splash);
        mBanner = (ImageView) findViewById(R.id.banner);

        //获取Banner
        AccountModel.getInstance().getBanner(new ServiceResponse<Banner>() {
            @Override
            public void onNext(Banner banner) {
                super.onNext(banner);
                Glide.with(SplashActivity.this)
                        .load(banner.imageUrl)
                        .into(mBanner);
            }
        });

        //更新用户信息
        if (AccountModel.getInstance().getAccount() != null) {
            AccountModel.getInstance().prolongToken(new ServiceResponse<Account>() {
                @Override
                public void onNext(Account account) {
                    super.onNext(account);
                    AccountModel.getInstance().saveAccount(account);
                }
            });
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                jumpHome();
            }
        }, 2000);
    }

    public void jumpHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
