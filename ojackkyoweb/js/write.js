function editArticle(){
    $.ajax({
        headers : {"token":sessionStorage.getItem("token")},
        url: ip +'/article/',
        contentType : 'application/json',
        type: 'POST',
        async: false,
        data : JSON.stringify({
            title : $("#title").val(),
            text : $("#contents").val()
         }),
         success : function(data){
            console.log(data.token);
            window.location.href = "frame.html";
         },
         error : function(data) {
           console.log(data)
         }
    })
}
