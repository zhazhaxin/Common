package cn.lemon.jcourse.model.bean;

/**
 * Created by linlongxin on 2016/9/14.
 */

public class BBS {
    //发布问题的相关信息
    public int id;
    public String avatar;
    public String name;
    public String sign;
    public String title;
    public String content;
    public String pictures;
    public Comment[] comments;

    public static class Comment {
        public String content;
        public String name;
        public String sign;
        public String avatar;
        public int objectId;
        public String objectName;
    }


}
