package cn.lemon.jcourse.config;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class Config {

    //服务器
    public static final String BASE_URL = "http://115.29.107.20/course/v1/";

    //服务器存放图片路径
    public static final String CACEH_IAMGE = "http://115.29.107.20/image/";

    //网络日志tag
    public static final String NET_LOG_TAG = "JCourse";
    //
    public static final String ACCOUNT = "Account";

    /**
     * Activity 之间跳转常量
     */
    public static final String JAVA_COURSE_DETAIL = "JavaCourseDetail";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final int REQUEST_IMAGE_CODE = 110;

    /**
     * EventBus
     */
    public static final String UPDATE_ACCOUNT_ON_DRAWER = "updateAccountInfoOnDrawer";
}
