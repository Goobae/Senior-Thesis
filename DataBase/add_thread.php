
<?php

require 'db_connect.php';

//get the json object
$json = file_get_contents('php://input');


//parse it
$jsondata = json_decode($json);

date_default_timezone_set('America/Chicago');

//assign to variables

$userID1=$jsondata->user1;
$userID2=$jsondata->user2;
$message = $jsondata-> message;

$created = date('Y-m-d H:i:s');

//get the name associated with that number
$sql = "INSERT INTO threads (created_at, message) VALUES ('$created', 'NULL');";

$result = mysqli_query($Thesisdb, $sql) or die(mysqli_error($Thesisdb));

$threadID = mysqli_insert_id($Thesisdb);

echo json_encode(array("id"=>$threadID));

$sql2 = "INSERT INTO usersthreads (user_id, thread_id) VALUES ('$userID1','$threadID')";
$result2 = mysqli_query($Thesisdb, $sql2) or die(mysqli_error($Thesisdb));
$sql3 = "INSERT INTO usersthreads (user_id, thread_id) VALUES ('$userID2','$threadID')";
$result3 = mysqli_query($Thesisdb, $sql3) or die(mysqli_error($Thesisdb));

$sql4 = "INSERT INTO messages (user_id, thread_id, message) VALUES ('$userID1','$threadID', 'message')";
$result4 = mysqli_query($Thesisdb, $sql4) or die(mysqli_error($Thesisdb));


mysqli_close($Thesisdb);