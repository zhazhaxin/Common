package cn.lemon.jcourse.module.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.net.ServiceResponse;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Info;

public class FeedbackActivity extends ToolbarActivity {

    private EditText mFeedContent;
    private EditText mRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_feedback);
        mFeedContent = (EditText) findViewById(R.id.feed_content);
        mRelation = (EditText) findViewById(R.id.relation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit) {
            feedback(mFeedContent.getText().toString(),mRelation.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    public void feedback(String content,String relation){
        if(TextUtils.isEmpty(content)){
            Utils.Toast("意见不能为空");
            return;
        }
        AccountModel.getInstance().feedback(content,relation,new ServiceResponse<Info>(){
            @Override
            public void onNext(Info info) {
                super.onNext(info);
                Utils.Toast(info.info);
                finish();
            }
        });
    }
}
