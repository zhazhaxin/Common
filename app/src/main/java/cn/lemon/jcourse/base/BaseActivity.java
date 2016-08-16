package cn.lemon.jcourse.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.lemon.jcourse.R;

/**
 * Created by linlongxin on 2016/7/31.
 */

public class BaseActivity extends AppCompatActivity {

    private boolean isHomeBack = true;
    private Toolbar mToolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public void setHomeBack(boolean isHomeBack) {
        this.isHomeBack = isHomeBack;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isHomeBack) {
                finish();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}
