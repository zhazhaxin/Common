package cn.lemon.jcourse.module;

import android.os.Bundle;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;

public class AboutActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_about);
    }
}
