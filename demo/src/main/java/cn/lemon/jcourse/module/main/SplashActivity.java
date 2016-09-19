package cn.lemon.jcourse.module.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.bean.Banner;

public class SplashActivity extends Activity {

    private ImageView mBanner;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_splash);
        mBanner = (ImageView) findViewById(R.id.banner);

        //获取Banner
        AccountModel.getInstance().getBanner(new ServiceResponse<Banner>() {
            @Override
            public void onNext(final Banner banner) {
                super.onNext(banner);
                Glide.with(SplashActivity.this)
                        .load(banner.imageUrl)
                        .into(mBanner);
                mBanner.setClickable(true);
                mBanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SplashActivity.this, WebActivity.class);
                        intent.putExtra(Config.WEB_VIEW_BANNER, banner);
                        Intent intentMain =new Intent(SplashActivity.this,MainActivity.class);
                        Intent[] intents = new Intent[]{intentMain,intent};
                        startActivities(intents);
                        finish();
                    }
                });
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

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
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

    @Override
    public void finish() {
        super.finish();
        mTimer.cancel();
        mTimer = null;
    }
}
