package ojackkyo.nero.ojackkyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.connection.Connection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInfo = (UserInfo)getApplicationContext();

        btn = (Button) findViewById(R.id.login_btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_btn:

                
                Connection connection = new Connection();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("uid","test");
                jsonObject.addProperty("password","test");

                JsonObject resultObject = null;
                try {
                    Gson gson = new Gson();
                    String result = (String) connection.execute(jsonObject,"auth/login","POST", null).get();
                    JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
                    resultObject = jsonElement.getAsJsonObject();

                    userInfo.setToken(resultObject.get("token").toString());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
        }
    }
}
