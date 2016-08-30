<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/6/3
 * Time: 19:15
 * @param $headers
 * @return mixed
 */

function get_UID($headers)
{
    if ($headers['UID'] != "") {
        return $headers['UID'];
    } else {
        header("http/1.1 401 UID is empty");
        $result['error'] = "UID为空，请先登录";
        echo json_encode($result);
        exit();
    }

}

function get_token($headers)
{
    if ($headers['token'] != "") {
        return $headers['token'];
    } else {
        header("http/1.1 400 token is empty");
        $result['error'] = "token为空，请先登录";
        echo json_encode($result);
        exit();
    }

}