package com.aqua.anroid.policynoticeapp.Calendar;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.aqua.anroid.policynoticeapp.Favorite.FavoriteAdapter;
import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.User.MemberUpdateActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

import java.util.Date;

public class EventEditActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    private EditText eventTitleET;
    private TextView eventDateTV, eventTimeTV, startDateTV, endDateTV;
    private Button deleteEventBtn, eventDatePickerBtn;

    public Event selectedEvent;
    private LocalTime time; // 현지 시간으로 시간 호출
    Event event = new Event();

    String userID;
    private static int nNumberID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now(); // 지금 현지 시간으로 초기화

        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");

        checkForEditEvent();

    }

    private void initWidgets()
    {
        eventTitleET = findViewById(R.id.eventTitleET);
        deleteEventBtn = findViewById(R.id.deleteEventBtn);
        eventDatePickerBtn = findViewById(R.id.eventDatePickerBtn);

        startDateTV = findViewById(R.id.startDateTV);
        endDateTV = findViewById(R.id.endDateTV);

    }

    private void checkForEditEvent()
    {
        Intent previousIntent = getIntent();

        int passedEventID = previousIntent.getIntExtra(Event.Event_EDIT_EXTRA, -1);
        selectedEvent = CalendarActivity.getEventForID(passedEventID);

        // 이벤트 편집이 있음을 의미하는 선택된 이벤트 찾았을 때
        if (selectedEvent != null)
        {
            eventTitleET.setText(selectedEvent.getTitle());
            startDateTV.setText(selectedEvent.getStartdate());
            endDateTV.setText(selectedEvent.getEnddate());

        }
        else
        {
            //새로운 이벤트 생성 시 삭제 버튼 숨김
            deleteEventBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void saveEventAction(View View)
    {
        String eventTitle = eventTitleET.getText().toString();
        String eventStartDate = startDateTV.getText().toString();
        String eventEndDate = endDateTV.getText().toString();


        if (selectedEvent == null) {
            //int id = Event.eventsList.size();
//            nNumberID++;

            InsertData task = new InsertData();
            task.execute("http://" + IP_ADDRESS + "/event_insert.php", userID, eventTitle, eventStartDate,eventEndDate);


        }
        // 편집 모드
        else
        {
            //선택한 메모에 제목을 가져와 동일하게 지정
            selectedEvent.setTitle(eventTitle);
            selectedEvent.setStartdate(eventStartDate);
            selectedEvent.setEnddate(eventEndDate);

            String updateTitle = eventTitleET.getText().toString();
            String updateStartDate = startDateTV.getText().toString();
            String updateEndDate = endDateTV.getText().toString();

            UpdateEventData task = new UpdateEventData();
            task.execute("http://" + IP_ADDRESS + "/event_update.php",userID,updateTitle,updateStartDate,updateEndDate);

            // db 업데이트 하기
            // ...
        }
        startActivity(new Intent(this, CalendarActivity.class));

    }

    // 이벤트 삭제
    public void deleteEventAction(View view) {
        // 새 날짜를 호출하여 삭제된 시간을 제공
//        CalendarActivity.eventsList.remove(selectedEvent);
        //db 설정
        //db 업데이트

        String deleteTitle = eventTitleET.getText().toString();
        String deleteStartDate = startDateTV.getText().toString();
        String deleteEndDate = endDateTV.getText().toString();

        DeleteEventData task = new DeleteEventData();
        task.execute(userID,deleteTitle,deleteStartDate,deleteEndDate);


        startActivity(new Intent(this, CalendarActivity.class));
    }

    //날짜 선택
    public void datepickerAction(View view) {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("날짜를 선택해주세요");
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        materialDateBuilder.setSelection(today);

        //미리 날짜 선택
//        builder.setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()));
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "Date_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startdate = new Date();
                        Date enddate = new Date();

                        startdate.setTime(selection.first);
                        enddate.setTime(selection.second);

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

    //이벤트 저장 - InserData
    class InsertData extends AsyncTask<String, String, String> {
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
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String eventTitle = (String) params[2];
            String eventStartDate = (String) params[3];
            String eventEndDate = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "userID=" + userID + "&title=" + eventTitle + "&startdate=" + eventStartDate + "&enddate=" + eventEndDate;


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
                Log.d(TAG, "POST response code - " + responseStatusCode);

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

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    class UpdateEventData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = ProgressDialog.show(MemberUpdateActivity.this,
//                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//            progressDialog.dismiss();

            Log.d(TAG, "update_result "+ result);

//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


        }


        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String eventTitle = (String) params[2];
            String eventStartDate = (String) params[3];
            String eventEndDate = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "userID=" + userID + "&title=" + eventTitle + "&startdate=" + eventStartDate + "&enddate=" + eventEndDate;


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
                Log.d(TAG, "POST response code - " + responseStatusCode);

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

                Log.d(TAG, "UpdateData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    class DeleteEventData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }


        @Override
        protected String doInBackground(String... params) {



            String serverURL = "http://10.0.2.2/event_delete.php";

            String userID = (String)params[0];
            String eventTitle = (String) params[1];
            String eventStartDate = (String) params[2];
            String eventEndDate = (String) params[3];

            String postParameters = "userID=" + userID + "&title=" + eventTitle + "&startdate=" + eventStartDate + "&enddate=" + eventEndDate;



            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                // Log.d(TAG, "DeleteData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
