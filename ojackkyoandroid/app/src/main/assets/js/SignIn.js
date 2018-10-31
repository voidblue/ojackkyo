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
            window.android.regSuccess("success");
            alert("회원가입 성공");
         },
         error : function(data) {
             console.log(data)
             alert("회원가입 실패");
         }

    })
}

$('#inputPWChk').keyup(function () {
    var inputPW = $('#inputPW').val();
    var inputPWChk = $('#inputPWChk').val();

    if (inputPW==inputPWChk) {
        $('#pwChk').html("PASSWARD가 일치합니다.");
    } else {
        $('#pwChk').html("PASSWARD가 일치하지 않습니다.");
    }

});

$("#btnID").click(function(){
    $.ajax({
        url: ip +'/user/uidCheck/' + $("#inputID").val(),
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

$("#btnNickname").click(function(){
    $.ajax({
        url: ip +'/user/nicknameCheck/' + $("#inputNickname").val(),
        contentType : 'application/json',
        type : "get",
        asycn: false,
        success : function(data){
            alert("사용 가능한 닉네임입니다.")
        },
        error : function(data) {
            alert("닉네임이 중복되었습니다.");
        }

    })
})
