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
//$userID = 2;
//$message = "hey";
//$threadID = 2;
$created = date('Y-m-d H:i:s');


//insert the message into a database
$sql = "INSERT INTO `messages` (`user_id`, `thread_id`, `message`, `created_at`) VALUES ('$userID', '$threadID', '$message', '$created');";

// preform
if (!mysqli_query($Thesisdb, $sql)) {
    echo json_encode("Errormessage: %s\n", mysqli_error($Thesisdb));
}

$sql3 = "UPDATE threads SET message='$message', created_at='$created' WHERE thread_id='$threadID'";

mysqli_query($Thesisdb, $sql3) or die(mysqli_error($Thesisdb));

$query = "SELECT users.name as name FROM users WHERE users.user_id = '$userID';";

$result = mysqli_query($Thesisdb, $query) or die(mysqli_error($Thesisdb));

$response;


while($row = $result->fetch_assoc() ){
       $response = $row;
}
echo json_encode($response);

$registrationIds=array();
$sql1=" SELECT users.user_id , users.gcm_token
        FROM users, messages,threads
        WHERE threads.thread_id=$threadID
        AND threads.thread_id=messages.thread_id
        AND messages.user_id=users.user_id
        AND users.user_id != $userID
        AND users.gcm_token != ''
        GROUP BY users.user_id
             
      
;";

$fire=mysqli_query($Thesisdb,$sql1);


$users=array();
if( (isset($fire))&&(!empty($fire)) ){
    while($result=mysqli_fetch_assoc($fire)){
        array_push($users,$result);
    }
}

//var_dump($users);

foreach($users as $single_user){
    $register_id=$single_user['gcm_token'];
    array_push($registrationIds,$register_id);
}


$msg = array
(
    'message'      => $threadID,
    'vibrate'   => 1,
    'sound'     => 1,
);

$fields = array
(
    'registration_ids'  => $registrationIds,
    'data'              => $msg
);

$headers = array
(
    'Authorization: key=' . GOOGLE_API_KEY,
    'Content-Type: application/json'
);

$ch = curl_init();
curl_setopt( $ch,CURLOPT_URL, 'https://android.googleapis.com/gcm/send' );
curl_setopt( $ch,CURLOPT_POST, true );
curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields,true ) );
$result1 = curl_exec($ch );
curl_close( $ch );

mysqli_close($Thesisdb);

?>