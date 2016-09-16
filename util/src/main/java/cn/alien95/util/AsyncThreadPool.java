package cn.alien95.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by linlongxin on 2016/9/16.
 */

public class AsyncThreadPool {

    private ExecutorService mThreadPool;

    private static AsyncThreadPool mInstance;

    public AsyncThreadPool(){
        mThreadPool = Executors.newFixedThreadPool(2);
    }
    public static AsyncThreadPool getInstance(){
        if(mInstance == null){
            mInstance = new AsyncThreadPool();
        }
        return mInstance;
    }

    public void executeRunnable(Runnable runnable){
        mThreadPool.execute(runnable);
    }

}
