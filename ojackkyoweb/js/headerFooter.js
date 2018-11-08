// $("#footer").offset({top:$(document).scrollTop() + $(window).height()-convertRemToPixels(3)})
// $(window).scroll(function(){
//     var limit = $(document).scrollTop() +$(window).height()-convertRemToPixels(3);
//     if(limit < convertRemToPixels(0)  + $('html').height()){
//       $("#footer").offset({top:$(document).scrollTop() + $(window).height()-convertRemToPixels(3)})
//     }
// })


//스크롤 내려도 header 고정

$("#header").offset({top:$(document).scrollTop()});
$(window).scroll(function(){
  $("#header").offset({top:$(document).scrollTop()});
})
//
// function convertRemToPixels(rem) {
//     return rem * parseFloat(getComputedStyle(document.documentElement).fontSize);
// }

$("#header").on('click', warning);
// $("#header").addEventListener('click', warning);

function warning(e) {
  var currentNode = e.target.parentElement;
  console.log(currentNode.className);

  if(e.target.className == "imgMessage" && currentNode.className == "hrefMessage") {
    alert("메시지 기능 구현 중입니다. 조금만 기다려 주세요!!");
  }
  else if(e.target.className == "imgAlarm" && currentNode.className == "hrefAlarm") {
    alert("알람 기능 구현 중입니다. 조금만 기다려 주세요!!");
  }
  // else if(e.target.className == "imgUser" && currentNode.className == "hrefUser") {
  //   $("#navbar").css('visibility', 'visible');
  // }
}

function logout() {
  alert('로그아웃 하시겠습니까?');
  window.location.href = "index.html";
}

function search() {
  if (window.event.keyCode == 13) {
    var search_bar = jQuery(this).attr("search_bar");
    alert('검색 기능 구현 중입니다.');
    search_bar.value = "";
  }
}

function modify() {
  alert('회원정보 수정 기능 구현 중입니다.');
}

var flag = true;

function slidebar() {
  if(flag == true) {
    $("#navbar").css('visibility', 'visible');
    flag = false;
  }
  else if(flag == false) {
    $("#navbar").css('visibility', 'hidden');
    flag = true;
  }
}

$("#header").html('\
<div id="logo">\
  <a href="main.html"><img src="img/frame.png" id="imgLogo"></a>\
</div>\
<div id="search">\
  <input type="search" name="search_bar" id="search_bar" onkeyup="search();">\
  <button type="button" name="btnSearch"><img id="imgSearch" src="img/search.svg"></button>\
</div>\
<div id="btnPack" class="btnPack">\
  <button type="button" class="btn message"><a href="#" class="hrefMessage"><img src="img/message.svg" class="imgMessage"></a></button>\
  <button type="button" class="btn alarm"><a href="#" class="hrefAlarm"><img src="img/alarm.svg" class="imgAlarm"></a></button>\
  <button type="button" class="btn user" onclick="slidebar();"><a href="#" class="hrefUser"><img src="img/user.svg" class="imgUser"></a></button>\
</div>');

$("#navbar").html('\
<a href="#">\
  <button type="button" class="nav modify" onclick="modify();">회원정보 수정</button>\
</a>\
<button type="button" class="nav logout" onclick="logout();">로그아웃</button>');
