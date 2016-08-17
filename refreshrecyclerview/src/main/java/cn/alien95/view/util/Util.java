package cn.alien95.view.util;

import android.content.Context;

/**
 * Created by linlongxin on 2016/1/27.
 */
public class Util {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
