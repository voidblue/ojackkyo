$.ajax({
    url: ip +'/article/' + getParameters("id"),
    contentType: 'application/json',
    type: 'GET',
    async: false,
    success: function(data){
      console.log(data);
      console.log(data.text);
      console.log(data.title);
      document.getElementById("title").value = data.title;
      console.log(data.text.includes("\n"))
      text = data.text.replace(/\n/g,"<br />")
      console.log(text);
      $("#contents").html(text);
     },
     error: function(data) {
       alert("Error!!");
     }
})

function btn_modify() {
  sharp = $("#contents").val().split("#");
  xxx = [];
  i = 0;
  console.log(sharp);
  for (var i = 1 ; i < sharp.length; i++){
      xxx[i-1] = sharp[i].split(" ")[0];
      console.log(xxx);
  }

  for (var i = 0 ; i < xxx.length; i++){
      str = '{"name":"' + sharp[i+1].split(" ")[0] + '"}';
      console.log(str);
      xxx[i] = JSON.parse(str);
  }
  console.log(xxx);

  $.ajax({
      headers : {"token":sessionStorage.getItem("token")},
      url: ip +'/article',
      contentType : 'application/json; charset=UTF-8',
      type: 'PUT',
      async: false,
      data : JSON.stringify({
          title : $("#title").val(),
          text : $("#contents").val(),
          id : getParameters("id"),
          tags : xxx
       }),
       success : function(data){
          console.log(data.token);
          window.location.href = "content?id=" + getParameters("id");

       },
       error : function(data) {
          console.log(data);
       }
  })
}

function getParameters (paramName) {
    // 리턴값을 위한 변수 선언
    var returnValue;

    // 현재 URL 가져오기
    var url = location.href;

    // get 파라미터 값을 가져올 수 있는 ? 를 기점으로 slice 한 후 split 으로 나눔
    var parameters = (url.slice(url.indexOf('?') + 1, url.length)).split('&');

    // 나누어진 값의 비교를 통해 paramName 으로 요청된 데이터의 값만 return
    for (var i = 0; i < parameters.length; i++) {
        var varName = parameters[i].split('=')[0];
        if (varName.toUpperCase() == paramName.toUpperCase()) {
            returnValue = parameters[i].split('=')[1];
            return decodeURIComponent(returnValue);
        }
    }
}
