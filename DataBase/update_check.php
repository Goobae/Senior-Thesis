<?php

require 'db_connect.php';

//get the json object
$json = file_get_contents('php://input');

//parse it
$jsondata = json_decode($json);

//assign to variables
$user = $jsondata->user;

//get the count
$sql = "select count(*) as count
        from usersthreads, messages
        where usersthreads.thread_id=messages.thread_id
        and usersthreads.user_id='$user'
;";

$result = mysqli_query($Thesisdb, $sql) or die(mysqli_error($Thesisdb));

$response;

while($row = $result->fetch_assoc() ){
       $response = $row;
}
echo json_encode($response);
