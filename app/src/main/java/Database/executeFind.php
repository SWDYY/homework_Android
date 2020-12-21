<?php
/**
 *查询数据库中数据
 * @param value      想要查询的值
 * @param tableName     想要查询的数据库的表名
 * @param index      想要查询数据库的表的哪一列的名字
 * @return   返回查询到的所有行
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
$tablename=$_POST["tableName"];
$index=$_POST["index"];
$value=$_POST["value"];
$type=$_POST["type"];

//$tablename="customermanager";
//$index="name";
//$value="'sxz'";
//$type="customer";

$link = connectToDB();

if($link->connect_error){
    die($link->connect_error);
}

$sql = "select * from $tablename where $index=$value";
//echo $sql;//显示查询语句
$res=$link->query($sql);
//构造成JSON语言格式
$data=array();
if($res){
//echo "查询成功";
    while ($row = mysqli_fetch_assoc($res))
    {
        $result = returnClassBytype($type);
        error_reporting(0);
        for ($i=0;$i<$result->count;$i++){
            $result->setData($i+1,$row[$result->getName($i+1)]);
        }
        $data[]=$result;
    }
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{".$type.":".$json."}";
}else{
    echo "查询失败";
}
closeDB();
