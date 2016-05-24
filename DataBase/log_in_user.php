<?php

require 'db_connect.php';

$json = file_get_contents('php://input');

$jsondata = json_decode($json);


$email = $jsondata->email;
$password = $jsondata->password;

$sql1 = " SELECT user_id as userid
          FROM users
          WHERE email = '$email' AND password = '$password';";

$result = mysqli_query($Thesisdb, $sql1) or die(mysqli_error($Thesisdb));

$rows = $result->num_rows;

$response;
while($row = $result->fetch_assoc() ){
       $response = $row;
}


if($rows != 1 ){
    $response = array('userid'=>"false");
}

echo json_encode($response);

mysqli_close($Thesisdb);