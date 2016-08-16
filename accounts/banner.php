<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/8/16
 * Time: 20:42
 */

include '../base/config.php';
include '../base/connect_pdo.php';
include '../base/check.php';
include '../base/statusCode.php';

$seedarray =microtime();
$seedstr =split(" ",$seedarray,5);
$seed =$seedstr[0]*1000000;

srand($seed);

$index = rand(1,5);
$sql = "select * from banner where id = '$index' LIMIT 1";
$query_result = $pdo_connect->query($sql);
$row = $query_result->fetch();

$result = array();
if(!empty($row)){
    $result['id'] = $row['id'];
    $result['imageUrl'] = $row['image_url'];
    $result['webUrl'] = $row['web_url'];
}
echo json_encode($result);