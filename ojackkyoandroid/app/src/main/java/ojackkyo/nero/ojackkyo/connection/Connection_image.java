package ojackkyo.nero.ojackkyo.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ojackkyo.nero.ojackkyo.UserInfo;

import static android.content.ContentValues.TAG;

public class Connection_image extends AsyncTask {
    private String path = "";
    private String method = "";
    private UserInfo userInfo;

    @Override
    protected String doInBackground(Object[] objects) {

        String serverURL = "http://117.17.102.131:4000/article/image";  //인자를 받는게 아니라서 아이피로 바로 연결
            try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestProperty("content-type", "application/json; charset=utf-8");
            httpURLConnection.setRequestProperty("token", (String) objects[1]); //토큰 이름
            httpURLConnection.setRequestProperty("image",(String)objects[2]);
            httpURLConnection.setRequestProperty("articleId", (String)objects[3]);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST"); //이미지 올리는 방식은 무조건 포스트
            httpURLConnection.connect();

//            JsonObject jsonobject = (JsonObject) objects[0]; //jsonobject 를 받고

            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                Log.d(TAG, "********************HTTP_OK**********************");

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(sb.toString(), JsonElement.class);
                JsonObject result = jsonElement.getAsJsonObject();

                bufferedReader.close();

                return result.toString();

            } else {
                Log.e("에러 테스트", String.valueOf(responseStatusCode));
                return String.valueOf(responseStatusCode);
            }


        } catch (Exception e) {

            JsonObject jsonObject1 = new JsonObject();
            e.printStackTrace();
            jsonObject1.addProperty("error", e.getMessage());

            return jsonObject1.toString();
        }

    }
}
