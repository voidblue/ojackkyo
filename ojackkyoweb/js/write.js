function editArticle(){
    sharp = $("#contents").val().split("#")
    xxx = []
    i = 0;
    console.log(sharp)
    for (var i = 1 ; i < sharp.length; i++){
        xxx[i-1] = sharp[i].split(" ")[0];
    }
    for (var i = 0 ; i < xxx.length; i++){
        str = '{"name":"' + sharp[i+1].split(" ")[0] + '"}';
        console.log(str)
        xxx[i] = JSON.parse(str);
    }
    console.log(xxx)
    $.ajax({
        headers : {"token":sessionStorage.getItem("token")},
        url: ip +'/article/',
        contentType : 'application/json; charset=UTF-8',
        type: 'POST',
        async: false,
        data : JSON.stringify({
            title : $("#title").val(),
            text : $("#contents").val(),
            tags : xxx
         }),
         success : function(data){
            console.log(data.token);
            window.location.href = "board.html"

         },
         error : function(data) {
           console.log(data)
         }
    })
}
