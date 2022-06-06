package com.aqua.anroid.policynoticeapp.NonUser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.aqua.anroid.policynoticeapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NonChatbotInquiryActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    private Spinner inquiry_type;
    private EditText inquiry_title;
    private EditText inquiry_email;
    private EditText inquiry_content;


    String[] list_type = {"선택안함","서비스 정보","앱 이용 방법","앱 개선 요청","시스템 오류","기타"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_chatbot_inquiry);
        inquiry_type = (Spinner) findViewById(R.id.inquiry_type);
        inquiry_title = (EditText) findViewById(R.id.inquiry_title);
        inquiry_email = (EditText) findViewById(R.id.inquiry_email);
        inquiry_content = (EditText) findViewById(R.id.inquiry_content);


        ArrayAdapter<String> lifeArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,list_type);
        lifeArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inquiry_type.setAdapter(lifeArray_adapter);
        inquiry_type.setSelection(0,false);

        ImageView backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NonChatbotInquiryActivity.this,NonChatbotMainActivity.class);
                startActivity(intent);

            }
        });


        Button inquirybtn = (Button) findViewById(R.id.inquirybtn);
        inquirybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = inquiry_type.getSelectedItem().toString();
                String title = inquiry_title.getText().toString();
                String email = inquiry_email.getText().toString();
                String content = inquiry_content.getText().toString();


                InsertInquiryData Ninquiry = new InsertInquiryData();
                Ninquiry.execute("http://" + IP_ADDRESS + "/non_inquiry.php",type,title,email,content);
                Log.d("type",type);
                Log.d("title",title);
                Log.d("email",email);
                Log.d("content",content);


                /*inquiry_title.setText("");
                inquiry_email.setText("");
                inquiry_content.setText("");
*/

            }
        });
        // Toast.makeText(this, "1대1문의", Toast.LENGTH_SHORT).show();
    }
    class InsertInquiryData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*progressDialog = ProgressDialog.show(Chatbot_Inquiry.this,
                    "Please Wait", null, true, true);*/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            //mTextViewResult.setText(result);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    NonChatbotInquiryActivity.this);

            //alertDialogBuilder.setTitle("Title Dialog");
            alertDialogBuilder
                    .setMessage(result)
                    .setCancelable(true)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int arg1) {
                                    // Handle Positive Button
                                    if(result.equals("문의가 성공적으로 등록되었습니다.")) {
                                        Intent intent = new Intent(getApplicationContext(), NonChatbotMainActivity.class);
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
            String type = (String)params[1];
            String title = (String)params[2];
            String email = (String)params[3];
            String content = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "&type=" + type + "&title=" + title + "&email=" + email + "&content=" +content;


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
