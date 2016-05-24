<?php

require 'db_connect.php';

$json = file_get_contents('php://input');

$jsondata = json_decode($json);

$threadid =$jsondata->thread;

$sql1 = " SELECT users.name as name, messages.message as message, messages.created_at as date, users.user_id as userID
          FROM messages, users, threads
          WHERE threads.thread_id = messages.thread_id
          AND messages.user_id=users.user_id
          AND threads.thread_id ='$threadid'
          ORDER BY messages.created_at ASC;";
   

$result = mysqli_query($Thesisdb, $sql1) or die(mysqli_error($Thesisdb));

$response = array();

while($row = $result->fetch_assoc() ){
       $response[] = $row;
}

echo json_encode(array("serverResponse"=>$response));

mysqli_close($Thesisdb);

