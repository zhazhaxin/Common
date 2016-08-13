# JCourse 数据库设计

##User

 - id
 - name
 - password
 - avatar 头像
 - gender  //0：男，1：女
 - sign

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
 
# API

##注册

参数
 - name
 - password

返回结果

 - info
 
##登录

参数
 - name
 - password
 
返回结果
 - name
 - avatar
 - token
 - gender
 - intro
 
##文本课程列表

参数
 - page
 - pageNum //默认20
 
返回结果
 - id
 - title
 - subtitle
 - cover
 - content