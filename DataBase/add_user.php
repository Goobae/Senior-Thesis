
<?php

require 'db_connect.php';

//get the json object
$json = file_get_contents('php://input');


//parse it
$jsondata = json_decode($json);

date_default_timezone_set('America/Chicago');

//assign to variables

$email = $jsondata->email;
$name = $jsondata->name;
$password = $jsondata->password;
$phone = $jsondata->phone;
$username = $jsondata->username;


//get the name associated with that number
$sql = "INSERT INTO users (name, password, email, phone_number, username) VALUES ('$name','$password', '$email','$phone', '$username');";

$result = mysqli_query($Thesisdb, $sql) or die(mysqli_error($Thesisdb));


$sql1 = "SELECT LAST_INSERT_ID() as id FROM users;";

$id = mysqli_query($Thesisdb, $sql1) or die(mysqli_error($Thesisdb));

$response;

while($row = $id->fetch_assoc() ){
       $response = $row;
}

echo json_encode($response);

mysqli_close($Thesisdb);