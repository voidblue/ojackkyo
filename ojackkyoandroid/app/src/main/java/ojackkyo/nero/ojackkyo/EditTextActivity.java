package ojackkyo.nero.ojackkyo;

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
import java.util.concurrent.ExecutionException;

import ojackkyo.nero.ojackkyo.connection.*;

public class EditTextActivity extends AppCompatActivity {

    private Spinner boardSpinner;
    private EditText edit_title, edit_text;
    private Button write_btn;
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


        boardSpinner = findViewById(R.id.board_type);
        ArrayAdapter<String> boardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, board_types);
        boardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boardSpinner.setAdapter(boardAdapter);

        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_text = (EditText) findViewById(R.id.edit_text);
        write_btn = (Button) findViewById(R.id.write_btn);


        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = edit_title.getText().toString();
                final String text = edit_text.getText().toString();
                final String board_type = boardSpinner.getSelectedItem().toString();

                JsonObject resultObject = null;
                Connection connection = new Connection();
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("title", title);
                jsonObject.addProperty("text", text);
                JsonArray jsonArray = new JsonArray();
                JsonObject tag = new JsonObject();
                tag.addProperty("name", boardSpinner.getSelectedItem().toString().substring(0, 2));
                JsonObject tag1 = new JsonObject();
                tag1.addProperty("name","구직");
                jsonArray.add(tag);
                jsonArray.add(tag1);
                jsonObject.add("tags", jsonArray);

                Log.e("게시글 작성", board_type + " " + title + " " + text);
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
                EditTextActivity.this.finish();
            }
        });
    }
}
