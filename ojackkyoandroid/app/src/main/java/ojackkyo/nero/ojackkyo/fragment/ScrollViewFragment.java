package ojackkyo.nero.ojackkyo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.ListResourceBundle;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import ojackkyo.nero.ojackkyo.List.ListViewAdapter;
import ojackkyo.nero.ojackkyo.PostActivity;
import ojackkyo.nero.ojackkyo.R;
import ojackkyo.nero.ojackkyo.UserInfo;
import ojackkyo.nero.ojackkyo.connection.Connection_list;

public class ScrollViewFragment extends Fragment {

    ObservableScrollView scrollView;
    TextView title;



    private ListView listView;
    private ListViewAdapter adapter;

    JSONObject listResult;
    JSONArray contentList;
    UserInfo userInfo;

    ArrayList<String> titleList = new ArrayList<String>();
    ArrayList<Integer> idList = new ArrayList<>();

    String[] tag;
    static int count;

    public static ScrollViewFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString("title",title);
        ScrollViewFragment fragment = new ScrollViewFragment();

        fragment.setArguments(args);
        count=0;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ObservableScrollView view = (ObservableScrollView)inflater.inflate(R.layout.fragment_scroll, container, false);
        userInfo = (UserInfo) getActivity().getApplicationContext();

        tag = userInfo.setTag();
        Connection_list connection_list = new Connection_list();
        try {

            Object list =  connection_list.execute(tag[count],userInfo.getToken()).get();
            count = count + 1;
            Log.e("count", "count: " + count );

            listResult = new JSONObject(list.toString());

            contentList = listResult.getJSONArray("content");

            Log.e("ddd", "결과: " + contentList.length());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for(int i = 0; i< contentList.length(); i++){
            try {
                listResult = contentList.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                titleList.add(listResult.getString("title"));
                idList.add(listResult.getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        Log.e("타이틀 결과", "타이틀결과 " + titleList.get(1) );

        // 게시판 목록 수정
        listView = (ListView)view.findViewById(R.id.listview);
        adapter = new ListViewAdapter();

        listView.setAdapter(null);

        // 해당 게시판에 게시글 갯수
        for(int i = 0; i<contentList.length();i++){
            // 제목 넣어주면 될듯
            adapter.addVO(titleList.get(i));
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PostActivity.class);

                Log.e("item", "onItemClick: "  + adapter.getItemId(position));
                int id1 = (int) adapter.getItemId(position);

                // 게시판 내용 넘겨주기
                intent.putExtra("id", idList.get(id1));
                startActivity(intent);
            }
        });
         return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollView = view.findViewById(R.id.scrollView);
        title = view.findViewById(R.id.title);
        ButterKnife.bind(this, view);

        title.setText(getArguments().getString("title"));

        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }
}
