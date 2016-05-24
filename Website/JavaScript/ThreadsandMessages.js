/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload(display());

SAVETHREAD;
MESSAGECOUNT;


function display(){
    MESSAGECOUNT=0;
    SAVETHREAD=0;
    displayThreads();
    setInterval(function(){
        update();
},2000);
}


var threadID;
//Logged in user
var userID;

function displayMessages(thread){
    
        SAVETHREAD = thread;
        $('#messages').empty();

        
        var src = window.location.search;
        if (src.indexOf("?src=") === 0) {
            src = src.substring(5);
        }
               
        userID=src;
        threadID=thread;
        

        var jsonObject = {"thread":threadID};
        var finalObject = JSON.stringify(jsonObject);
    $.ajax({
        type: 'POST',
        url: 'http://75.86.150.212:8080/Thesis/AMessage/Database/get_messages.php',
        data: finalObject,
        dataType: 'json',
        success: function(data)
        {
            var messages = data['serverResponse'];


           $.each( messages, function( name, value) {
               var message =
                    "<li class='message' role='listitem'>" +
                        "<div>" + value.name + "</div>" +
                        "<div>" + value.message + "</div>" +
                        "<div>" + jdateformat(value.date) + "</div>" +
                    "</li>";
               $("#messages").append($(message).addClass(value.userID === userID ? "sentMes" : ""));              
         }); 
         updateScroll();
        },
        error: function(ts){
            alert(JSON.stringify(ts));
        }
        
    });
}

//DONE
function displayThreads(){
    $('#threads').empty();
    
    var src = window.location.search;
    if (src.indexOf("?src=") === 0) {
        src = src.substring(5);
    }
    
        var jsonObject = {"userID":src};
        var finalObject = JSON.stringify(jsonObject);
    $.ajax({
        type: 'POST',
        url: 'http://75.86.150.212:8080/Thesis/AMessage/Database/get_thread.php',
        data: finalObject,
        dataType: 'json',
        success: function(data)
        {
            var threads = data['threads'];
 
           $.each( threads, function( name, value) {
               var thread =
                    "<li onclick='threadClicked("+value.thread+")' class='message' role='listitem'>" +
                        "<div>" + value.name + "</div>" +
                        "<div>" + value.message + "</div>" +
                        "<div>" + jdateformat(value.date) + "</div>" +
                        "<div  style='color:#FFFFFF'>" + value.thread +"</div>" +
                    "</li>";
               $("#threads").append($(thread).addClass("thread"));              
         }); 
         
        },
        error: function(ts){
            alert(JSON.stringify(ts));
        }
        
    });
}

function updateScroll(){
   
$('#messages').scrollTop($('#messages')[0].scrollHeight);
}

function threadClicked(thread) {
    displayMessages(thread);
}

function sendMessage(message){

    if(threadID !== undefined){
    if(message !== "")
    {
      
    var jsonObject = {"user":userID, "thread":threadID, "message":message};
    var finalObject = JSON.stringify(jsonObject);
    
    $.ajax({
        type: 'POST',
        url: 'http://75.86.150.212:8080/Thesis/AMessage/Database/send_message_web.php',
        data: finalObject,
        dataType: 'json',
        success: function(data)
        {
           var name = data.name;
           

               var messagec =
                    "<li class='message' role='listitem'>" +
                        "<div>" + name + "</div>" +
                        "<div>" + message + "</div>" +
                        "<div>" + formatDate(new Date()) + "</div>" +
                    "</li>";
               $("#messages").append($(messagec).addClass("sentMes"));  
           
           document.getElementById("messagetosend").value="";
         updateScroll();
         displayThreads();
        },
        error: function(ts){
            alert(JSON.stringify(ts));
        }
        
    });
    }
}
}

function update(){
    var src = window.location.search;
    if (src.indexOf("?src=") === 0) {
        src = src.substring(5);
    }
    
        var jsonObject = {"user":src};
        var finalObject = JSON.stringify(jsonObject);
    $.ajax({
        type: 'POST',
        url: 'http://75.86.150.212:8080/Thesis/AMessage/Database/update_check.php',
        data: finalObject,
        dataType: 'json',
        success: function(data)
        {
            var serverCount = data.count;
            if(serverCount>MESSAGECOUNT)
            {
                displayThreads();
                displayMessages(SAVETHREAD);
                MESSAGECOUNT=serverCount;
            }
            
         
         
        },
        error: function(ts){
            alert(JSON.stringify(ts));
        }
        
    });
}


function jdateformat(date){
    var t = date.split(/[- :]/);
    var d = new Date(t[0], t[1]-1, t[2], t[3], t[4], t[5]);
    return formatDate(d);
}

function formatDate(date) {

  var hours = date.getHours();
  var minutes = date.getMinutes();
  var ampm = hours >= 12 ? 'pm' : 'am';
  hours = hours % 12;
  hours = hours ? hours : 12; // the hour '0' should be '12'
  minutes = minutes < 10 ? '0'+minutes : minutes;
  var strTime = hours + ':' + minutes + ' ' + ampm;
  return date.getMonth()+1 + "/" + date.getDate() + "/" + date.getFullYear() + "  " + strTime;
}

