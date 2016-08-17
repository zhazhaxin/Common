package cn.lemon.jcourse.module;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.config.Config;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.module.account.UpdateInfoActivity;
import cn.lemon.jcourse.module.java.StarJCourseListActivity;
import cn.lemon.jcourse.module.java.TextFragment;
import cn.lemon.jcourse.module.java.VideoFragment;

public class MainActivity extends ToolbarActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerAdapter mAdapter;
    private ImageView mAvatar;
    private TextView mName;
    private TextView mSign;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    private long mFirstPressBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarHomeBack(false);
        setContentView(R.layout.main_activity);

        EventBus.getDefault().register(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View header = mNavigationView.getHeaderView(0);
        mAvatar = (ImageView) header.findViewById(R.id.avatar);
        mName = (TextView) header.findViewById(R.id.name);
        mSign = (TextView) header.findViewById(R.id.sign);
        mAvatar.setOnClickListener(this);
        mName.setOnClickListener(this);
        mSign.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mAdapter = new ViewPagerAdapter();
        mAdapter.addFragment(new TextFragment(), "课程");
        mAdapter.addFragment(new VideoFragment(), "视频");
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        updateAccountInfo();
    }

    public void updateAccountInfo() {
        Account account = AccountModel.getInstance().getAccount();
        if (account != null) {
            Glide.with(MainActivity.this)
                    .load(account.avatar)
                    .into(mAvatar);
            mName.setText(account.name);
            mSign.setText(account.sign);
        } else {
            mAvatar.setImageResource(R.drawable.ic_avatar);
            mName.setText("昵称");
            mSign.setText("个性签名");
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mFirstPressBackTime > 1000) {
                mFirstPressBackTime = System.currentTimeMillis();
                Utils.Toast("再点击一次退出App");
            } else
                super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.java:
                break;
            case R.id.android:
                break;
            case R.id.star:
                jumpStarList();
                break;
            case R.id.about:
                break;
            case R.id.login_out:
                loginOut();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean jumpStarList(){
        if (AccountModel.getInstance().getAccount() == null) {
            Utils.Toast("请先登录");
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        mNavigationView.setCheckedItem(R.id.java);
        startActivity(new Intent(this, StarJCourseListActivity.class));
        return true;
    }

    public void loginOut() {
        if (AccountModel.getInstance().getAccount() == null) {
            Utils.Toast("请先登录");
            return;
        }
        showDialog("确定要退出？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccountModel.getInstance().deleteAccount();
                dismissDialog();
                Utils.Toast("已退出");
            }
        }, null);
    }

    @Override
    public void onClick(View v) {
        if (AccountModel.getInstance().getAccount() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, UpdateInfoActivity.class));
        }

    }

    @Subscribe
    public void onEvent(String updateAccountInfoEvent) {
        if (updateAccountInfoEvent.equals(Config.UPDATE_ACCOUNT_ON_DRAWER)) {
            updateAccountInfo();
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public ViewPagerAdapter() {
            super(getSupportFragmentManager());
            mFragments = new ArrayList<>();
            mTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mTitles.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mTitles.add(title);
        }
    }
}
