<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/6/3
 * Time: 16:01
 */

/**
 * 检查参数是否为空
 */

function check_empty()
{
    $args = func_get_args(); //获取函数参数

    for ($i = 0; $i < count($args); $i++) {
        if ($args[$i] == "") {
            header("http/1.1 400 params error");
            $result["error"] = "参数不能为空";
            echo json_encode($result);
            exit();
        }
    }
}

function check_not_exist($pdo_connect, $table, $field, $field_value, $errorInfo)
{
    $check_sql = "SELECT * FROM $table WHERE $field = '$field_value'";
    $check_result = $pdo_connect->query($check_sql);
    if ($check_result->rowCount() == 0) {
        paramErrorWithInfo($errorInfo);
    }
}

function check_has_exist($pdo_connect, $table, $field, $field_value, $errorInfo)
{
    $check_sql = "SELECT * FROM $table WHERE $field = '$field_value' LIMIT 1";
    $check_result = $pdo_connect->query($check_sql);
    if ($check_result->rowCount() > 0) {
        paramErrorWithInfo($errorInfo);
    }
}

function check_token_past_due($token)
{
    $memcache = new Token();
    $UID = $memcache->get_user_uid($token);
    if (empty($UID)) {
        tokenInvalid();
    } else {
        return;
    }

}