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

/**
 * Created by wjdal on 2018-08-02.
 */

public class Connection extends AsyncTask {

    private String path = "";
    private String method = "";
    private UserInfo userInfo;


    @Override
    protected String doInBackground(Object[] objects) {

        String serverURL = "http://117.17.102.131:4000/" + objects[1];  // 그거 1개\
        try {


            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestProperty("content-type", "application/json; charset=utf-8");
            httpURLConnection.setRequestProperty("token", (String) objects[3]);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod((String) objects[2]); // 방식 2개
            httpURLConnection.connect();

            JsonObject jsonobject = (JsonObject) objects[0]; // 객체 이름 3개

            if (((String) objects[2]).equals("GET") || ((String) objects[2]).equals("DELETE")) {

            } else {

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(jsonobject.toString().getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
            }


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
                return "bb";
            }


        } catch (Exception e) {

            JsonObject jsonObject1 = new JsonObject();
            e.printStackTrace();
            jsonObject1.addProperty("error", e.getMessage());

            return jsonObject1.toString();
        }

    }
}
