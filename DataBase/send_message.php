<?php

require 'db_connect.php';

//get the json object
$json = file_get_contents('php://input');


//parse it
$jsondata = json_decode($json);

date_default_timezone_set('America/Chicago');

//assign to variables
$userID = $jsondata->user;
$message = $jsondata->message;
$threadID = $jsondata->thread;
$created = date('Y-m-d H:i:s');

//insert the message into a database
$sql = "INSERT INTO `messages` (`user_id`, `thread_id`, `message`, `created_at`) VALUES ('$userID', '$threadID', '$message', '$created');";

// preform
if (!mysqli_query($Thesisdb, $sql)) {
    echo json_encode("Errormessage: %s\n", mysqli_error($Thesisdb));
}

$sql1 = "UPDATE threads SET message='$message', created_at='$created' WHERE thread_id='$threadID'";

mysqli_query($Thesisdb, $sql1) or die(mysqli_error($Thesisdb));

mysqli_close($Thesisdb);

?>