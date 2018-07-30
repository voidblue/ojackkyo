package ojackkyo.nero.ojackkyo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import butterknife.ButterKnife;
import ojackkyo.nero.ojackkyo.List.ListViewAdapter;
import ojackkyo.nero.ojackkyo.PostActivity;
import ojackkyo.nero.ojackkyo.R;

public class ScrollViewFragment extends Fragment {

    ObservableScrollView scrollView;
    TextView title;

    private ListView listView;
    private ListViewAdapter adapter;

    public static ScrollViewFragment newInstance(String title){
        Bundle args = new Bundle();
        args.putString("title",title);
        ScrollViewFragment fragment = new ScrollViewFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ObservableScrollView view = (ObservableScrollView)inflater.inflate(R.layout.fragment_scroll, container, false);

        // 게시판 목록 수정
        listView = (ListView)view.findViewById(R.id.listview);
        adapter = new ListViewAdapter();

        listView.setAdapter(null);

        for(int i = 0; i<30;i++){
            adapter.addVO("test " + i);
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                // 게시판 내용 넘겨주기
                intent.putExtra("kind", getArguments().getString("title"));
                intent.putExtra("name","test" + position);
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
