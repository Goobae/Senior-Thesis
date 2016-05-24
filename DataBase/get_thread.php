<?php
require 'db_connect.php';

$json = file_get_contents('php://input');

$jsondata = json_decode($json);

$userID = $jsondata->userID;
//$userID =1;

/*          select t.thread_id as thread, t.name as name, messages.message as message, max(messages.created_at) as date
            from (SELECT threads.thread_id, threads.name
                  FROM threads,messages
                  WHERE threads.thread_id=messages.thread_id
                  AND messages.user_id=$userID
                  GROUP BY messages.thread_id) as t
            inner join messages
            on t.thread_id = messages.thread_id
            group by t.thread_id
            order by date DESC
 * 
 * select t.thread_id as thread, t.name as name, messages.message as message, max(messages.created_at) as date
from (SELECT threads.thread_id, nt.name
      FROM threads,messages,
            (SELECT users.name as name, usersthreads.thread_id as threadID
            from users,usersthreads
            where users.user_id=usersthreads.user_id
            and users.user_id!='$userID') as nt
      WHERE threads.thread_id=messages.thread_id
      and nt.threadID=threads.thread_id
      AND messages.user_id=$userID
      GROUP BY messages.thread_id) as t
inner join messages
on t.thread_id = messages.thread_id 
group by t.thread_id
order by date DESC

 * 
Select t1.thread_id as thread, t1.name as name
from
(select usersthreads.thread_id, users.name from users,
(select thread_id as thread, user_id
from usersthreads
where user_id='$userID') as t1
inner join usersthreads
on t1.thread=usersthreads.thread_id
where usersthreads.user_id!='$userID'
and usersthreads.user_id=users.user_id) as t1
 */

$sql1 = "
select n.name as name, n.thread_id as thread, threads.message as message, threads.created_at as date
from
        (select users.name, tn.thread_id
        from
                (select usersthreads.user_id, usersthreads.thread_id
                from
                        (select thread_id
                        from usersthreads
                        where user_id='$userID') thread
                    inner join usersthreads
                        on thread.thread_id = usersthreads.thread_id
                where usersthreads.user_id!='$userID') tn
            inner join users
                on tn.user_id = users.user_id) n
     inner join threads
        on n.thread_id=threads.thread_id
order by threads.created_at desc


            

        ;";

$result = mysqli_query($Thesisdb, $sql1) or die(mysqli_error($Thesisdb));

$response = array();

while($row = $result->fetch_assoc() ){
       $response[] = $row;
}
//var_dump($response);
echo json_encode(array("threads"=>$response));

mysqli_close($Thesisdb);


