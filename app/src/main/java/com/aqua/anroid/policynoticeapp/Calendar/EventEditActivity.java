package com.aqua.anroid.policynoticeapp.Calendar;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import com.aqua.anroid.policynoticeapp.LocalIp;
import com.aqua.anroid.policynoticeapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class EventEditActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    String IP_ADDRESS;
    private static String EDITTAG = "editphp";
    private static String SAVETAG = "savephp";
    private static String AlarmTAG = "alarm";

    private EditText eventTitleET;
    private TextView startDateTV, endDateTV;
    private Button eventDatePickerBtn, eventSaveBtn, eventAlarmBtn, eventTimeBtn;

    Event event = new Event();

    public String alarmActive = "0" ;

    static int result; // 수정인지 새로 생성인지 구별
    String userID;
    String editTitle,editStartdate,editEnddate;
    String passedEventID;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

/*

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

*/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        IP_ADDRESS = ((LocalIp)getApplication()).getIp();

        /*

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
        Log.v("Alarmstart", mCalender.getTime().toString());

        */

        initWidgets();  // 초기화
        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");

        checkForEditEvent();


        //FavoriteAdapter에서 받아온 일정제목과 마감일자
        Intent intent = getIntent();
        String favorite_title = intent.getStringExtra("title");
        String favorite_CloseDt =  intent.getStringExtra("CloseDate");

        //즐겨찾기에서 받아온 정보를 캘린더에 추가
        if(favorite_title!=null && favorite_CloseDt!=null){
            long nowDate = System.currentTimeMillis();
            String getTime = sdf.format(nowDate);

            //yyyy-MM-dd 형식으로 변환
            favorite_CloseDt = "20"+favorite_CloseDt.substring(0,2)+"-"+favorite_CloseDt.substring(2,4)
                      +"-"+favorite_CloseDt.substring(4,6);

            eventTitleET.setText(favorite_title);
            startDateTV.setText(getTime);
            endDateTV.setText(favorite_CloseDt);
        }


        if(alarmActive.equals("1")) {
        //    setAlarm();
//            startAlarm();
        }

        eventTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TImepicker();
                timePicker.show(getSupportFragmentManager(),"timepick");
            }
        });

        eventAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmActive.equals("0")) {
                    eventAlarmBtn.setText("알림 on");
//                    eventAlarmBtn.setBackground(Color.parseColor("#FFE91C")); 색변경..?
                    alarmActive = "1";
                    Log.e("alarm", alarmActive);
                    Toast.makeText(EventEditActivity.this.getApplicationContext(),"알림 설정이 되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else {
                    eventAlarmBtn.setText("알림 Off");
                    alarmActive = "0";
                    Log.e("alarm", alarmActive);
                    Toast.makeText(EventEditActivity.this.getApplicationContext(),"알림 설정을 해제했습니다.",Toast.LENGTH_SHORT).show();

                }
                Log.e("alarmActive", String.valueOf(alarmActive));

/*
                notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                mCalender = new GregorianCalendar();
                Log.v("HelloAlarmActivity", mCalender.getTime().toString());

*/


            }
        });
    }

    //시간 정하면 호출되는 메소드
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        Log.d("alert", "alert set");
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
//        updateTimeText(c);

        startAlarm(c);
    }

    private void startAlarm(Calendar c){
        Log.d(AlarmTAG, "##start Alarm##");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(EventEditActivity.this, AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EventEditActivity.this, 1, intent, 0);

      /*  String from = "2020-05-27 14:08:00"; //임의로 날짜와 시간을 지정

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);*/

        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE,1);
        }



        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

/*

    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(EventEditActivity.this, AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EventEditActivity.this, 0, receiverIntent, PendingIntent.FLAG_IMMUTABLE);

        String from = "2022-06-09 13:45:00"; //임의로 날짜와 시간을 지정

        //날짜 포맷을 바꿔주는 소스코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
    }

*/


    private void initWidgets()
    {
        eventTitleET = findViewById(R.id.eventTitleET);
        startDateTV = findViewById(R.id.startDateTV);
        endDateTV = findViewById(R.id.endDateTV);
        eventDatePickerBtn = findViewById(R.id.eventDatePickerBtn);
        eventSaveBtn = findViewById(R.id.eventSaveBtn);
        eventAlarmBtn = findViewById(R.id.eventAlarmBtn);
        eventTimeBtn = findViewById(R.id.eventTimeBtn);
    }


    // 이벤트 수정인지 체크하는 함수
    public void checkForEditEvent(){
        Intent previousIntent = getIntent();
        Intent intent = getIntent();
        passedEventID =  previousIntent.getStringExtra(Event.Event_EDIT_EXTRA);

        // result: 같은 ID 존재한다면 = 1, 아니면 0
        result = CalendarActivity.eventsForID(passedEventID);

        // 수정(result=1)일 경우
        if(result == 1)
        {
            Log.e("intentresult" , String.valueOf(result));

            // EventAdpater에서 Key를 통해 전송한 데이터를 받는다
            editTitle = intent.getStringExtra("title");
            editStartdate =intent.getStringExtra("startdate");
            editEnddate = intent.getStringExtra("enddate");
            alarmActive = intent.getStringExtra("alarmactive");

            // 수정할 리스트뷰 정보 가져옴
            eventTitleET.setText(editTitle);
            startDateTV.setText(editStartdate);
            endDateTV.setText(editEnddate);

            if(alarmActive.equals("0")) {
                eventAlarmBtn.setText("알림 OFF");
                Log.e("alarm", alarmActive);

            }
            else {
                eventAlarmBtn.setText("알림 ON");
                Log.e("alarm", alarmActive);

            }

        }
    }

    //저장 버튼 클릭시 Action
    public void saveEventBtn(View view) {

        // 새로 생성(result=0)
        if(result != 1) {
            // 입력한 데이터를 변수에 담는다
            String eventTitle = eventTitleET.getText().toString();
            String eventStartDate = startDateTV.getText().toString();
            String eventEndDate = endDateTV.getText().toString();
            Log.e("result(0)_save..", "save data..");

            // ID 랜덤 생성
            Random random = new Random();
            String eventId = random.ints(48, 123)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(8)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            // DB에 이벤트 저장하는 함수 호출
            InsertEvent inserttask = new InsertEvent();
            inserttask.execute("http://" + IP_ADDRESS + "/event_insert.php", userID, eventId, eventTitle, eventStartDate, eventEndDate, alarmActive);

        }

        //수정(result=1)
        if(result == 1)
        {
            // 새로 입력한 데이터를 변수에 담는다
            String updateTitle = eventTitleET.getText().toString();
            String updateStartDate = startDateTV.getText().toString();
            String updateEndDate = endDateTV.getText().toString();

            // DB에 이벤트 ID로 새로 입력한 데이터를 업데이트하는 함수 호출
            UpdateEvent updatetask = new UpdateEvent();
            updatetask.execute("http://" + IP_ADDRESS + "/event_update.php",
                    updateTitle, updateStartDate,updateEndDate,alarmActive, passedEventID);
        }

        startActivity(new Intent(this, CalendarActivity.class));
    }

    //날짜 선택
    public void datepickerAction(View view) {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("날짜를 선택해주세요");
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        materialDateBuilder.setSelection(today);

        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "Date_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startdate = new Date();
                        Date enddate = new Date();

                        startdate.setTime(selection.first); //첫번째로 선택한 날짜를 표시
                        enddate.setTime(selection.second); //두번째로 선택한 날짜를 표시

                        String startdateString = simpleDateFormat.format(startdate);
                        String enddateString = simpleDateFormat.format(enddate);

                        startDateTV.setText(startdateString);
                        endDateTV.setText(enddateString);

                        //선택한 날짜 객체 저장
                        event.setStartdate(startdateString);
                        event.setEnddate(enddateString);
                    }
                });

    }


    // DB에 이벤트 저장
    class InsertEvent extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(EventEditActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
//            mTextViewResult.setText(result);
            Log.d(SAVETAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String eventId = (String) params[2];
            String eventTitle = (String) params[3];
            String eventStartDate = (String) params[4];
            String eventEndDate = (String) params[5];
            String eventAlarmActive = (String) params[6];

            String serverURL = (String) params[0];
            String postParameters = "userID=" + userID + "&ID=" + eventId +
                    "&title=" + eventTitle + "&startdate=" + eventStartDate + "&enddate=" + eventEndDate + "&alarmactive=" + eventAlarmActive;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(SAVETAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {

                Log.d(SAVETAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }





    // DB에 저장되어있는 이벤트 수정(passedEventID 로 데이터 찾아 업데이트)
    class UpdateEvent extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(EDITTAG, "update_event "+ result);
        }

        @Override
        protected String doInBackground(String... params) {
            String eventTitle = (String) params[1];
            String eventStartDate = (String) params[2];
            String eventEndDate = (String) params[3];
            String alarmBtnActive = (String) params[4];
            String passedEventID = (String) params[5];

            //PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비
            //POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않는다.
            String serverURL = (String)params[0];

            //HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 한다,
            //전송할 데이터는 '이름=값' 형식이며 여러개를 보내야 할 경우에는 항목 사이에 &를 추가한다.
            //여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 된다.
            String postParameters = "title=" + eventTitle + "&startdate=" + eventStartDate + "&enddate=" + eventEndDate +
                    "&alarmactive=" + alarmBtnActive + "&passedEventID=" + passedEventID;

            try {

                //HttpURLConnection 클래스를 사용하여 POST방식으로 데이터를 전송
                URL url = new URL(serverURL);   //주소가 저장된 변수를 입력
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생
                httpURLConnection.setConnectTimeout(5000);  //5초안에 연결이 안되면 예외가 발생
                httpURLConnection.setRequestMethod("POST"); //요청방식으로 POST로 한다.
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                //전송할 데이터가 저장된 변수를 이곳에 입력한다. 인코딩을 고려해야한다.
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                //응답 읽기
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(EDITTAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                //StringBuilder를 사용하여 수신되는 데이터를 저장
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();

                return sb.toString();   //저장된 데이터를 스트링으로 변환하여 리턴턴


            } catch (Exception e) {

                Log.d(EDITTAG, "UpdateEvent: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
