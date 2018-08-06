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

<<<<<<< HEAD
$(".footer").offset({top:$(document).scrollTop() + $(window).height()-60})
$(window).scroll(function(){
    $(".footer").offset({top:$(document).scrollTop() + $(window).height()-60})     
})
=======
$("#btnID").click(function(){
    $.ajax({
        url: ip +'/user/duplicationCheck/' + $("#inputID").val(),
        contentType : 'application/json',
        type : "get",
        asycn: false,
        success : function(data){
            alert("사용 가능한 ID입니다.")
        },
        error : function(data) {
            alert("ID가 중복되었습니다.");
        }   
        
    })
})
>>>>>>> 27f7ccba8800f889640c355995196d16439d9f01
