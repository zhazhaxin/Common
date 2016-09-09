<?php
/**
 * Created by PhpStorm.
 * User: linlongxin
 * Date: 2016/6/2
 * Time: 17:52
 */

$dsn = "mysql:dbname=java_course;host=localhost";;
$user = "Lemon";
$password = "Lemon";
$opt = array(PDO::ATTR_PERSISTENT => true); //持久连接

try{
    $pdo_connect = new PDO($dsn, $user, $password,$opt);
    $pdo_connect->query("set names utf8");
}catch (PDOException $e){
    echo  '数据库连接失败 : '.$e->getMessage();
}