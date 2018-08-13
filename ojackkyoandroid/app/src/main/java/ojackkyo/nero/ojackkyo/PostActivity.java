package ojackkyo.nero.ojackkyo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.connection.Connection;

public class PostActivity extends AppCompatActivity {
    TextView name, context;
    UserInfo userInfo;
    Button back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        userInfo = (UserInfo) getApplicationContext();

        Connection connection = new Connection();
        JsonObject jsonObject = new JsonObject();
        JsonObject resultObject = null;

        Intent intent = getIntent();
        int id_index = intent.getExtras().getInt("id");

        name = (TextView) findViewById(R.id.title);
        context = (TextView) findViewById(R.id.context);
        back_btn = (Button)findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        Log.e("게시글 보기", String.valueOf(id_index));
        try {
            Gson gson = new Gson();

            String result = (String) connection.execute(jsonObject, "article/" + id_index, "GET", null).get();
            JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
            resultObject = jsonElement.getAsJsonObject();

            String post_context = resultObject.get("text").toString().substring(1, resultObject.get("text").toString().length() - 1);
            String post_name = resultObject.get("title").toString().substring(1, resultObject.get("title").toString().length() - 1);

            context.setText(post_context);
            name.setText(post_name);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
