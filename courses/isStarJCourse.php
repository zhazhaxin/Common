<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/8/14
 * Time: 12:11
 */

include '../base/connect_pdo.php';
include '../base/token.php';
include '../base/check.php';
include '../base/header.php';
include '../base/config.php';
include '../base/statusCode.php';

$headers = getallheaders();
$uid = get_UID($headers);
$token = get_token($headers);

check_token_past_due($token);

$id = $_POST['id'];

$sql = "SELECT * FROM j_course_relation WHERE user_id = '$uid' AND j_course_id = '$id' LIMIT 1";
$query_result = $pdo_connect->query($sql);
if($query_result->rowCount() > 0){
    $result['star'] = true;
}else{
    $result['star'] = false;
}
echo json_encode($result);