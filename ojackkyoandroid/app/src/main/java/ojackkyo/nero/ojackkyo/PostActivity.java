package ojackkyo.nero.ojackkyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends AppCompatActivity {
    TextView name, kind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();

        name = (TextView)findViewById(R.id.title);
        kind = (TextView)findViewById(R.id.kind);

        String post_name = intent.getExtras().getString("name");
        String post_kind = intent.getExtras().getString("kind");

        kind.setText(post_kind);
        name.setText(post_name);
    }
}
