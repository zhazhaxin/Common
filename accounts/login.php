<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/6/1
 * Time: 10:40
 */

include '../base/connect_pdo.php';
include '../base/check.php';
include '../base/token.php';
include '../base/statusCode.php';

$name = $_POST['name'];
$password = $_POST['password'];

$login_sql = "select * from user where name = '$name' and password = '$password' LIMIT 1";

$query_result = $pdo_connect->query($login_sql);
if (empty($query_result)) {
    serverError();
}

if ($query_result->rowCount()) {

    $row = $query_result->fetch();

    //生成token
    $id = $row['id'];
    $token_str = $row['token'];
    $token = new Token();
    $token->set_user_token($token_str, $id);

    $result = array(
        'id' => intval($id),
        'name' => $row ['name'],
        'avatar' => $row ['avatar'],
        'gender' => intval($row ['gender']),
        'sign' => $row ['sign'],
        'token' => $token_str
    );
} else {
    header("http/1.1 400 params error");
    $result["error"] = "手机号码或密码错误";
}

echo json_encode($result);


