package ojackkyo.nero.ojackkyo;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void reset() {
        this.token = "";
        this.정보 = "";
        this.nickname = "";
        this.text = null;
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
        Log.e("닉네임", "setNickname: " + resultObject.get("nickname").toString());
        Log.e("학번", "setUid: " + resultObject.get("uid").toString());
        this.nickname = resultObject.get("nickname").toString();
    }

    public List<String> setTag() {  // 태그 스트링 배열 호출
        List<String> tag = new ArrayList<String >();
        Gson gson = new Gson();
        JsonObject resultObject;
        JsonElement jSonElement = gson.fromJson(정보, JsonElement.class);
        resultObject = jSonElement.getAsJsonObject();
        String tagString = resultObject.get("tags").toString();
        Pattern MY_PATTERN = Pattern.compile("\\=(\\S+)\\}");
        Matcher mat = MY_PATTERN.matcher(tagString);
        tag.add("통합");
        while (mat.find()) {
            tag.add(mat.group(1));
        }
        return tag;
    }


}

