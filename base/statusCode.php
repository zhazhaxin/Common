<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/6/6
 * Time: 13:16
 */

/**
 * 参数错误
 */
function badParam()
{
    header("http/1.1 400 params error");
    $result["error"] = "params error";
    echo json_encode($result);
    exit();
}

function paramErrorWithInfo($errorInfo)
{
    header("http/1.1 400 params error");
    $result["error"] = $errorInfo;
    echo json_encode($result);
    exit();
}

function tokenInvalid()
{
    header("http/1.1 401 token invalid");
    $result["error"] = "token失效，请重新登录";
    echo json_encode($result);
    exit();
}

function permissionError()
{
    header("http/1.1 403 permission error");
    $result["error"] = "permission error";
    echo json_encode($result);
    exit();
}

function serverError()
{
    header("http/1.1 500 server error");
    $result["error"] = "server error";
    echo json_encode($result);
    exit();
}