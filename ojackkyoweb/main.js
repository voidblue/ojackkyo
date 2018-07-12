if (sessionStorage.getItem("token")==null){
    $("#ifLogin").html('\
    <button type="button"><a href="#">Sign in</a></button>\
    <button type="button"><a href="Login.html">Login</a></button>\
      ')
    } 
    else{
        $("#ifLogin").html('<button type="button"><a href="#">Logout</a></button>')
    }
for(var i = 0; i<3; i++){
  string += '<table class="attr"><thead><tr><th scope="cols"><button type="button" class="plusButton">+</button>' +titleArray[i]+ '</th></tr></thead>'
        $.ajax({
        url: 'http://192.168.43.202:4000/user/list',
        contentType : 'application/json',
        type: 'GET',
        async: false,
        success : function(data){
            console.log(data.length)

            for (var j = 0 ; j < data.length ; j++){
                string += '<tr><td>' + data[j].uid + '</td></tr>';
            }
        },
        error : function(a, b, c){
            console.log(a)
            console.log(b)
            console.log(c)
        }
    })
  string +='</table>'
};

$('#table').html(string);

$(".footer").offset({top:$(document).scrollTop() + $(window).height()-60})
$(window).scroll(function(){    
    $(".footer").offset({top:$(document).scrollTop() + $(window).height()-60})     
})

