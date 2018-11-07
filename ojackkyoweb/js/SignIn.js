function PWchk() {
  var patternNum = /[0-9]/; //숫자 포함
  var patternEng = /[a-zA-Z]/; //영문자 포함
  var password = $("#inputPW").val();
  var passwordchk = $("#inputPWChk").val();

  if( !patternNum.test(password) || !patternEng.test(password) || password.length < 6 || password.length > 10) {
    alert("비밀번호는 영문자, 숫자 포함 6자 이상 10자 이하로 설정해야 합니다.");
  }
  else if(password != passwordchk) {
    alert("비밀번호가 일치하지 않습니다.");
  }
  else {
    SignIn();
  }
}

function SignIn(){
  var array = [];
  var interest = $('.interest');
  var num = 0;

  for(var i = 0; i < interest.length; i++) {
    if(interest[i].checked == true) {
      array[num] = interest[i].value;
      num++;
      }
  }

  jQuery.ajaxSettings.traditional = true;

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
            studentCode: $("#inputStdID").val(),
            tags: array
         }),
         success : function(data){
            console.log(data.id);
            console.log(data);
            alert("회원가입 성공");
            window.location.replace("index.html");
            window.android.regSuccess("success");
         },
         error : function(data) {
             console.log(data);
             alert("회원가입 실패");
         }
    })
}

$('#inputPWChk').keyup(function () {
    var inputPW = $('#inputPW').val();
    var inputPWChk = $('#inputPWChk').val();

    if (inputPW==inputPWChk) {
        $('#condition').html("PASSWARD가 일치합니다.");
    } else {
        $('#condition').html("PASSWARD가 일치하지 않습니다.");
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
});

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
});
