package ojackkyo.nero.ojackkyo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import ojackkyo.nero.ojackkyo.List.ListViewAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ListViewAdapter();
        listView = (ListView)findViewById(R.id.testListView);
        listView.setDivider(null);

        listView.setAdapter(adapter);

        for(int i = 0; i<10;i++){
            adapter.addVO("test " + i);
        }
    }
}
