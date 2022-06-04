package com.aqua.anroid.policynoticeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.User.MainActivity;
import com.aqua.anroid.policynoticeapp.User.MemberUpdateActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SettingActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "register";

    ImageView menubtn;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");

        menubtn = findViewById(R.id.menubtn);

        //메뉴버튼 클릭 시 메뉴화면으로 이동
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });

    }
    public void info_update(View view){
        Intent intent = new Intent(SettingActivity.this, MemberUpdateActivity.class);
        startActivity(intent);
    }

    public void signout(View view){
        DeleteData task = new DeleteData();
        task.execute(userID);

        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin",MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();
        spEdit.clear();
        spEdit.commit();

    }
    public void logout(View view){
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    class DeleteData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SettingActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];

            String serverURL = "http://10.0.2.2/delete.php";
            String postParameters = "userID=" + searchKeyword1;


            //PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비
            //POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않는다.
            //String serverURL = (String)params[0];

            //HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 한다,
            //전송할 데이터는 '이름=값' 형식이며 여러개를 보내야 할 경우에는 항목 사이에 &를 추가한다.
            //여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 된다.

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

                Log.d(TAG, "DeleteData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
