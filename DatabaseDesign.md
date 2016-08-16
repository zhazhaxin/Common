# JCourse 数据库设计

##User

 - id
 - name
 - password
 - avatar 头像
 - gender
 - intro

##j_course --- java课程表

 - id
 - cover //封面
 - title  //标题
 - subtitle  //副标题
 - content

##a_course --- android课程表

 - id
 - name
 - cover //封面
 - title  //标题
 - content

##j_course_relation --- java课程关系表

 - id
 - user_id
 - j_course_id

##a_course_relation --- Android课程关系表

 - id
 - user_id
 - a_course_id