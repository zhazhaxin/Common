<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/8/17
 * Time: 13:38
 */

include '../base/check.php';
include '../base/connect_pdo.php';

$unit_index = $_POST['unit'];
$page = $_POST["page"];  //从零开始
$page_num = $_POST["pageNum"];

if ($page_num == 0) {
    $page_num = 10;
}
$start = $page_num * $page;
$end = $page_num * ($page + 1);

$units = array("第一章","第二章","第三章","第四章","第五章","第六章","第七章","第八章","第九章","第十章");

$title = $units[$unit_index - 1];

$sql = "SELECT * FROM j_course WHERE title = '$title' LIMIT $start,$page_num";
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