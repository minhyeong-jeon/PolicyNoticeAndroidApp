package com.aqua.anroid.policynoticeapp.Calendar;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Event implements Serializable
{
    //    public static ArrayList<Event> eventsList = new ArrayList<>(); //이벤트 목록
    public static String Event_EDIT_EXTRA = "eventEdit";


    public String ID;
    public String title;
    public String startdate;
    public String enddate;

    public String getAlarmactive() {
        return alarmactive;
    }

    public void setAlarmactive(String alarmactive) {
        this.alarmactive = alarmactive;
    }

    public String alarmactive;


    public Event(){}

    public Event (String ID, String title, String startdate, String enddate, String alarmactive)
    {
        this.ID = ID;
        this.title = title;
        this.startdate = startdate;
        this.enddate = enddate;
        this.alarmactive = alarmactive;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public String setTitle(String title) {
        this.title = title;
        return title;
    }

    public String getStartdate() {
        return startdate;
    }

    public String setStartdate(String startdate) {
        this.startdate = startdate;
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

}