package com.aqua.anroid.policynoticeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Chatbot_Inquiry extends AppCompatActivity {

    String IP_ADDRESS;
    private static String TAG = "phptest";

    private Spinner inquiry_type;
    private EditText inquiry_title;
    private EditText inquiry_email;
    private EditText inquiry_content;

    String userID; //선택한 정책의 서비스 아이디 저장 변수

    String[] list_type = {"선택안함","서비스 정보","앱 이용 방법","앱 개선 요청","시스템 오류","기타"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_inquiry);
        IP_ADDRESS = ((LocalIp)getApplication()).getIp();

        inquiry_type = (Spinner) findViewById(R.id.inquiry_type);
        inquiry_title = (EditText) findViewById(R.id.inquiry_title);
        inquiry_email = (EditText) findViewById(R.id.inquiry_email);
        inquiry_content = (EditText) findViewById(R.id.inquiry_content);

        // userID 기기에 저장
        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");


        //문의유형 스피너 어뎁터
        ArrayAdapter<String> lifeArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,list_type);
        lifeArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inquiry_type.setAdapter(lifeArray_adapter);
        inquiry_type.setSelection(0,false);

        ImageView backBtn = findViewById(R.id.backbtn);

        //뒤로가기 버튼 클릭 시 이동
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatbot_Inquiry.this,Chatbot_Main.class);
                startActivity(intent);

            }
        });

        //문의등록 클릭 시 해당문의가 inquiry 테이블에 저장
        Button inquirybtn = (Button) findViewById(R.id.inquirybtn);
        inquirybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //입력 데이터를 각 변수에 저장
                String title = inquiry_title.getText().toString();
                String email = inquiry_email.getText().toString();
                String content = inquiry_content.getText().toString();
                String type = inquiry_type.getSelectedItem().toString();

                //DB에 문의내용을 저장하는 함수 호출
                InsertInquiryData inquiry = new InsertInquiryData();
                inquiry.execute("http://" + IP_ADDRESS + "/inquiry.php",userID,type,title,email,content);
                Log.d("userID",userID);
                Log.d("type",type);
                Log.d("title",title);
                Log.d("email",email);
                Log.d("content",content);

            }
        });
    }

    //DB에 문의 저장
    class InsertInquiryData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    Chatbot_Inquiry.this);

            alertDialogBuilder
                    .setMessage(result)
                    .setCancelable(true)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                //문의 등록 성공 시 AlertDialog 출력 후 문의 메인화면으로 이동
                                public void onClick(DialogInterface dialog, int arg1) {
                                    // Handle Positive Button
                                    if(result.equals("문의가 성공적으로 등록되었습니다.")) {
                                        Intent intent = new Intent(getApplicationContext(), Chatbot_Main.class);
                                        startActivity(intent);
                                    }


                                }
                            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {
            String userID = (String)params[1];
            String type = (String)params[2];
            String title = (String)params[3];
            String email = (String)params[4];
            String content = (String)params[5];

            String serverURL = (String)params[0];
            String postParameters = "userID=" + userID + "&type=" + type + "&title=" + title + "&email=" + email + "&content=" +content;

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

                Log.d(TAG, "InsertInquiryData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}