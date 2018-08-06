function SignIn(){
    $.ajax({
        url: ip +'/user',
        contentType : 'application/json',
        type : "post",
        asycn: false,
        data : JSON.stringify({
            uid : $("#inputID").val(),
            password : $("#inputPW").val(),
            name : $("#inputName").val(),
            nickname : $("#inputNickname").val(),
            callNumber : $("#inputCal").val(),
            email: $("#inputEmail").val(),
            studentCode: $("#inputStdID").val()
         }),
         success : function(data){
            console.log(data.id);
            window.location.replace("front.html");
            alert("회원가입 성공");
         },
         error : function(data) {
             console.log(data)
             alert("회원가입 실패");
         }   
        
    })
}