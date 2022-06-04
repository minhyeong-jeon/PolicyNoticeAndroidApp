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

import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.NonUser.NonmemberActivity;

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
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "userID";
    private static final String TAG_COUNTRY ="userPass";

    EditText edit_id, edit_pw;
    private Button btn_login, btn_register, btn_nonmember;
    TextView state_result;
    String mJsonString;
    CheckBox login_state;

    String userID;
    String userPass;

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

        btn_nonmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NonmemberActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData task = new GetData();
                task.execute( edit_id.getText().toString(), edit_pw.getText().toString());
            }
        });



    }



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

            if (result == null){

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
            Log.d(TAG, "로그인id - " + searchKeyword1);
            Log.d(TAG, "로그인pass - " + searchKeyword2);


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

                //String id = item.getString(TAG_ID);
                userID = item.getString(TAG_NAME);
                userPass = item.getString(TAG_COUNTRY);

                HashMap<String,String> hashMap = new HashMap<>();

                //hashMap.put(TAG_ID, id);
                hashMap.put(TAG_NAME, userID);
                hashMap.put(TAG_COUNTRY, userPass);

                Log.d(TAG, "hashMap : " + hashMap.toString());


                String test_id = edit_id.getText().toString();

                preferences = getSharedPreferences("userID", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userID",test_id);
                editor.commit();

                Intent intent = new Intent(this, MemberActivity.class);
                startActivity(intent);


                Log.d(TAG, "intent보내는값_login : " + test_id);

                state_result.setText("");
                edit_id.setText("");
                edit_pw.setText("");

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult_login : ", e);
        }

    }

}