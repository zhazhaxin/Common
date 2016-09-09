package cn.lemon.common.base.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class SuperModel {

    private final String TAG = "SuperModel";
    private final String OBJECT_CACHE = "ObjectCache"; //缓存对象文件目录
    private String mObjectCachePath;
    private static Context mContext;
    private static Map<String, SuperModel> mInstanceMap = new HashMap<>();
    protected SharedPreferences mSP;
    protected SharedPreferences.Editor mEditor;

    public SuperModel() {
        mSP = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        mEditor = mSP.edit();
        mObjectCachePath = mContext.getExternalFilesDir(OBJECT_CACHE).getAbsolutePath();
    }

    //这样去写单例模式虽然可以省去很多代码，不过因为newInstance方法有限制：构造函数必须public,必须有一个构造函数没有参数
    public static void initialize(Context context) {
        mContext = context;
    }

    public static <T extends SuperModel> T getInstance(Class<T> model) {
        if(!mInstanceMap.containsKey(model.getSimpleName())){
            synchronized (model){
                try {
                    T instance = model.newInstance();
                    mInstanceMap.put(model.getSimpleName(), instance);
                    return instance;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return (T) mInstanceMap.get(model.getSimpleName());
    }

    /**
     * 通过sp保存基本类型数据
     */
    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public int getInt(String key, int defaultInt) {
        return mSP.getInt(key, defaultInt);
    }

    public void putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public float getFloat(String key, float defaultFloat) {
        return mSP.getFloat(key, defaultFloat);
    }

    public void putLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    public long getLong(String key, long defaultLong) {
        return mSP.getLong(key, defaultLong);
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public String getString(String key, String defaultString) {
        return mSP.getString(key, defaultString);
    }

    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public boolean getBooolean(String key, boolean defaultBoolean) {
        return mSP.getBoolean(key, defaultBoolean);
    }

    public void setStringSet(String key, Set<String> set) {
        mEditor.putStringSet(key, set);
    }

    public Set<String> getStringSet(String key, Set<String> defaultSet) {
        return mSP.getStringSet(key, defaultSet);
    }

    /**
     * 通过文件保存对象数据，对象必须可序列化。通过清理app数据即可清理掉数据
     * 目录:SDCard/Android/data/应用包名/data/files/ObjectCache
     */
    public void putObject(String key, Object value) {
        File objectFile = new File(mObjectCachePath + "/" + key);
        if (objectFile.exists()) {
            objectFile.delete();
        }
        try {
            objectFile.createNewFile();
            ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream(objectFile));
            obs.writeObject(value);
            obs.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "文件流打开失败。");
        }
    }

    public Object getObject(String key) {
        File objectFile = new File(mObjectCachePath + "/" + key);
        if (!objectFile.exists()) {
            Log.i(TAG, "该对象没有被缓存");
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectFile));
            Object result = ois.readObject();
            ois.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "对象缓存读取失败");
        }
        return null;
    }

    public void clearCacheObject() {
        File cacheDir = new File(mObjectCachePath);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            deleteDir(cacheDir);
        }
    }

    protected void deleteDir(File dir) {
        if (dir.isDirectory() && dir.listFiles().length > 0) {
            for (File file : dir.listFiles()) {
                deleteDir(file);
            }
        } else {
            dir.delete();
        }

    }
}
