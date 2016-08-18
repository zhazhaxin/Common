package cn.lemon.jcourse.module.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.lemon.common.base.RequirePresenter;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;

@RequirePresenter(JavaCourseDirListPresenter.class)
public class JavaCourseDirListActivity extends ToolbarActivity<JavaCourseDirListPresenter> implements View.OnClickListener {

    private Button mOne, mTwo, mThree, mFour, mFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.java_activity_course_dir_list);

        mOne = (Button) findViewById(R.id.one);
        mTwo = (Button) findViewById(R.id.two);
        mThree = (Button) findViewById(R.id.three);
        mFour = (Button) findViewById(R.id.four);
        mFive = (Button) findViewById(R.id.five);

        mOne.setOnClickListener(this);
        mTwo.setOnClickListener(this);
        mThree.setOnClickListener(this);
        mFour.setOnClickListener(this);
        mFive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                starActivityWithUnit(1);
                break;
            case R.id.two:
                starActivityWithUnit(2);
                break;
            case R.id.three:
                starActivityWithUnit(3);
                break;
            case R.id.four:
                starActivityWithUnit(4);
                break;
            case R.id.five:
                starActivityWithUnit(5);
                break;
        }
    }

    public void starActivityWithUnit(int unit) {
        Intent intent = new Intent(this, JavaCourseUnitListActivity.class);
        intent.putExtra(Config.JAVA_COURSE_UNIT, unit);
        startActivity(intent);
    }
}
