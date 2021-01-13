<?php

/**
 *在数据库创建一个新的表
 * @param value      想要查询的值
 * @param tableName     数据库的表名及参数名   eg:table(id,name,age)
 * @param value      要传入的值   字符串需要打单引号          eg:1,'sxz',20
 * eg:dbBean.execQuery("nameandpassword(user_name,user_password,age)","'yzj','654321',10");
 * @return  0代表成功 1代表失败
 */
require_once __DIR__ . '/connect.php';//引用connect.php
require_once __DIR__ . '/header.php';//引用connect.php

//等待传入的参数
$tableName = $_POST["tableName"];
$value= $_POST["value"];
//$tableName = "sxz";
//$value = "order";

//建立连接
$link = connectToDB();
//查看是否连接失败
if ($link->connect_error) {
    die($link->connect_error);
}
switch ($value){
    case "order":
        $sql = "create table $tableName (id int NOT NULL AUTO_INCREMENT PRIMARY KEY,name varchar(100) NOT NULL UNIQUE KEY".
            ",state varchar(100) NOT NULL".
            ",orders varchar(100) NOT NULL".
            ",price_all float,num int NOT NULL".
            ",profit float NOT NULL)";
        break;
    case "repository":
        $sql = "create table $tableName (id int NOT NULL AUTO_INCREMENT PRIMARY KEY, ".
            "name varchar(100) NOT NULL UNIQUE KEY".
            ",outprice float NOT NULL".
            ",inprice float NOT NULL,".
            "num int NOT NULL,".
            "outprice_wholesale float NOT NULL)";
        break;
    case "item_order":
        $sql= "create table $tableName (order_id int NOT NULL".
            ",product_name varchar(100) NOT NULL".
            ",num int NOT NULL)";
        break;
}
//echo $sql;//显示插入语句
$res = $link->query($sql);
//构造成JSON语言格式
$data = array();
$result = returnClassBytype("create");
if ($res) {
    error_reporting(0);
    $result->setData(1, "0");
    $data[] = $result;
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{" . "create" . ":" . $json . "}";
} else {
    $result->setData(1, "1");
    $data[] = $result;
    $json = json_encode($data);//把数据转换为JSON数据.
    echo "{" . "create" . ":" . $json . "}";
}
closeDB();