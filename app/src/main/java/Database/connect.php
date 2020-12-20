<?php

/**
 * A class file to connect to database
 */
/**
 * Function to connect with database
 */
function connectToDB() {

    /*包含并运行指定的文件*/
    // import database connection variables
    require_once __DIR__ . '/db_config.php';

    global $con;
    // Connecting to mysql database
    $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD,DB_DATABASE) or die(mysqli_connect_error());

    // returing connection cursor
    return $con;
}

/**
 * Function to close db connection
 */
function closeDB() {
    // closing db connection
    global $con;
    mysqli_close($con);
}
?>