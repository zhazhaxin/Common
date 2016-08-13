<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/6/14
 * Time: 18:53
 */

include 'connect_pdo.php';

$sql = "SELECT * FROM app ORDER BY time DESC LIMIT 1";
$result_sql = $pdo_connect->query($sql);
$result = $result_sql->fetch();

$item = array();
if ($result) {
    $item['id'] = $result['id'];
    $item['versionName'] = $result['versionName'];
    $item['versionCode'] = $result['versionCode'];
    $item['info'] = $result['info'];
    $item['url'] = $result['url'];
    $item['time'] = strtotime($result['time']);
}

echo json_encode($item);