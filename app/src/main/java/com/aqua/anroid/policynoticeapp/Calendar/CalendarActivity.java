package com.aqua.anroid.policynoticeapp.Calendar;


import static com.aqua.anroid.policynoticeapp.Calendar.CalendarUtils.daysInMonthArray;
import static com.aqua.anroid.policynoticeapp.Calendar.CalendarUtils.monthYearFromDate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aqua.anroid.policynoticeapp.Favorite.FavoriteActivity;
import com.aqua.anroid.policynoticeapp.Favorite.FavoriteData;
import com.aqua.anroid.policynoticeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";
    private static final String TAG_JSON="root";

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    String userID;
    String mJsonString;

    public static ArrayList<Event> eventsList = new ArrayList<>(); //이벤트 목록
    public static ArrayList<String> titles= new ArrayList<>();
    public static ArrayList<String> startdates= new ArrayList<>();
    public static ArrayList<String> enddates= new ArrayList<>();
    static ArrayList<Event> items= new ArrayList<>();
    ArrayList<Event> dailyEvents;
    EventAdapter eventAdapter;
    static ArrayList<Event> events;

    String title;
    String startdate;
    String enddate;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");

        GetData task = new GetData();
        task.execute(userID);

        initWidgets(); //id 통해 목록 찾음
        //loadFromDBToMemory();

        CalendarUtils.selectedDate = LocalDate.now(); //현재 날짜
        setMonthView(); //화면 설정
        setOnClickListener();

    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = (TextView) findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    /* db
    private void loadFromDBToMemory()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instance
    }*/

    private void setMonthView()
    {
        Log.d("monthYearFromDate(CalendarUtils.selectedDate", monthYearFromDate(CalendarUtils.selectedDate));

        //년 월 텍스트뷰
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));

        //해당 월 날짜 가져오기
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);

        //레이아웃 설정, 열 7개
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);


        setEventAdapter();
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        //현재 날짜로 변경하는 함수를 호출하여
        //CalendarUtils.selectedDate 가 날짜와 같도록 설정
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    // 이벤트 Adapter 제공
    private void setEventAdapter()
    {
        //ID 로 목록 찾고 리스트 호출
        dailyEvents = eventsForDate(CalendarUtils.selectedDate);
//        eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents, items);
        eventAdapter = new EventAdapter(this, this, dailyEvents);

        eventListView.setAdapter(eventAdapter);

//        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), eventsList.get(i).title, Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    // event item 클릭 시
    private void setOnClickListener() {
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = (Event) eventListView.getItemAtPosition(position);

                Intent eventEditIntent = new Intent(getApplicationContext(), EventEditActivity.class);
                eventEditIntent.putExtra(Event.Event_EDIT_EXTRA, selectedEvent.getId());
                startActivity(eventEditIntent);

                Toast.makeText(getApplicationContext(), eventsList.get(position).title, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(CalendarActivity.this,EventEditActivity.class));
    }

    // 재개될 때마다 다시 로드되도록 EventAdapter 호출
    //Activity가 사용자와 상호작용하기 바로 전에 호출됨
    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdapter();
    }

    // 주어진 날짜에 대한 모든 이벤트 반환
    public static ArrayList<Event> eventsForDate(LocalDate date) {
//        ArrayList<Event> events = new ArrayList<>();
        events = new ArrayList<>();

        for (int k = 0; k<eventsList.size(); k++)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String selectDate1 = date.toString();   //현재누른날짜
            String startDate1 = eventsList.get(k).getStartdate();  //시작날짜
            String endDate1 = eventsList.get(k).getEnddate();  //종료날짜
            Date selectDate = null;

            Log.d("시작날짜", startDate1);


            try {
                selectDate = dateFormat.parse(selectDate1);

                Date startDate  = dateFormat.parse(startDate1);
                Date endDate    = dateFormat.parse(endDate1);

                int result1 = selectDate.compareTo(startDate);       // curr > d1
                int result2 = selectDate.compareTo(endDate);

                // 조건이 맞을때
                if((result1>=0)&&(result2<=0))  //선택한 날짜가 시작날짜랑 같거나 크고 & 앤드날짜보다 작거나 같으면
                    events.add(eventsList.get(k));   //items.get(k)가  events 어레이에 더해짐
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return events;
    }

    public static Event getEventForID(int passedEventID)
    {
        for(Event event : eventsList)
        {
            if(event.getId() == passedEventID)
                return event;
        }
        return null;
    }

    class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CalendarActivity.this,
                    "Please Wait", null, true, true);


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result != null) {
                mJsonString = result;

                showResult();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://10.0.2.2/event_query.php";
            String postParameters = "userID=" + searchKeyword1;

            Log.d(TAG, "userID_event : " + searchKeyword1);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }


        public void showResult() {

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                eventsList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);
                    Log.d(TAG, "JSONObject : " + item);

                    title = item.getString("title");
                    startdate = item.getString("startdate");
                    enddate = item.getString("enddate");

                    eventsList.add(new Event(title,startdate,enddate));

                    Log.d(TAG, "eventsList : " + eventsList.toString());


                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult_member : ", e);
            }

        }
    }

}