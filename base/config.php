<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/6/6
 * Time: 11:08
 */

class Config{
    
    //用户默认头像
    public static $DEFAULT_AVATAR = "http://img03.sogoucdn.com/app/a/100520093/dbc006c92ca7e754-8079a6fac653bad5-1f51ab8e53b21b7911e9aeca0d6505cb.jpg";

    //服务器图片目录
    public static $SERVICE_CACHE_IMAGE_DIR = '/var/www/html/image/';

    //用户默认
    public static $DEFAULT_SIGN = "more code,keep heart";

    //mob
    public static $MOB_APP_KEY = "138d55e115e47";
    public static $MOB_APP_SECRET = "6b4f19b7294db77ca853d9bfbc181f96";
    public static $API_MOB_VERIFY_CODE = "https://webapi.sms.mob.com/sms/verify";

    //七牛
    public static $QINIU_ACCESS_KEY = "UOUxbo4brbNKkEEkTEZbnkPXrKaq_KoqCxhlo2oe";
    public static $QINIU_SECRET_KEY = "55JlULJtMpRpt-LJuid3gK_7I9CJOVDyhT_-k6sO";
}