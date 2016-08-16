package cn.lemon.jcourse.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_splash);

        if (AccountModel.getInstance().getAccount() != null) {
            AccountModel.getInstance().prolongToken(new ServiceResponse<Account>() {
                @Override
                public void onNext(Account account) {
                    super.onNext(account);
                    AccountModel.getInstance().saveAccount(account);
                    jumpHome();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    jumpHome();
                }
            });
        } else {
            jumpHome();
        }
    }

    public void jumpHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
