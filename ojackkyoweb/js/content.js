$.ajax({
    url: ip +'/article/' + 40,
    contentType : 'application/json',
    type: 'GET',
    async: false,
     success : function(data){
        console.log(data.token);
        $("#title").html(data.title);
        $("#contents").html(data.text);
        var str = data.authorsNickname + ", " + data.timeCreated.split(".")[0]   + ", " + data.viewed;
        $("#details").html(str)
     },
     error : function(data) {
         alert("ID,PW를 확인하세요");
     }
})
