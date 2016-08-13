<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/8/6
 * Time: 22:49
 */

include '../base/check.php';
include '../base/connect_pdo.php';

$page = $_POST["page"];  //从零开始
$page_num = $_POST["pageNum"];

if ($page_num == 0) {
    $page_num = 20;
}
$start = $page_num * $page;
$end = $page_num * ($page + 1);

$sql = "SELECT * FROM j_course LIMIT $start,$page_num";
$query_result = $pdo_connect->query($sql);
$rows = $query_result->fetchAll();

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