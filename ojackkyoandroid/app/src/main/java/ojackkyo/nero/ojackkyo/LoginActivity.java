package ojackkyo.nero.ojackkyo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
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
        reg_btn = (Button) findViewById(R.id.reg);

        reg_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);

        id_input = (EditText) findViewById(R.id.input_id);
        pw_input = (EditText) findViewById(R.id.input_pw);

        pw_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    login_btn.performClick();
                }
                return false;
            }
        });
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
                String result = "";
                try {
                    result = (String) connection.execute(jsonObject, "auth/login", "POST", null).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (result.equals("400")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("ID/PW를 확인해주세요.");
                    alert.show();
                } else {
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
                    resultObject = jsonElement.getAsJsonObject();
                    Log.e("외않되", resultObject.get("token").toString());
                    userInfo.setToken(resultObject.get("token").toString());

                    Intent main_intent = new Intent(this, MainActivity.class);
                    startActivity(main_intent);
                    LoginActivity.this.finish();
                }
                break;

            case R.id.reg:
                Intent reg_intent = new Intent(this, RegActivity.class);
                reg_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(reg_intent);
                break;
            case R.id.find:
                Intent find_intent = new Intent(this, RegActivity.class);
                startActivity(find_intent);
                break;

        }
    }
}
