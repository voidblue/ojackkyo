function editArticle(){
  var txt = $("#contents").val();
  var tags = ["#통합"];
  var xxx = [];
  txt = txt.replace(/#[^#\s,;]+/gm, function(tag) {
   tags.push(tag);
   });
   console.log(tags);

    for (var i = 0 ; i < tags.length; i++){
        str = '{"name":"' + tags[i].substring(1,tags[i].length) + '"}';
        console.log(str);
        xxx[i] = JSON.parse(str);
    }
    console.log(xxx);

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
            window.location.href = "main.html";

         },
         error : function(data) {
            console.log(data);
         }
    })
}
