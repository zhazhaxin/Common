package cn.lemon.jcourse.model.bean;

/**
 * Created by linlongxin on 2016/9/14.
 */

public class BBS {
    //发布问题的相关信息
    public String avatar;
    public String name;
    public String sign;
    public String title;
    public String content;
    public String pictures;
    public Comment[] comments;

    class Comment {
        public Commenter commenter;
        public Objecter object;
    }

    //评论者
    class Commenter {
        public String avator;
        public String name;
        public String sign;
        public String content;
    }

    //回复对象
    class Objecter {
        public String avator;
        public String name;
        public String sign;
    }
}
