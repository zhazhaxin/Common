package cn.lemon.jcourse.model.bean;

import java.util.List;

/**
 * Created by linlongxin on 2017.1.22.
 */

public class Exercise {

    public int id;
    public int courseId;
    public String title;
    public int isMultipleChoice;
    public String contentList;
    public String answer;
    public List<Integer> choice;
}
