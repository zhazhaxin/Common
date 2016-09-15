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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.jcourse.R;
import cn.lemon.jcourse.model.AccountModel;
import cn.lemon.jcourse.model.bean.Account;
import cn.lemon.jcourse.model.net.GlideCircleTransform;
import cn.lemon.jcourse.module.account.LoginActivity;
import cn.lemon.jcourse.module.account.UpdateInfoActivity;
import cn.lemon.jcourse.module.bbs.BBSFragment;
import cn.lemon.jcourse.module.java.CourseDirListActivity;
import cn.lemon.jcourse.module.java.StarListActivity;
import cn.lemon.jcourse.module.java.TextListFragment;
import cn.lemon.jcourse.module.java.VideoFragment;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

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
        mAdapter.addFragment(new TextListFragment(), "课程");
        mAdapter.addFragment(new VideoFragment(), "视频");
        mAdapter.addFragment(new BBSFragment(), "社区");
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAccountInfo();
    }

    public void updateAccountInfo() {
        Account account = AccountModel.getInstance().getAccount();
        if (account != null) {
            Glide.with(MainActivity.this)
                    .load(account.avatar)
                    .transform(new GlideCircleTransform(this))
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
        } else if (JCVideoPlayer.backPress()) {
            return;
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
            case R.id.star:
                jumpStarList();
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.login_out:
                loginOut();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //跳转收藏列表
    public boolean jumpStarList() {
        if (AccountModel.getInstance().getAccount() == null) {
            Utils.Toast("请先登录");
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }
        startActivity(new Intent(this, StarListActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.dir) {
            startActivity(new Intent(this, CourseDirListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ViewPage
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
