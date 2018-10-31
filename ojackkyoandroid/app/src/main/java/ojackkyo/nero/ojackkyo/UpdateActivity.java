package ojackkyo.nero.ojackkyo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ojackkyo.nero.ojackkyo.connection.Connection;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_title, edit_text;
    private Button update_btn;
    private String title, context, authorsNickname;
    private int id_index;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        context = intent.getExtras().getString("context");
        id_index = intent.getExtras().getInt("id");
        authorsNickname = intent.getExtras().getString("authorsNickname");

        userInfo = (UserInfo) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_text = (EditText) findViewById(R.id.edit_text);
        update_btn = (Button) findViewById(R.id.update_btn);
        edit_text.setHorizontallyScrolling(false);

        edit_title.setText(title);
        edit_text.setText(context);

        update_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.update_btn:
                final String title = edit_title.getText().toString();
                final String text = edit_text.getText().toString();

                JsonObject resultObject = null;
                Connection connection = new Connection();
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("title", title);
                jsonObject.addProperty("text", text);
                jsonObject.addProperty("id",id_index);
                JsonArray jsonArray = new JsonArray();

                ArrayList<String> tags_list = new ArrayList();
                tags_list.add("통합");

                Pattern MY_PATTERN = Pattern.compile("#(\\S+)");
                Matcher mat = MY_PATTERN.matcher(text);
                while (mat.find()) {
                    tags_list.add(mat.group(1));
                }

                HashSet<String> datalist = new HashSet(tags_list);
                tags_list = new ArrayList(datalist);

                Log.e("태그 테스트", String.valueOf(tags_list));

                for (int i = 0; i < tags_list.size(); i++) {
                    // 게시글 태그 추가하기
                    JsonObject tag = new JsonObject();
                    tag.addProperty("name", tags_list.get(i));
                    jsonArray.add(tag);

                    Log.e("태그 테스트 - 최종", tags_list.get(i));
                }
                jsonObject.add("tags", jsonArray);
                String result = "";

                Log.e("태그 테스트", String.valueOf(jsonObject));

                try {
                    result = (String) connection.execute(jsonObject, "article", "PUT", userInfo.getToken()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (result.equals("400")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(UpdateActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("제목 혹은 내용이 비어있습니다.");
                    alert.show();
                } else {
                    Gson gson = new Gson();
                    Log.e("result", result);
                    JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
                    Log.e("jsonElement", String.valueOf(jsonElement));
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                    UpdateActivity.this.finish();
                }

                break;
        }
    }
}
