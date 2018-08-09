if(sessionStorage.getItem("token") == null){

}
else{
    b64claims = sessionStorage.getItem("token").split(".")[1];    
    claims = JSON.parse(b64DecodeUnicode(b64claims));
    
    console.log(claims);
    var arrayTag=claims.tags.split('=');
    
    for(var i=0; i<arrayTag.length; i++){
        arrayTag[i] = arrayTag[i].split('}')[0];

    }
    
}

//디코드 함수//
function b64DecodeUnicode(str) {
    // Going backwards: from bytestream, to percent-encoding, to original string.
    return decodeURIComponent(atob(str).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}
//디코드 함수//