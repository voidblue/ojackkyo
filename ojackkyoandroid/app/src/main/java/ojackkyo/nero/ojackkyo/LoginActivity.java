package ojackkyo.nero.ojackkyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.connection.Connection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login_btn, reg_btn;
    UserInfo userInfo;
    EditText id_input, pw_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInfo = (UserInfo) getApplicationContext();

        login_btn = (Button) findViewById(R.id.login_btn);
        reg_btn = (Button)findViewById(R.id.reg);

        reg_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);

        id_input = (EditText) findViewById(R.id.input_id);
        pw_input = (EditText) findViewById(R.id.input_pw);

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

                jsonObject.addProperty("uid", user_id);
                jsonObject.addProperty("password", user_pw);

                JsonObject resultObject = null;
                try {
                    Gson gson = new Gson();
                    String result = (String) connection.execute(jsonObject, "auth/login", "POST", null).get();
                    JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
                    resultObject = jsonElement.getAsJsonObject();

                    userInfo.setToken(resultObject.get("token").toString());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent main_intent = new Intent(this, MainActivity.class);
                startActivity(main_intent);
                LoginActivity.this.finish();
                break;

            case R.id.reg:
                Intent reg_intent = new Intent(this, RegActivity.class);
                startActivity(reg_intent);
                break;

        }
    }
}
