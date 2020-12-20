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
$type=$_POST["type"];
//$tablename="login";
//$type="login";
//建立连接
$link = connectToDB();
//查看是否连接失败
if($link->connect_error){
    die($link->connect_error);
}
//构造SQL语句
$sql = "select * from $tablename";
//echo $sql;//显示查询语句
$res=$link->query($sql);
//构造成JSON语言格式
$data=array();
if($res){
//echo "查询成功";
    while ($row = mysqli_fetch_array($res,MYSQLI_NUM))
    {
        error_reporting(0);
        $result = returnClassBytype($type);
        for ($i=0;$i<$result->count;$i++){
            $result->setData($i+1,$row[strval($i)]);
        }
        $data[]=$result;
    }
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{".'"login"'.":".$json."}";
}else{
    echo "查询失败";
}
closeDB();
