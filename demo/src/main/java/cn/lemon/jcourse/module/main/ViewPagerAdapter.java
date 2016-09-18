package cn.lemon.jcourse.module.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.jcourse.module.account.GroupFragment;
import cn.lemon.jcourse.module.bbs.BBSFragment;
import cn.lemon.jcourse.module.java.TextListFragment;
import cn.lemon.jcourse.module.java.VideoFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Map<Integer,Fragment> mFragments;
    private List<String> mTitles;
    private FragmentManager mFragmentManager;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
        mFragments = new HashMap<>();
        mTitles = new ArrayList<>();
    }

    //ViewPager加载进Fragment时每创建fragment调用一次
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new GroupFragment();
        } else if (position == 1) {
            fragment = new TextListFragment();
        } else if (position == 2) {
            fragment = new VideoFragment();
        } else if (position == 3) {
            fragment = new BBSFragment();
        }

        mFragments.put(position,fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mFragmentManager.beginTransaction().hide(mFragments.get(position)).commit();
    }

    public void addTitle(String title) {
        mTitles.add(title);
    }
}