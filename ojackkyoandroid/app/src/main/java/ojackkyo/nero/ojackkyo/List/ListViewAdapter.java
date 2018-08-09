package ojackkyo.nero.ojackkyo.List;

/**
 * Created by wjdal on 2018-07-05.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import ojackkyo.nero.ojackkyo.R;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListVO> listVO = new ArrayList<ListVO>();

    public ListViewAdapter() {

    }

    @Override
    public int getCount() {
        return listVO.size();
    }

    // ** 이 부분에서 리스트뷰에 데이터를 넣어줌 **
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //postion = ListView의 위치      /   첫번째면 position = 0
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        TextView state = (TextView) convertView.findViewById(R.id.state);
        ListVO listViewItem = listVO.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        state.setText(listViewItem.getTitle());

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listVO.get(position);
    }

    // 데이터값 넣어줌
    public void addVO(String items) {
        ListVO item = new ListVO();
        item.setTitle(items);
        listVO.add(item);
    }
}