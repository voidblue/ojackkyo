package ojackkyo.nero.ojackkyo;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.connection.Connection;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    TextView name, context, comment_tv, context_time, user_name;
    UserInfo userInfo;
    Button comment_btn;
    Connection connection;
    Connection connection_delete;
    String authorsNickname;
    String real_context;
    String post_name;
    int id_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        userInfo = (UserInfo) getApplicationContext();

        connection = new Connection();
        JsonObject jsonObject = new JsonObject();
        JsonObject resultObject = null;

        Toolbar toolbar = (Toolbar) findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent intent = getIntent();
        id_index = intent.getExtras().getInt("id");

        name = (TextView) findViewById(R.id.title);
        context = (TextView) findViewById(R.id.context);
        comment_tv = (TextView) findViewById(R.id.comment_tv);
        comment_btn = (Button) findViewById(R.id.comment_btn);
        context_time = (TextView) findViewById(R.id.context_time);
        user_name = (TextView)findViewById(R.id.user_name);
        comment_btn.setOnClickListener(this);

        Log.e("게시글 보기", String.valueOf(id_index));

        try {
            Gson gson = new Gson();

            String result = (String) connection.execute(jsonObject, "article/" + id_index, "GET", null).get();
            JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
            resultObject = jsonElement.getAsJsonObject();

            String post_context = resultObject.get("text").toString().replace("\\n", "\n");
            real_context = post_context.substring(1, post_context.length() - 1);
            post_name = resultObject.get("title").toString().substring(1, resultObject.get("title").toString().length() - 1);
            authorsNickname = resultObject.get("authorsNickname").toString();
            String time = resultObject.get("timeCreated").toString().substring(1, resultObject.get("timeCreated").toString().length() - 6);
            String viewed = resultObject.get("viewed").toString();
            Log.e("asdfasdf", id_index + " "+ real_context  +"  "+ post_name +"  "+ authorsNickname +"  "+ time +"  "+ viewed);

            context.setText(real_context);
            name.setText(post_name);

            context_time.setText("작성시간 : " + time + " | 조회수 : " + viewed);
            user_name.setText("작성자 : "+ authorsNickname.substring(1,authorsNickname.length()-1));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (authorsNickname.equals(userInfo.getNickname())) {
            getMenuInflater().inflate(R.menu.main, menu);
        } else {

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.삭제하기:
                connection_delete = new Connection();
                connection_delete.execute(null, "article/" + id_index, "DELETE", userInfo.getToken());
                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                startActivity(intent);
                PostActivity.this.finish();
                return true;

            case R.id.수정하기:
                Intent intent1 = new Intent(PostActivity.this, UpdateActivity.class);
                intent1.putExtra("id",id_index);
                intent1.putExtra("title",post_name);
                intent1.putExtra("context",real_context);
                intent1.putExtra("authorsNickname",authorsNickname.substring(1,authorsNickname.length()-1));
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.comment_btn:
                String comment = comment_tv.getText().toString();
                Toast.makeText(this, comment, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
