package com.aqua.anroid.policynoticeapp.Calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aqua.anroid.policynoticeapp.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private final ArrayList<LocalDate> days;
    public final View parentView;
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days)
    {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
        this.days = days;
    }

    @Override
    public void onClick(View view)
    {
        // 일 텍스트 가져오기
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }



}

