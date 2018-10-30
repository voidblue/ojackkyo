package ojackkyo.nero.ojackkyo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        userInfo = (UserInfo) getApplicationContext();

        Connection connection = new Connection();
        JsonObject jsonObject = new JsonObject();
        JsonObject resultObject = null;

        Toolbar toolbar = (Toolbar) findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        int id_index = intent.getExtras().getInt("id");

        name = (TextView) findViewById(R.id.title);
        context = (TextView) findViewById(R.id.context);

        Log.e("게시글 보기", String.valueOf(id_index));

        try {
            Gson gson = new Gson();

            String result = (String) connection.execute(jsonObject, "article/" + id_index, "GET", null).get();
            JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
            resultObject = jsonElement.getAsJsonObject();

            String post_context = resultObject.get("text").toString().replace("\\n", "\n");
            String real_context = post_context.substring(1, post_context.length() - 1);
            String post_name = resultObject.get("title").toString().substring(1, resultObject.get("title").toString().length() - 1);

            context.setText(real_context);
            name.setText(post_name);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                return true;

            case R.id.삭제하기:
                Log.e("삭제하기","삭제하기");
                return true;

            case R.id.수정하기:
                Log.e("수정하기","수장하기");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
