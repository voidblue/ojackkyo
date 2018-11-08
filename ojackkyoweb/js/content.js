var id_index = getParameters("id");

console.log("id index " + id_index);

$.ajax({
    url: ip +'/article/' + getParameters("id"),
    contentType: 'application/json',
    type: 'GET',
    async: false,
    success: function(data){
      console.log(data);
      console.log(data.text);
      $("#title").html(data.title);
      console.log(data.text.includes("\n"))
      text = data.text.replace(/\n/g,"<br />")
      console.log(text);
      $("#contents").html(text);
      var str = data.authorsNickname + " | " + data.timeCreated.split(".")[0] + " | 조회수 " + data.viewed;
      $("#details").html(str);
      comments_refresh();
     },
     error: function(data) {
       alert("Error!!");
     }
})

function writeCm(data) {
  var detail = document.getElementById("writeCm").value;
  $.ajax({
    headers : {"token":sessionStorage.getItem("token")},
    url: ip +'/comments/',
    contentType : 'application/json; charset=UTF-8',
    type: 'POST',
    async: false,
    data: JSON.stringify({
      contents: detail,
      articleId: getParameters("id"),
      title: "title"
     }),
     success: function(data){
       console.log(data);

       comments_refresh();
       document.getElementById("writeCm").value = '';
      },
      error: function(data) {
       console.log(data);
       alert("댓글 등록 실패");
      }
   })
}

function comments_refresh(){
  var innerstr = "";
  var comment_box = document.getElementById("comment_box");

  $.ajax({
    url: ip +'/comments/list/search?articleId=' + getParameters("id") + '&sort=timeCreated,asc',
    contentType: 'application/json;  charset=UTF-8',
    type: 'GET',
    async: false,
    success: function(data){
        console.log(data);
        obj = data.content;
        console.log(obj);
        comment_box.innerHTML = "";

        for (var i = 0 ; i < obj.length ; i++){
          $.each(obj, function (){
            var text = obj[i].contents.replace(/\n/g,"<br />");
            innerstr = '<div class="comment">' +
            '<span class="cm_writer">' + obj[i].authorsNickname + '</span>' +
            '<span class="cm_content">' + text + '</span>' +
            '<span class="cm_time">' + obj[i].timeCreated.split(".")[0] + '</span>' +
            '</div>';
          })
        comment_box.innerHTML += innerstr;
        }
      },
      error : function(data) {
        alert("Error!!");
      }
   })
}
var flag = true;

function more() {
  if(flag == true) {
    $('#slide').css('visibility', 'visible');
    flag = false;
  }
  else if(flag == false){
    $('#slide').css('visibility', 'hidden');
    flag = true;
  }
}

function delete_post() {
  $.ajax({
    headers : {"token":sessionStorage.getItem("token")},
    url: ip +'/article/' + getParameters("id"),
    contentType: 'application/json',
    type: 'DELETE',
    async: false,
    success: function(data) {
      console.log(data);
      alert('게시글을 삭제하였습니다.');
      window.location.href = "main.html";
    },
    error: function(data) {
      console.log(data);
      alert('게시글 삭제 실패하였습니다.');
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

function content_modify() {
      window.location.href = "modify?id=" + id_index;
}
