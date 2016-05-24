<?php

require 'db_connect.php';

$json = file_get_contents('php://input');

$jsondata = json_decode($json);


$username = $jsondata->username;


$sql1 = " SELECT username
          FROM users
          WHERE username='$username';";

$result = mysqli_query($Thesisdb, $sql1) or die(mysqli_error($Thesisdb));

$rows = $result->num_rows;

$response;
while($row = $result->fetch_assoc() ){
       $response = $row;
}


if($rows != 0 ){
    $response = array('username'=>"false");
} else{
    $response = array('username'=>"true");
}

echo json_encode($response);

mysqli_close($Thesisdb);