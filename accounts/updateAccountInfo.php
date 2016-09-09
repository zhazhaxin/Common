<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/8/12
 * Time: 20:15
 */

include '../base/connect_pdo.php';
include '../base/check.php';
include '../base/token.php';
include '../base/header.php';
include '../base/statusCode.php';

$headers = getallheaders();
$id = get_UID($headers);
$token = get_token($headers);
check_token_past_due($token);  //检查token是否过期

$avatar = $_POST['avatar'];
$name = $_POST['name'];
$sign = $_POST['sign'];

check_empty($name,$sign,$avatar);
check_has_exist($pdo_connect,"user","name",$name,"用户名已存在");

$sql = "UPDATE user SET name='$name',sign='$sign',avatar='$avatar' WHERE id='$id'";
$update_result = $pdo_connect->exec($sql);

if($update_result > 0){
    $account_sql = "SELECT * FROM user WHERE id='$id' LIMIT 1";
    $query_result = $pdo_connect->query($account_sql);
    $row = $query_result->fetch();

    $result['id'] = $row['id'];
    $result['name'] = $row['name'];
    $result['avatar'] = $row['avatar'];
    $result['gender'] = $row['gender'];
    $result['sign'] = $row['sign'];
    $result['token'] = $row['token'];

    echo json_encode($result);
}else{
    echo serverError();
}

