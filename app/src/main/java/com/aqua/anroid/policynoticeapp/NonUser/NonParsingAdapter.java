package com.aqua.anroid.policynoticeapp.NonUser;


import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aqua.anroid.policynoticeapp.Parser.PublicDataList;
import com.aqua.anroid.policynoticeapp.R;

import java.util.ArrayList;

public class NonParsingAdapter extends BaseAdapter {
    private static String TAG = "phptest";

    private Context context;

    ArrayList<PublicDataList> publicDataLists = new ArrayList<PublicDataList>();

    private static String IP_ADDRESS = "10.0.2.2";
    private Activity activity;
    private OnItemClick listener;

    String servID;


    public NonParsingAdapter(Context context, ArrayList<PublicDataList> publicDataLists, OnItemClick listener, Activity activity) {
        this.context = context;
        this.publicDataLists = publicDataLists;
        this.listener = listener;
        this.activity = activity;
    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return publicDataLists.size();
    }

    //뷰홀더 추가
    class ViewHolder {
        TextView list_text_name;
        TextView list_text_content;

    }

    //i에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        //int pos = i;
        Context context = parent.getContext();
        final ViewHolder holder;//아이템 내 view들을 저장할 holder 생성

        final PublicDataList publicDataList_item = publicDataLists.get(i);

        Log.d(TAG, "items_adapter : " + publicDataLists.toString());


        //"item_list" Layout을 inflate하여 view 참조 획득
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //최초 생성 view인 경우, inflation -> ViewHolder 생성 -> 해당 View에 setTag 저장
            view = inflater.inflate(R.layout.non_parsing_list, parent, false);

            holder = new ViewHolder();

            //화면에 표시될 View(Layoutㅇ inflate된)으로부터 위젯에 대한 참조 획득
            holder.list_text_name = (TextView) view.findViewById(R.id.list_text_name);
            holder.list_text_content = (TextView) view.findViewById(R.id.list_text_content);

            //해당 view에 setTag로 Holder 객체 저장
            view.setTag(holder);
        } else {
            //view가 이미 생성된 적이 있다면, 저장되어 있는 Holder 가져오기
            holder = (ViewHolder) view.getTag();
        }

        holder.list_text_name.setText(publicDataList_item.getServNm());
        holder.list_text_content.setText(publicDataList_item.getServDgst());


        LinearLayout select_item = (LinearLayout) view.findViewById(R.id.select_item);

        select_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servID = publicDataList_item.getServID();
                Log.d("servID", servID);
                listener.onClick(servID);
            }
        });

        //해당 view 반납
        return view;
    }

    public interface OnItemClick {
        void onClick (String value);
    }


    //지정한 위치(i)에 있는 데이터 리턴턴
    @Override
    public Object getItem(int i) {
        return publicDataLists.get(i);
    }


    //지정한 위치(i)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int i) {
        return i;
    }
}