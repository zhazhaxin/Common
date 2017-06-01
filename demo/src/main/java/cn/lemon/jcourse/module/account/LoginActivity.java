package cn.lemon.jcourse.module.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.lemon.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;

public class LoginActivity extends ToolbarActivity implements View.OnClickListener {

    private TextInputLayout mTextName;
    private TextInputLayout mTextPassword;
    private TextView mLoginButton;
    private TextView mRegisterButton;
    private TextView mForgetPassword;

    private String mName;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(false);
        setContentView(R.layout.account_activity_login);
        setToolbarHomeBack(true);

        mTextName = (TextInputLayout) findViewById(R.id.name);
        mTextPassword = (TextInputLayout) findViewById(R.id.password);
        mLoginButton = (TextView) findViewById(R.id.login);
        mRegisterButton = (TextView) findViewById(R.id.register);
        mForgetPassword = (TextView) findViewById(R.id.forget_password);
        mLoginButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);

        mName = getIntent().getStringExtra(Config.NAME);
        mPassword = getIntent().getStringExtra(Config.PASSWORD);
        if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPassword)) {
            mTextName.getEditText().setText(mName);
            mTextPassword.getEditText().setText(mPassword);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.forget_password:
                break;
            case R.id.login:
                login();
                break;
        }

    }

    public void login() {
        Utils.closeInputMethod(this);
        String name = mTextName.getEditText().getText().toString();
        String password = mTextPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Utils.Toast("用户名或密码不能为空");
            return;
        }
        if (password.length() < 6) {
            Utils.Toast("密码不能少于6位");
            return;
        }
        AccountModel.getInstance().login(name, password, new ServiceResponse<Account>() {
            @Override
            public void onNext(Account account) {
                Utils.Toast("登录成功");
                EventBus.getDefault().post(Config.CHECK_STATUS_FOR_GROUP_FRAGMENT);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Utils.Toast("用户名或密码错误");
            }
        });
    }
}
