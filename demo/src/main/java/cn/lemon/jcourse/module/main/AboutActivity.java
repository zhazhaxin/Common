package cn.lemon.jcourse.module.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;

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
                Utils.jumpDialUI(this,"18983679028");
                break;
            case R.id.feedback:
                startActivity(new Intent(this,FeedbackActivity.class));
                break;
        }
    }
}
