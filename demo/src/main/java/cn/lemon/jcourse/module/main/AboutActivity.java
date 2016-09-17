package cn.lemon.jcourse.module.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;

public class AboutActivity extends ToolbarActivity implements View.OnClickListener{

    private LinearLayout mTellAuthor;
    private TextView mFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_about);

        mTellAuthor = (LinearLayout) findViewById(R.id.tell_author);
        mFeedBack = (TextView) findViewById(R.id.feedback);
        mTellAuthor.setOnClickListener(this);
        mFeedBack.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tell_author:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                            Config.REQUEST_CALL_PHONE);
                }else {
                    Utils.call(this,"18983679028");
                }
                break;
            case R.id.feedback:
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Config.REQUEST_CALL_PHONE){
            if (grantResults.length>0 && (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)){

            }else {

            }
        }
    }
}
