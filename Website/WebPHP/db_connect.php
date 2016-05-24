<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

require 'config.php';



$mysql_host = DB_HOST;
$mysql_user = DB_USERNAME;
$mysql_password = DB_PASSWORD;
$mysql_name = DB_NAME;

$Thesisdb = new mysqli($mysql_host, $mysql_user, $mysql_password, $mysql_name);

if(!$Thesisdb->connect_errno == 0){

    echo $Thesisdb->connect_err();
}

?>