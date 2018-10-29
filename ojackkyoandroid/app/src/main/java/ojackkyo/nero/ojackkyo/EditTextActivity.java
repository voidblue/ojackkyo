package ojackkyo.nero.ojackkyo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.connection.*;

public class EditTextActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_title, edit_text;
    private Button write_btn, back_btn;
    UserInfo userInfo;

    ArrayList<String> board_types = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        board_types.add("통합게시판");
        board_types.add("자유게시판");
        board_types.add("구직게시판");
        board_types.add("질문게시판");
        userInfo = (UserInfo) getApplicationContext();

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_text = (EditText) findViewById(R.id.edit_text);
        write_btn = (Button) findViewById(R.id.write_btn);
        back_btn = (Button) findViewById(R.id.back_btn);
        edit_text.setHorizontallyScrolling(false);

        back_btn.setOnClickListener(this);
        write_btn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditTextActivity.this, MainActivity.class);
        startActivity(intent);
        EditTextActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_btn:
                onBackPressed();
                finish();
                break;

            case R.id.write_btn:
                final String title = edit_title.getText().toString();
                final String text = edit_text.getText().toString();

                JsonObject resultObject = null;
                Connection connection = new Connection();
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("title", title);
                jsonObject.addProperty("text", text);
                JsonArray jsonArray = new JsonArray();

                ArrayList<String> tags_list = new ArrayList();
                ArrayList<String> list = new ArrayList();
                list.add("통합");

                String[] tags = text.replace("\n","").split("#");
                Collections.addAll(tags_list, tags);
                tags_list.remove(0);
                Log.e("태그 테스트", String.valueOf(tags_list));

                for (int i = 0; i < tags_list.size(); i++) {
                    list.add(tags_list.get(i).split(" ")[0]);
                }
                Log.e("리스트 테스트", String.valueOf(list));

                for (int i = 0; i < list.size(); i++) {
                    // 게시글 태그 추가하기
                    JsonObject tag = new JsonObject();
                    tag.addProperty("name", list.get(i));
                    jsonArray.add(tag);

                    Log.e("태그 테스트 - 최종", list.get(i));
                }
                jsonObject.add("tags", jsonArray);

                try {
                    Gson gson = new Gson();
                    String result = (String) connection.execute(jsonObject, "article", "POST", userInfo.getToken()).get();

                    Log.e("result", result);

                    JsonElement jsonElement = gson.fromJson(result, JsonElement.class);
                    resultObject = jsonElement.getAsJsonObject();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(EditTextActivity.this, MainActivity.class);
                startActivity(intent);
                EditTextActivity.this.finish();
                break;

        }
    }
}
