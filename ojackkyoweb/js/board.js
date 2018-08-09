var arrayTag;


if(sessionStorage.getItem("token") == null){

}
else{
    b64claims = sessionStorage.getItem("token").split(".")[1];    
    claims = JSON.parse(b64DecodeUnicode(b64claims));
    
    console.log(claims);
    arrayTag=claims.tags.split('=');
    
    for(var i=0; i<arrayTag.length; i++){
        arrayTag[i] = arrayTag[i].split('}')[0];

    }
    str = "";
    for(var i=1; i<arrayTag.length; i++){
        innerstr = "";
        $.ajax({
            url: ip +'/article/list/search?tag=' + arrayTag[i],
            contentType : 'application/json',
            type: 'GET',
            async: false,
            success : function(data){
                console.log(data)
                obj = data.content
                console.log(obj)
                $.each(obj, function (key, value){
                    innerstr += '<a href="content.html" >\
                    <div class="content">'+value.title+'</div>\
                    </a>'
                })
                str += '<div class = "tag" id='+arrayTag[i]+'>\
                    <div class="title">\
                    '+ "#"+arrayTag[i] +'\
                    </div>\
                    '+ innerstr +'\
                    <a href="write.html">\
                        <button type="button" name="write">글쓰기</button>\
                    </a>\
                </div>'      
            },
            error : function(data) {
                console.log(data)
            }
        })

    }
    $("#board").html(str);

}

//디코드 함수//
function b64DecodeUnicode(str) {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(atob(str).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}
//디코드 함수//