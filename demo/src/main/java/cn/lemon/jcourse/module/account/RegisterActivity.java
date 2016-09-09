package cn.lemon.jcourse.module.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Info;

public class RegisterActivity extends ToolbarActivity implements View.OnClickListener {

    private TextInputLayout mTextName;
    private TextInputLayout mTextPassword;
    private TextView mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_register);

        mTextName = (TextInputLayout) findViewById(R.id.name);
        mTextPassword = (TextInputLayout) findViewById(R.id.password);
        mRegisterButton = (TextView) findViewById(R.id.register);

        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String name = mTextName.getEditText().getText().toString();
        final String password = mTextPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Utils.Toast("用户名或密码不能为空");
            return;
        }
        if (password.length() < 6) {
            Utils.Toast("密码不能少于6位");
            return;
        }
        AccountModel.getInstance().register(name, password, new ServiceResponse<Info>() {
            @Override
            public void onNext(Info info) {
                Utils.Toast(info.info);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra(Config.NAME, name);
                intent.putExtra(Config.PASSWORD, password);
                startActivity(intent);
                finish();
            }
        });
    }
}
