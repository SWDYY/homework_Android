<?php

/**
 * 更新数据库中的数据（如果值为字符串类型，需要加单引号‘’）
 * @param index_value   查询那一列的值
 * @param tableName     想要更新的表名
 * @param index         查询哪一列
 * @param target_value  想要更改后的值。如果值为字符串类型，需要加单引号‘’
 * @param target_index  想要修改表单的哪一列值
 * dbBean.executeUpdate(" ' wkr ' ", " nameandpassword ",
 *                 "user_name","654321","user_password");
 * @return  0代表成功 1代表失败
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
$tableName = $_POST["tableName"];
$target_index=$_POST["target_index"];
$target_value=$_POST["target_value"];
$index=$_POST["index"];
$index_value=$_POST["index_value"];

//$tableName = "repository1";
//$target_index="num";
//$target_value="2";
//$index="name";
//$index_value="'car'";

//建立连接
$link = connectToDB();
//查看是否连接失败
if ($link->connect_error) {
    die($link->connect_error);
}
//构造SQL语句
$sql="update $tableName set $target_index=$target_value where $index=$index_value";
//echo $sql;//显示插入语句
$res = $link->query($sql);
//构造成JSON语言格式
$data = array();
$result = returnClassBytype("update");
if ($res) {
    error_reporting(0);
    $result->setData(1, "0");
    $data[] = $result;
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{" . "update" . ":" . $json . "}";
} else {
    $result->setData(1, "1");
    $data[] = $result;
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{" . "update" . ":" . $json . "}";
}
closeDB();