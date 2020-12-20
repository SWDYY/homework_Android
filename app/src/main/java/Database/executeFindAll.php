<?php
/**
 *查询数据库中全部数据
 * @param value      想要查询的值
 * @param tableName     想要查询的数据库的表名
 * @param index      想要查询数据库的表的哪一列的名字
 * @return   返回查询到的所有行
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
$tablename=$_POST["tableName"];
//$tablename="repository1";


$link = connectToDB();

if($link->connect_error){
    die($link->connect_error);
}

$sql = "select * from $tablename";
//echo $sql;//显示查询语句
$res=$link->query($sql);

$data=array();
if($res){
//echo "查询成功";
    while ($row = mysqli_fetch_array($res,MYSQLI_NUM))
    {
        $login = new repository();
        error_reporting(0);
        $login->id = $row["0"];
        $login->name = $row["1"];
        $login->outprice = $row["2"];
        $data[]=$login;
    }
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{".'"login"'.":".$json."}";
}else{
    echo "查询失败";
}
closeDB();
