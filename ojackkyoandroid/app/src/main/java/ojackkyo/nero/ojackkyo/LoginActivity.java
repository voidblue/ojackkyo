package ojackkyo.nero.ojackkyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.connection.Connection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    UserInfo userInfo;
    EditText id_input, pw_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInfo = (UserInfo)getApplicationContext();

        btn = (Button) findViewById(R.id.login_btn);
        btn.setOnClickListener(this);

        id_input = (EditText)findViewById(R.id.input_id);
        pw_input = (EditText)findViewById(R.id.input_pw);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_btn:

                String user_id = id_input.getText().toString();
                String user_pw = pw_input.getText().toString();

                Connection connection = new Connection();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("uid",user_id);
                jsonObject.addProperty("password",user_pw);

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
