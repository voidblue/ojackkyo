package ojackkyo.nero.ojackkyo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class EditTextActivity extends AppCompatActivity {

    private Spinner boardSpinner;
    private EditText edit_title, edit_text;
    private Button write_btn;
    private int viewd = 0;
    private String authorNickname = "test";

    ArrayList<String> board_types = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        board_types.add("자유게시판");
        board_types.add("구직게시판");
        board_types.add("익명게시판");
        board_types.add("질문게시판");


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
                Toast.makeText(getApplicationContext(), board_type + " " + title + " " + text + " " + viewd + " " + authorNickname, Toast.LENGTH_LONG).show();
            }
        });
    }
}
