package com.aqua.anroid.policynoticeapp.User;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "register";

    private EditText et_id, et_pass;
    private Spinner sp_lifearray, sp_trgterIndvdlArray, sp_area;
    private Button btn_register, btn_id_check;
    private TextView register_state_result, check_id;
    private int check_cnt=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티 시작시 처음으로 실행되는 생명주기!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 아이디 값 찾아주기
        et_id = findViewById(R.id.join_id);
        et_pass = findViewById(R.id.join_pw);
        sp_lifearray = findViewById(R.id.join_lifearray);
        sp_trgterIndvdlArray = findViewById(R.id.join_trgterIndvdlArray);
        sp_area = findViewById(R.id.join_area);
        check_id = findViewById(R.id.check_id);
        btn_id_check= findViewById(R.id.chekc_id_btn);
        register_state_result = (TextView)findViewById(R.id.check_pass);


        register_state_result.setMovementMethod(new ScrollingMovementMethod());


        // 회원가입 버튼 클릭 시 수행
        btn_register = findViewById(R.id.join_savebtn);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userLifearray = sp_lifearray.getSelectedItem().toString();
                String userTrgterIndvdl = sp_trgterIndvdlArray.getSelectedItem().toString();
                String userArea = sp_area.getSelectedItem().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/register.php", userID,userPass,userLifearray,userTrgterIndvdl, userArea);

                if(check_cnt==0){
                    check_id.setText("아이디 중복확인을 해주세요");
                }
            }
        });

        btn_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check_cnt = 1;
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                InsertDataID task = new InsertDataID();
                task.execute("http://" + IP_ADDRESS + "/id_check.php", userID, userPass);


            }
        });
    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RegisterActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if(result.equals("가입되었습니다")){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
                alertDialogBuilder
                        .setMessage( "가입되었습니다")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }


            else if(result.equals("아이디를 입력하세요")){
               check_id.setText(result);
               register_state_result.setText("");
            }
            else if(result.equals("비밀번호를 입력하세요"))
                register_state_result.setText(result);

            else{
                register_state_result.setText(result);
            }



            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String userPass = (String)params[2];
            String userLifearray = (String)params[3];
            String userTrgterIndvdl = (String)params[4];
            String userArea = (String)params[5];


            //PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비
            //POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않는다.
            String serverURL = (String)params[0];

            //HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 한다,
            //전송할 데이터는 '이름=값' 형식이며 여러개를 보내야 할 경우에는 항목 사이에 &를 추가한다.
            //여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 된다.
            String postParameters = "userID=" + userID + "& userPass=" + userPass + "& userLifearray=" + userLifearray +
                    "& userTrgterIndvdl=" + userTrgterIndvdl +"& userArea=" + userArea;


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

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    class InsertDataID extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RegisterActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if(result.equals("이미 사용중인 아이디입니다")
                    || result.equals("아이디를 입력하세요")
                    || result.equals("사용가능한 아이디입니다")){
                check_id.setText(result);
            }

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String userID = (String)params[1];
            String userPass = (String)params[2];


            //PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비
            //POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않는다.
            String serverURL = (String)params[0];

            //HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 한다,
            //전송할 데이터는 '이름=값' 형식이며 여러개를 보내야 할 경우에는 항목 사이에 &를 추가한다.
            //여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 된다.
            String postParameters = "userID=" + userID + "&userPass=" + userPass;


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

                Log.d(TAG, "InsertDataID: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}