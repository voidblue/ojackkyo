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

$("#header").html('\
<div id="logo">\
  <a href="main.html"><img src="img/frame.png" id="imgLogo"></a>\
</div>\
<div id="search">\
  <input type="search" name="search_bar">\
  <button type="button" name="btnSearch"><img id="imgSearch" src="img/search.svg"></button>\
</div>\
<div id="btnPack">\
  <button type="button" class="btn message"><a href="#"><img src="img/message.svg"></a></button>\
  <button type="button" class="btn alarm"><a href="#"><img src="img/alarm.svg"></a></button>\
  <button type="button" class="btn user"><a href="#"><img src="img/user.svg"></a></button>\
</div>')
