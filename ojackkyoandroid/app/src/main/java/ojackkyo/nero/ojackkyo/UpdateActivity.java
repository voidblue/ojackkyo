package ojackkyo.nero.ojackkyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_title, edit_text;
    private Button write_btn;
    private String title, context;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        context = intent.getExtras().getString("context");

        userInfo = (UserInfo)getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_text = (EditText) findViewById(R.id.edit_text);
        write_btn = (Button) findViewById(R.id.write_btn);
        edit_text.setHorizontallyScrolling(false);

        edit_title.setText(title);
        edit_text.setText(context);

        write_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.write_btn:
                break;
        }
    }
}
