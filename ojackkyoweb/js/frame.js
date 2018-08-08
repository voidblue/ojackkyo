$("#footer").offset({top:$(document).scrollTop() + $(window).height()-convertRemToPixels(3)})
$(window).scroll(function(){
    $("#footer").offset({top:$(document).scrollTop() + $(window).height()-convertRemToPixels(3)})
})

$("#header").offset({top:$(document).scrollTop()});
$(window).scroll(function(){
  $("#header").offset({top:$(document).scrollTop()});
})

function convertRemToPixels(rem) {
    return rem * parseFloat(getComputedStyle(document.documentElement).fontSize);
}
