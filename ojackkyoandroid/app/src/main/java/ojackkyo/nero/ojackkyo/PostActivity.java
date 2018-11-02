package ojackkyo.nero.ojackkyo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.List.CommentList;
import ojackkyo.nero.ojackkyo.List.CommentListViewAdapter;
import ojackkyo.nero.ojackkyo.connection.Connection;
import ojackkyo.nero.ojackkyo.connection.Connection_list;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {
    TextView name, context, comment_et, context_time, user_name;
    ListView comment_view;
    UserInfo userInfo;
    Button comment_btn;
    Connection connection;
    Connection connection_delete;
    Connection input_comment;
    Connection_list connection_comment_read;
    Connection_list connection_comment_refresh;
    String authorsNickname;
    String real_context;
    String post_name;
    String time;
    String viewed;
    int id_index;

    CommentListViewAdapter adapter;
    ArrayList<CommentList> comment_list;
    JSONArray contentList;
    JSONObject object;
    Object result_comment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        userInfo = (UserInfo) getApplicationContext();

        comment_view = (ListView) findViewById(R.id.comment_lv);
        comment_list = new ArrayList<CommentList>();

        connection = new Connection();
        connection_comment_read = new Connection_list();
        connection_comment_refresh = new Connection_list();

        JsonObject jsonObject = new JsonObject();
        JsonObject resultObject = null;
        JsonObject commentObject = null;

        Toolbar toolbar = (Toolbar) findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Intent intent = getIntent();
        id_index = intent.getExtras().getInt("id");

        name = (TextView) findViewById(R.id.title);
        context = (TextView) findViewById(R.id.context);
        comment_et = (TextView) findViewById(R.id.comment_et);
        comment_btn = (Button) findViewById(R.id.comment_btn);
        context_time = (TextView) findViewById(R.id.context_time);
        user_name = (TextView) findViewById(R.id.user_name);
        comment_btn.setOnClickListener(this);

        Log.e("게시글 보기", String.valueOf(id_index));

        try {
            Gson gson = new Gson();

            // 게시글 내용 받아오기
            String result = (String) connection.execute(jsonObject, "article/" + id_index, "GET", null).get();
            JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
            resultObject = jsonElement.getAsJsonObject();

            String post_context = resultObject.get("text").toString().replace("\\n", "\n");
            real_context = post_context.substring(1, post_context.length() - 1);
            post_name = resultObject.get("title").toString().substring(1, resultObject.get("title").toString().length() - 1);
            authorsNickname = resultObject.get("authorsNickname").toString();
            time = resultObject.get("timeCreated").toString().substring(1, resultObject.get("timeCreated").toString().length() - 6);
            viewed = resultObject.get("viewed").toString();
            String tag = resultObject.get("tags").toString();
            Log.e("게시글은 태그를 가져 올까?", tag);

//            // 게시글 내부 댓글 받아오기
//            result_comment = connection_comment_read.execute("comments", "articleId=" + id_index, "asc", null).get();
//            object = new JSONObject(result_comment.toString());


//            contentList = object.getJSONArray("content");
////            comment_list.add(new CommentList(commentObject.get("contents").toString()));
//            Log.e("ddd", "결과: " + contentList.length());
//            Log.e("ddd", "결과: " + contentList.get(0));
////            comment_list.add(new CommentList("댓글 테스트"));
//
//            Log.e("댓글 테스트", String.valueOf(commentObject));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < contentList.length(); i++) {
//            JSONObject test = null;
//            try {
//                test = contentList.getJSONObject(i);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            try {
//                comment_list.add(new CommentList(test.getString("contents")));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        adapter = new CommentListViewAdapter(PostActivity.this, comment_list);
//        comment_view.setAdapter(adapter);

        context.setText(real_context);
        name.setText(post_name);
//
//        commentObject.get("contents").toString();
//        commentObject.get("timeCreated").toString();
//        commentObject.get("authorsNickname").toString();

        context_time.setText("작성시간 : " + time + " | 조회수 : " + viewed);
        user_name.setText("작성자 : " + authorsNickname.substring(1, authorsNickname.length() - 1));
    }

    @Override
    protected void onResume() {
        try {
            connection_comment_refresh = new Connection_list();
            result_comment = connection_comment_refresh.execute("comments", "articleId=" + id_index, "asc", null).get();
            object = new JSONObject(result_comment.toString());
            contentList = object.getJSONArray("content");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        comment_list.clear();
        for (int i = 0; i < contentList.length(); i++) {
            JSONObject test = null;
            try {
                test = contentList.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                comment_list.add(new CommentList(test.getString("contents")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new CommentListViewAdapter(PostActivity.this, comment_list);
        comment_view.setAdapter(adapter);
        super.onResume();
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
                intent1.putExtra("id", id_index);
                intent1.putExtra("title", post_name);
                intent1.putExtra("context", real_context);
                intent1.putExtra("authorsNickname", authorsNickname.substring(1, authorsNickname.length() - 1));
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
                String comment = comment_et.getText().toString();
                JsonObject jsonObject = new JsonObject();
                input_comment = new Connection();

                jsonObject.addProperty("contents", comment);
                jsonObject.addProperty("articleId", id_index);
                jsonObject.addProperty("title", "abc");

                input_comment.execute(jsonObject, "comments", "POST", userInfo.getToken());
                onResume();
                Toast.makeText(this, comment, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
