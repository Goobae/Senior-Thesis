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


while($row = $result->fetch_assoc() ){
       $response[] = $row;
}

$post_data = array();

if($rows == 1){
    $post_data = array('result'=>$response);
}else {
    $post_data = array('result' => false);
}

echo json_encode($post_data);

mysqli_close($Thesisdb);