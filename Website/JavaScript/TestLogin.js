/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//make sure the login is correct
function testLogin(email, password){
    //make sure the boxes arent empty
    if(email !== "" && password !=="")
    {
        //create a json object, and 
        var jsonObject = {"email":email, "password":password};
        var finalObject = JSON.stringify(jsonObject);
        

        //sending json object
        
            $.ajax({
                type: 'POST',
                url: 'WebPHP/check_login.php',
                contentType: "application/json; charset=utf-8",
                data: finalObject,
                async:false,
                dataType: 'json',
                success: function(data)
                {
                   if(data["result"] === false){
                     alert("Invalid Email or Password");
                     
                        
                } else{
                    var userID = data["result"][0].userid;
                    var url = "AMessage.html";
                    window.location = url+"?src="+userID;
                }
                }
            });
return false;  
    }     
}