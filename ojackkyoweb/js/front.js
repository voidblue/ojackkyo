function btnLogin(){
    $.ajax({
        headers : {"token":sessionStorage.getItem("token")},
        url: ip +'/auth/login',
        contentType : 'application/json; charset=UTF-8',
        type: 'POST',
        async: false,
        data : JSON.stringify({
            uid : $("#userID").val(),
            password : $("#userPW").val()
         }),
         success : function(data){
             console.log(data);
            console.log(data.token);
            sessionStorage.setItem("token", data.token);
            window.location.replace("main.html");
         },
         error : function(data) {
            alert("ID,PW를 확인하세요");
         }
    })
}

function btnFind(){
    alert("기능구현 중입니다.");
    window.location.href("#");
}

function btnSignIn(){
    window.location.href="SignIn.html";
}

function enterkey() {
  if(window.event.keyCode == 13) {
    btnLogin();
  }
}
