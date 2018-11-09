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

function btn_modify() {  var txt = $("#contents").val();
  var tags = ["#통합"];
  var xxx = [];
  txt = txt.replace(/#[^#\s,;]+/gm, function(tag) {
   tags.push(tag);
   });
   console.log(tags);

    for (var i = 0 ; i < tags.length; i++){
        str = '{"name":"' + tags[i].substring(1,tags[i].length) + '"}';
        console.log(str);
        xxx[i] = JSON.parse(str);
    }
    console.log(xxx);

    $.ajax({
        headers : {"token":sessionStorage.getItem("token")},
        url: ip +'/article/',
        contentType : 'application/json; charset=UTF-8',
        type: 'PUT',
        async: false,
        data : JSON.stringify({
            title : $("#title").val(),
            text : $("#contents").val(),
            tags : xxx
         }),
         success : function(data){
            console.log(data.token);
            window.location.href = "main.html";

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
