<?php

require 'db_connect.php';

//get the json object
$json = file_get_contents('php://input');


//parse it
$jsondata = json_decode($json);

date_default_timezone_set('America/Chicago');

//assign to variables
$phone = 321;//$jsondata->phone;

//get the name associated with that number
$sql = "SELECT users.name as name, users.user_id as id FROM users WHERE users.phone_number = '$phone';";

$result = mysqli_query($Thesisdb, $sql) or die(mysqli_error($Thesisdb));

while($row = $result->fetch_assoc() ){
       $response = $row;
}

echo json_encode($response);

mysqli_close($Thesisdb);