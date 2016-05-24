<?php

require 'db_connect.php';

//get the json object
$json = file_get_contents('php://input');

//parse it
$jsondata = json_decode($json);

//assign to variables
$userID = $jsondata->user;
$gcm = $jsondata->gcmid;

//insert the message into a database
$sql = "UPDATE users SET gcm_token='$gcm' WHERE user_id='$userID';";

// preform
if (!mysqli_query($Thesisdb, $sql)) {
    printf("Errormessage: ", mysqli_error($Thesisdb));
}
mysqli_close($Thesisdb);

echo "Good";

?>