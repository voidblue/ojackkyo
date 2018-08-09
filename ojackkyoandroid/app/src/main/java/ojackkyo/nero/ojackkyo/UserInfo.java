package ojackkyo.nero.ojackkyo;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        this.token = token.substring(1,token.length() -1);
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
        this.nickname = resultObject.get("nickname").toString();
    }
}
