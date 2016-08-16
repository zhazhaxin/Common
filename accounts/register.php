<?php

include '../base/connect_pdo.php';
include '../base/check.php';
include '../base/request.php';
include '../base/config.php';
include '../base/token.php';
include '../base/statusCode.php';

// 配置项

$name = $_REQUEST['name'];
$password = $_REQUEST['password'];
//$code = $_REQUEST['code'];
//$number = $_REQUEST['number'];
$avatar = $_REQUEST['avatar'];

check_empty($name, $password);
check_has_exist($pdo_connect, "user","name",$name,"用户名已存在");

//生成token
$token = new Token();
$real_token_str = $token->get_token($name, "pick_picture");

//默认签名，头像
$sign = "more code，keep heart";
$avatar = Config::$DEFAULT_AVATAR;

$insert_sql = "insert into user (name,password,avatar,token,sign) 
values('$name','$password','$avatar','$real_token_str','$sign')";
$insert_user = $pdo_connect->exec($insert_sql);

if ($insert_user) {
    $result['info'] = "注册成功";
} else {
    serverError();
}



//$response = postRequest(Config::$API_MOB_VERIFY_CODE, array(
//    'appkey' => Config::$MOB_APP_KEY,
//    'phone' => $number,
//    'zone' => '86',
//    'code' => $code,
//));
//
//$response_json = json_decode($response, true);
//
//if ($response_json['status'] == 200) {
//
//    $token = new Token();
//    $real_token_str = $token->get_token($name, "pick_picture"); //生成token
//
//    $insert_sql = "insert into user (name,password,avatar,token)
//values('$name','$password','$avatar','$real_token_str')";
//    $insert_user = $pdo_connect->exec($insert_sql);
//
//    if ($insert_user) {
//        $result['info'] = "success";
//    } else {
//        serverError();
//    }
//} else if ($response_json['status'] == 468) {
//    header("http/1.1 400 Bad Request");
//    $result["error"] = "验证码错误";
//} else {
//    header("http/1.1 400 Bad Request");
//    $result["error"] = "验证平台" . $response;
//}

echo json_encode($result);
