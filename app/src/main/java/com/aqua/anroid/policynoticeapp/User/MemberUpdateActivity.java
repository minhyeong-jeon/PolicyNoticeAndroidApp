package com.aqua.anroid.policynoticeapp.User;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.API_Data.PublicActivity;
import com.aqua.anroid.policynoticeapp.LocalIp;
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

public class MemberUpdateActivity extends AppCompatActivity {
    String IP_ADDRESS;
    private static String TAG = "update";
    private static final String TAG_JSON="root";

    String mJsonString;

    //변경할 정보 저장 변수 선언
    EditText update_pass;
    Spinner update_lifearray;
    Spinner update_trgterIndvdlArray;
    Spinner update_area;

    TextView update_user_id;    //로그인 한 유저아이디 저장 변수
    Button saveBtn;
    ImageView backBtn;

    String[] lifeArray_items = {"선택안함", "영유아", "아동", "청소년", "청년","중장년", "노년", "임신·출산" };
    String[] trgterIndvdlArray_items = {"선택안함", "다문화·탈북민", "다자녀", "보훈대상자", "장애인", "저소득", "한부모·조손"};
    String[] area_items = { "선택안함", "지역무관", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberupdate);

        IP_ADDRESS = ((LocalIp)getApplication()).getIp();


        update_pass = findViewById(R.id.update_pw);
        update_lifearray = findViewById(R.id.update_lifearray);
        update_trgterIndvdlArray = findViewById(R.id.update_trgterIndvdlArray);
        update_area = findViewById(R.id.update_area);
        saveBtn = findViewById(R.id.update_savebtn);
        update_user_id = findViewById(R.id.user_id);
        backBtn = findViewById(R.id.backbtn);

        //뒤로가기 버튼 클릭 시 설정화면으로 이동
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemberUpdateActivity.this, SettingActivity.class);
                startActivity(intent);

            }
        });

        /*아디디 출력*/
        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        String userID  = sharedPreferences.getString("userID","");

        update_user_id.setText(userID);


        //생애주기 업데이트
        ArrayAdapter<String> update_lifeArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,lifeArray_items);
        update_lifeArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_lifearray.setAdapter(update_lifeArray_adapter);
        update_lifearray.setSelection(0,false);

        update_lifearray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //가구유형 업데이트
        ArrayAdapter<String> update_trgterIndvdlArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,trgterIndvdlArray_items);
        update_trgterIndvdlArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_trgterIndvdlArray.setAdapter(update_trgterIndvdlArray_adapter);
        update_trgterIndvdlArray.setSelection(0,false);

        update_trgterIndvdlArray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //지역 업데이트
        ArrayAdapter<String> update_area_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,area_items);
        update_area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_area.setAdapter(update_area_adapter);
        update_area.setSelection(0,false);

        update_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //기존 유저정보를 불러옴
        GetData task = new GetData();
        task.execute(update_user_id.getText().toString());

        //수정하기 버튼 클릭 시
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //수정한 정보를 변수에 대입
                String userPass = update_pass.getText().toString();
                String userLifearray = update_lifearray.getSelectedItem().toString();
                String userTrgterIndvdl = update_trgterIndvdlArray.getSelectedItem().toString();
                String userID = update_user_id.getText().toString();
                String userArea = update_area.getSelectedItem().toString();

                //수정된 정보를 인자로 하여 UpdateData 호출
                UpdateData task = new UpdateData();
                task.execute("http://" + IP_ADDRESS + "/modify.php",userID,userPass,userLifearray,userTrgterIndvdl,userArea);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemberUpdateActivity.this);
                alertDialogBuilder
                        .setMessage( "수정되었습니다")
                        .setCancelable(true)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int arg1) {
                                        //수정완료 AlertDialog 출력 후 설정화면으로 이동
                                        startActivity(new Intent(MemberUpdateActivity.this, SettingActivity.class));

                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    //DB에 정보를 update하기 위해 서버와 연결
    class UpdateData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "update_result "+ result);
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
                    "& userTrgterIndvdl=" + userTrgterIndvdl + "& userArea=" + userArea;



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


    //기존 유저정보를 DB에서 가져옴
    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MemberUpdateActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result != null){
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://10.0.2.2/main_userinfo.php";
            String postParameters = "userID=" + searchKeyword1;

            Log.d(TAG, "userid_update : " + searchKeyword1);

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
                Log.d(TAG, "JSONObject : "+ item);

                String userLifearray = item.getString("userLifearray"); //디비에서 가져온 userLifearray를 대입
                String userTrgterIndvdl = item.getString("userTrgterIndvdl"); //디비에서 가져온 userTrgterIndvdl를 대입
                String userArea = item.getString("userArea"); //디비에서 가져온 userTrgterIndvdl를 대입

                for(int q=0; q<lifeArray_items.length ; q++){
                    if(lifeArray_items[q].equals(userLifearray)) { //생애주기 배열의 인덱스 값이 userLifearray와 같다면
                        update_lifearray.setSelection(q);   //생애주기의 초기 스피너값을 해당 인덱스 아이템으로 설정
                    }
                }

                for(int t=0; t<trgterIndvdlArray_items.length ; t++){
                    if(trgterIndvdlArray_items[t].equals(userTrgterIndvdl)) { //가구유형 배열의 인덱스 값이 userLifearray와 같다면
                        update_trgterIndvdlArray.setSelection(t); //가구유형의 초기 스피너값을 해당 인덱스 아이템으로 설정
                    }
                }

                for(int t=0; t<area_items.length ; t++){
                    if(area_items[t].equals(userArea)) { //가구유형 배열의 인덱스 값이 userLifearray와 같다면
                        update_area.setSelection(t); //가구유형의 초기 스피너값을 해당 인덱스 아이템으로 설정
                    }
                }
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult_member : ", e);
        }

    }

}
