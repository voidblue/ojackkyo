if (sessionStorage.getItem("token")==null){
    $("#ifLogin").html('\
    <button type="button"><a href="#">Sign in</a></button>\
    <button type="button"><a href="Login.html">Login</a></button>\
      ')
    } 
    else{
        $("#ifLogin").html('<button type="button" onclick="logout()"><a href="#">Logout</a></button>')
    }

function logout(){
    sessionStorage.clear();
    window.location.reload();
}

var titleArray = ['', '', '']
var string
for(var i = 0; i<3; i++){
    
    string += '<table class="attr"><thead><tr><th scope="cols"><button type="button" class="plusButton">+</button>' + titleArray[i]+ '</th></tr></thead>'
    
        $.ajax({
        url: ip + '/user/1',
        contentType : 'application/json',
        type: 'GET',
        async: false,
        success : function(data){
            console.log(data)
            string += '<tr><td>' + data.uid + '</td></tr>';
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

