package com.aqua.anroid.policynoticeapp.Calendar;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/* 자주 사용하는 변수 정리*/
public class CalendarUtils
{
    public static LocalDate selectedDate; //선택한 날짜

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd", Locale.KOREAN);
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.KOREAN);
        return time.format(formatter);
    }

    // 월 년도
    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MMMM", Locale.KOREAN);
        return date.format(formatter);
    }


    //월 별 배열
    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        //해당 월 마지막 날짜 가져오기
        int daysInMonth = yearMonth.lengthOfMonth();

        //해당 월 첫 번째 날 가져오기
        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);

        //첫 번째 날 요일 가져오기
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        //날짜 생성
        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                // 선택한 날짜의 연도 가져오고, 선택된 날짜의 월을 가져와
                // i에서 요일을 뺸 다음 현재 날짜의 배열 목록 복사
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
        return  daysInMonthArray;
    }

}
