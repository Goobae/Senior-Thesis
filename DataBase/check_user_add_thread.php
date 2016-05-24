<?php

require 'db_connect.php';

$json = file_get_contents('php://input');

$jsondata = json_decode($json);


$username =$jsondata->username;


$sql1 = " SELECT username, user_id as userid
          FROM users
          WHERE username='$username';";

$result = mysqli_query($Thesisdb, $sql1) or die(mysqli_error($Thesisdb));

$rows = $result->num_rows;

$response;
while($row = $result->fetch_assoc() ){
       $response = $row;
}


if($rows != 1 ){
    $response = array('username'=>"false", 'userid'=>0);
} else{
    $response;
}

echo json_encode($response);

mysqli_close($Thesisdb);