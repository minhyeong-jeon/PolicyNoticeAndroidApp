package com.aqua.anroid.policynoticeapp.Calendar;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aqua.anroid.policynoticeapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.155555555); //월별 달력


        return new CalendarViewHolder(view, onItemListener, days);
    }

    // 생성된 ViewHolder 에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        //날짜 변수에 담기
        final LocalDate date = days.get(position);

        //ID 로 목록 찾고 리스트 호출
        ArrayList<Event> dailyEvents = CalendarActivity.eventsForDate(CalendarUtils.selectedDate);



        if (date == null)
            //날짜가 null인 경우 홀더 날짜를 설정
            holder.dayOfMonth.setText("");

        else {
            //그렇지 않으면 날짜 넣기
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

            //선택한 날짜 회색으로 표시
            if (date.equals(CalendarUtils.selectedDate)) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }

            //주말 색상 지정(토요일: BLUE, 일요일: RED)
            if ((position + 1) % 7 == 0) {
                holder.dayOfMonth.setTextColor(Color.BLUE);
            } else if (position == 0 || position % 7 == 0) {
                holder.dayOfMonth.setTextColor(Color.RED);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                for (int i = 0;CalendarActivity.eventsList.size()>i ; i++) {

                    Event CalendarDate1 = CalendarActivity.eventsList.get(i);
                    Date curr = dateFormat.parse(date.toString());

                    Date d1 = dateFormat.parse(CalendarDate1.startdate);
                    Date d2 = dateFormat.parse(CalendarDate1.enddate);


                    int result1 = curr.compareTo(d1);       // curr > d1
                    int result2 = curr.compareTo(d2);

                    // 조건이 맞을때
                    if ((result1 >= 0) && (result2 <= 0))
                        holder.parentView.setBackgroundColor(Color.GREEN);
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }


        }

    }
    // 전체 데이터 개수 return
    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        //해당 변수 이름은 현재날짜로 변경됨
        void onItemClick(int position, LocalDate date);
    }


}
