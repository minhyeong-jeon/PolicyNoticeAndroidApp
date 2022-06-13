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
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataList;
import com.aqua.anroid.policynoticeapp.R;

import java.util.ArrayList;

public class NonParsingAdapter extends BaseAdapter {
    private static String TAG = "phptest";
    private Context context;
    private static String IP_ADDRESS = "192.168.35.237";
    private Activity activity;
    private OnItemClick listener;

    ArrayList<PublicDataList> publicDataLists = new ArrayList<PublicDataList>();

    String servID; //선택한 정책의 서비스 아이디 저장 변수

    // NonParsingAdapter 생성자
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
        Context context = parent.getContext();
        final ViewHolder holder;//아이템 내 view들을 저장할 holder 생성

        //publicDataLists 아이템들을 publicDataList_item에 대입
        final PublicDataList publicDataList_item = publicDataLists.get(i);


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

        holder.list_text_name.setText(publicDataList_item.getServNm()); //list_text_name에 서비스제목을 set함
        holder.list_text_content.setText(publicDataList_item.getServDgst()); //list_text_content에 서비스설명을 set함


        //각 정책(리니어레이아웃) 클릭 시 해당 서비스 아이디를 가져와서 onClick 함수 호출
        //FavoriteActivity와 연결
        LinearLayout select_item = (LinearLayout) view.findViewById(R.id.select_item);
        select_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servID = publicDataList_item.getServID();
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