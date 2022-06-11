package com.aqua.anroid.policynoticeapp.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.API_Data.PublicActivity;
import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.NonUser.NonPublicActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "phpquerytest";

    private static final String TAG_JSON="root";

    EditText edit_id, edit_pw;  //로그인 시 입력한 아이디와 비밀번호 저장 변수
    private Button btn_login, btn_register, btn_nonmember;
    TextView state_result;  //로그인 결과 저장 변수
    String mJsonString;

    String userID;      //유저 아이디 저장 변수
    String userPass;    //유저 비밀번호 저장 변수

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.login_btn);
        btn_register = findViewById(R.id.login_joinbtn);
        btn_nonmember = findViewById(R.id.login_nmemberbtn);
        state_result = (TextView) findViewById(R.id.login_result);
        edit_id = (EditText) findViewById(R.id.login_id);
        edit_pw = (EditText) findViewById(R.id.login_pw);



        // 회원가입 버튼을 클릭 시 수행
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //비회원 버튼 클릭 시
        btn_nonmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NonPublicActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭 시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아이디와 비밀번호를 인자로하여 DB에서 정보를 Get
                GetData task = new GetData();
                task.execute( edit_id.getText().toString(), edit_pw.getText().toString());
            }
        });



    }

    //DB에서 데이터를 가져옴
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        //에러가 있는 경우 에러메시지를 보여주고 아니면 JSON을 파싱하여 화면에 보여주는 showResult메소드를 호출
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            state_result.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){    //에러메시지 출력
                state_result.setText(errorString);
                Log.d(TAG, "result - " + state_result);

            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            //POST 방식 HTTP 통신의 아규먼트로 하여 서버에 있는 PHP파일 실행
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://10.0.2.2/login.php";
            String postParameters = "userID=" + searchKeyword1 + "&userPass=" + searchKeyword2;

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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                //StringBuilder를 사용하여 PHP가 에코한 문자열을 저장하고 스트링으로 변환하여 리턴
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
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
    }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                userID = item.getString("userID");  //DB에서 가져온 userID를 userID에 대입
                userPass = item.getString("userPass"); //DB에서 가져온 userPass를 userPass에 대입

                String user_id = edit_id.getText().toString();

                //sharedPreference에 userID 저장
                preferences = getSharedPreferences("userID", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userID",user_id);
                editor.commit();

                //검색화면으로 이동
                Intent intent = new Intent(this, PublicActivity.class);
                startActivity(intent);

                //로그인결과, 아이디, 비밀번호 초기화
                state_result.setText("");
                edit_id.setText("");
                edit_pw.setText("");

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult_login : ", e);
        }

    }

}