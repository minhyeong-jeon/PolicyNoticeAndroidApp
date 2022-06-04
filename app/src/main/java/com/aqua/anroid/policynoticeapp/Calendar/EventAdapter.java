package com.aqua.anroid.policynoticeapp.Calendar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.aqua.anroid.policynoticeapp.Favorite.FavoriteAdapter;
import com.aqua.anroid.policynoticeapp.R;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

/* 이벤트 유형의 배열 어댑터 확장 */
public class EventAdapter extends BaseAdapter{

    private static String TAG = "phptest";

    ArrayList<Event> events= new ArrayList<Event>();
    Context context;
    Activity activity;
//    public EventAdapter(@NonNull Context context, ArrayList<Event> events, ArrayList<Event> items)
//    {
//        super(context, 0, events); //super 호출 위해 resource 0 지정
//        this.context = context;
//        this.events = events;
//        this.items = items;
//    }

    public EventAdapter(Context context, Activity activity, ArrayList<Event> events)
    {
        this.context = context;
        this.activity = activity;
        this.events = events;

    }

    //뷰홀더 추가
    class ViewHolder {
        TextView eventTitleTV;
        TextView eventStartDateTV;
        TextView eventEndDateTV;
    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return events.size();
    }

    //지정한 위치(i)에 있는 데이터 리턴턴
    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    //지정한 위치(i)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴
    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Log.d("어뎁터 갱신", "어뎁터 갱신");

        LocalDate date;
//        ViewHolder holder = new ViewHolder();
        final ViewHolder holder;//아이템 내 view들을 저장할 holder 생성

        final Event event_item = events.get(position);

        Log.d(TAG, "events_adapter : " + events.toString());


        //이벤트 항목 가져옴
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.event_cell, parent, false);

            holder = new ViewHolder();

            holder.eventTitleTV = (TextView) convertView.findViewById(R.id.eventTitleTV);
            holder.eventStartDateTV = (TextView) convertView.findViewById(R.id.eventStartDateTV);
            holder.eventEndDateTV = (TextView) convertView.findViewById(R.id.eventEndDateTV);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

            holder.eventTitleTV.setText(event_item.getTitle());
            holder.eventStartDateTV.setText(event_item.getStartdate());
            holder.eventEndDateTV.setText(event_item.getEnddate());




        return convertView;
    }
}
