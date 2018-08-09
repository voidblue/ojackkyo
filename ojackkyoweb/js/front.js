function btnLogin(){
    $.ajax({
        headers : {"token":sessionStorage.getItem("token")},
        url: ip +'/auth/login',
        contentType : 'application/json',
        type: 'POST',
        async: false,
        data : JSON.stringify({
            uid : $("#userID").val(),
            password : $("#userPW").val()
         }),
         success : function(data){
            console.log(data.token);
            sessionStorage.setItem("token", data.token);
            window.location.replace("board.html"); 
         },
         error : function(data) {
             alert("ID,PW를 확인하세요");
         }   
    })   
}

function btnFind(){
    window.location.href("#");
}

function btnSignIn(){
    window.location.href="SignInSelect.html";
}