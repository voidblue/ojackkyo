$("#submit").click(function(){
  
    $.ajax({
        url: 'http://192.168.43.202:4000/auth/login',
        contentType : 'application/json',
        type: 'POST',
        async: false,
        data : JSON.stringify({
            uid : $("#login__username").val(),
            password : $("#login__password").val()
        }),
        success : function(data){
            console.log(data);
            sessionStorage.setItem("token", data);
        },
        error : function(a, b, c){  
            console.log(a)
            console.log(b)
            console.log(c)
        }
    })  
    
})

