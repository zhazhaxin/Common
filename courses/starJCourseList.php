<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/8/14
 * Time: 13:41
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

$page = $_POST["page"];  //从零开始
$page_num = $_POST["pageNum"];

if ($page_num == 0) {
    $page_num = 10;
}
$start = $page_num * $page;
$end = $page_num * ($page + 1);

//取个人收藏的j_course_id
$sql = "SELECT j_course.* FROM user,j_course_relation,j_course WHERE user.id = '$uid' AND j_course_relation.user_id = '$uid'
AND j_course.id = j_course_relation.j_course_id LIMIT $start,$page_num";

$query_result = $pdo_connect->query($sql);
$rows = $query_result->fetchAll();

$result = array();
$index = 0;
foreach ($rows as $row) {
    $temp['id'] = intval($row['id']);
    $temp['subtitle'] = $row['subtitle'];
    $temp['cover'] = $row['cover'];
    $temp['title'] = $row['title'];
    $temp['content'] = $row['content'];

    $result[$index] = $temp;
    $index ++;
}
echo json_encode($result);