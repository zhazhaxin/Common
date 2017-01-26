package cn.lemon.jcourse.config;

/**
 * Created by linlongxin on 2016/8/7.
 */

public class Config {

    //服务器
    public static final String BASE_URL = "http://139.199.19.141/course/v1/";

    //服务器存放图片路径
    public static final String CACEH_IAMGE = "http://139.199.19.141/image/";

    //网络日志tag
    public static final String NET_LOG_TAG = "JCourse";

    public static final String ACCOUNT = "Account";

    /**
     * Activity 之间跳转常量
     */
    public static final int REQUEST_IMAGE_CODE = 123;
    public static final int REQUEST_PUBLISH_BBS = 321;
    public static final int RESULT_PUBLISH_BBS = 132;

    public static final String JAVA_COURSE_DETAIL = "JavaCourseDetail";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String JAVA_COURSE_UNIT = "JavaCourseUnitNumber";
    public static final String BBS_DETAIL_ID = "BBS_detail_id";
    public static final String USER_BBS_LIST = "user_bbs_list";
    public static final String WEB_VIEW_BANNER = "web_view_banner";
    public static final String EXERCISE_ID = "exercise_id";

    /**
     * EventBus
     */
    //更新圈子的状态
    public static final String CHECK_STATUS_FOR_GROUP_FRAGMENT = "check_status";


    public static class Color {
        public static int RED = 0Xf42930;
        public static int BLUE = 0Xff5677fc;
        public static int WHITE = 0xffffffff;
    }
}
