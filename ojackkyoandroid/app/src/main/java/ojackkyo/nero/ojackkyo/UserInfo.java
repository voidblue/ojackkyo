package ojackkyo.nero.ojackkyo;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.SimpleTimeZone;
import java.util.logging.Level;

/**
 * Created by wjdal on 2018-08-06.
 */

public class UserInfo extends Application {
    private String token;
    private String 정보;
    private String nickname;
    private String[] text;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token.substring(1, token.length() - 1);
        text = this.token.split("\\.", 0);
        정보 = new String(Base64.decode(text[1], Base64.DEFAULT));
        Log.e("정보", 정보);
        setNickname();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname() {
        Gson gson = new Gson();
        JsonObject resultObject;
        JsonElement jsonElement = gson.fromJson(정보, JsonElement.class);
        resultObject = jsonElement.getAsJsonObject();
        Log.e("닉네임", "setNickname: " + resultObject.get("nickname").toString() );
        this.nickname = resultObject.get("nickname").toString();
    }

    public String[] setTag() {  // 태그 스트링 배열 호출
        String tag;
        Gson gson = new Gson();
        JsonObject resultObject;
        JsonElement jSonElement = gson.fromJson(정보, JsonElement.class);
        resultObject =jSonElement.getAsJsonObject();
        String tagString = resultObject.get("tags").toString();
        tag = tagString.substring(8,tagString.length()-3);

//        Log.e("태긍ㅇㅇ", "setTag: " + tag );
//        for(int i=0; i<tag.split("\\}, \\{name=").length; i++){
//            Log.e("태그", "setTag: " + tag.split("\\}, \\{name=")[i] );
//        }
        return tag.split("\\}, \\{name=");

    }
}
