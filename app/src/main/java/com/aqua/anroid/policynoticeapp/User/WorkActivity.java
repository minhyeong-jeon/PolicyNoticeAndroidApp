package com.aqua.anroid.policynoticeapp.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.MenuActivity;
import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.worknet_Parser.WorkDataDetail;
import com.aqua.anroid.policynoticeapp.worknet_Parser.WorkDataList;
import com.aqua.anroid.policynoticeapp.worknet_Parser.WorkDataParser;
import com.aqua.anroid.policynoticeapp.worknet_Parser.WorkWantedDetail;
import com.aqua.anroid.policynoticeapp.worknet_Parser.WorkWantedList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*API흐름
 * 리스트뷰초기화 -> URL생성 -> URL연결 -> 파서 ->데이터출력
 *
 */
public class WorkActivity extends AppCompatActivity implements WorkParsingAdapter.OnItemClick{
    private static String TAG = "phptest";
    String mJsonString;
    private static final String TAG_JSON="root";

    //PublicDataListParser parser = new PublicDataListParser();
    WorkDataParser workparser = new WorkDataParser();

    ArrayList<WorkDataList>   workDataArray;   //목록조회그릇
    ArrayList<WorkDataDetail> workDetailArray; //상세보기그릇

    ArrayList<WorkDataList> workDataList;
    WorkParsingAdapter workParsingAdapter;

    // Scroll
    final ArrayList<String> work_scrollItemList = new ArrayList<String>();
    ArrayAdapter<String> adapter = null;

    String serachAuthNo; //서비스아이디값

    Spinner check_title;   //검색유형 스피너 값 저장변수
    String[] check_title_items = { "제목", "회사명", "제목+회사"};

    Spinner check_area;     //지역 스피너 값 저장변수
    String[] check_area_items = { "선택안함", "지역무관", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"};
    String check_area_text;         //지역입력값

    Spinner check_salary;   //임금유형 스피너 값 저장변수
    String[] check_salary_items = {"선택안함", "일급", "시급", "월급", "연봉"};
    String check_salary_text;         //임금입력값

    String title_search;
    String company_search;

    EditText input_search;   //검색어 변수

    ListView list;

    ArrayList<String> work_scrollServID = new ArrayList<String>();

    public static  Context work_context;
    TextView jobsNm, wantedTitle, relJobsNm, jobCont, salTpNm, workRegion, workdayWorkhrCont, pfCond, selMthd;
    private View layout_1;
    private View layout_2;
    String userID;
    ImageView btn_menu;

    @Override
    public void onClick(String value) {

        serachAuthNo = value;
        Log.d("serachAuthNo",serachAuthNo);
        WorkSearchDateDetail(serachAuthNo);
        layout_1.setVisibility(View.VISIBLE);
        layout_2.setVisibility(View.INVISIBLE);
    }

    public void onClick_work_List(View view)  //목록조회버튼
    {
        WorkSearchDataList();
    }
    public void  onClick_work_reset(View view) //초기화 버튼
    {
        input_search = findViewById(R.id.input_search);
        input_search.setText(null);
        input_search.clearFocus();
        check_area.setSelection(0);
        check_salary.setSelection(0);
        workDataList.clear();
        workParsingAdapter.notifyDataSetChanged();
    }

    public void back_searchlist(View view){
        layout_2.setVisibility(View.VISIBLE);
        layout_1.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        work_context = this;

        check_title = findViewById(R.id.check_title);
        check_area = findViewById(R.id.check_area);
        check_salary = findViewById(R.id.check_salary);

        layout_1 = (LinearLayout) findViewById(R.id.layout);
        layout_2 = (LinearLayout) findViewById(R.id.main_layout);

        jobsNm = findViewById(R.id.jobsNm);
        wantedTitle = findViewById(R.id.wantedTitle);
        relJobsNm = findViewById(R.id.relJobsNm);
        jobCont = findViewById(R.id.jobCont);
        salTpNm = findViewById(R.id.salTpNm);
        workRegion = findViewById(R.id.workRegion);
        workdayWorkhrCont = findViewById(R.id.workdayWorkhrCont);
        pfCond = findViewById(R.id.pfCond);
        selMthd = findViewById(R.id.selMthd);

        layout_1.setVisibility(View.INVISIBLE);
        layout_2.setVisibility(View.VISIBLE);

        /*로그인 id값 받는 부분*/
        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");

        GetData task = new GetData();
        task.execute(userID);

        //지역 스피너 어뎁터
        ArrayAdapter<String> area_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,check_area_items);
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_area.setAdapter(area_adapter);
        check_area.setSelection(0,false);

        check_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //임금유형 스피너 어뎁터
        ArrayAdapter<String> salary_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,check_salary_items);
        salary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_salary.setAdapter(salary_adapter);
        check_salary.setSelection(0,false);

        check_salary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //검생유형 스피너 어뎁터
        ArrayAdapter<String> title_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,check_title_items);
        title_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_title.setAdapter(title_adapter);
        check_title.setSelection(0,false);

        check_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //메뉴버튼 클릭 시 메뉴화면으로 이동
        btn_menu = findViewById(R.id.menubtn);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });


        // 리스트뷰 초기화
        WorkInitListView();

    }

    void WorkSearchDataList() //목록조회검색및필터링
    {
        new Thread(){
            public  void run(){
                try {

                    input_search = findViewById(R.id.input_search);


                    // 검색에 필요한 입력 데이터
                    WorkWantedList workWantedList = new WorkWantedList();

                    if(check_title.getSelectedItem().equals("제목")){
                        title_search = input_search.getText().toString();
                        company_search = null;
                        Log.d(TAG, "제목 " + title_search);

                    }
                    else if(check_title.getSelectedItem().equals("회사명")){
                        company_search = input_search.getText().toString();
                        title_search=null;
                        Log.d(TAG, "회사명 " + company_search);
                    }

                    else if(check_title.getSelectedItem().equals("제목+회사")){
                        company_search = input_search.getText().toString();
                        title_search=input_search.getText().toString();
                        Log.d(TAG, "제목+회사 " + title_search + "," + company_search);
                    }


                    if(check_area.getSelectedItem().equals("지역무관")) workWantedList.region="00000";

                    else if(check_area.getSelectedItem().equals("서울")) workWantedList.region = "11000";

                    else if(check_area.getSelectedItem().equals("부산")) workWantedList.region = "26000";

                    else if(check_area.getSelectedItem().equals("대구")) workWantedList.region = "27000";

                    else if(check_area.getSelectedItem().equals("인천")) workWantedList.region = "28000";

                    else if(check_area.getSelectedItem().equals("광주")) workWantedList.region = "29000";

                    else if(check_area.getSelectedItem().equals("대전")) workWantedList.region = "30000";

                    else if(check_area.getSelectedItem().equals("울산")) workWantedList.region = "31000";

                    else if(check_area.getSelectedItem().equals("세종")) workWantedList.region = "36110";

                    else if(check_area.getSelectedItem().equals("경기")) workWantedList.region = "41000";

                    else if(check_area.getSelectedItem().equals("강원")) workWantedList.region = "42000";

                    else if(check_area.getSelectedItem().equals("충북")) workWantedList.region = "43000";

                    else if(check_area.getSelectedItem().equals("충남")) workWantedList.region = "44000";

                    else if(check_area.getSelectedItem().equals("전북")) workWantedList.region = "45000";

                    else if(check_area.getSelectedItem().equals("전남")) workWantedList.region = "46000";

                    else if(check_area.getSelectedItem().equals("경북")) workWantedList.region = "47000";

                    else if(check_area.getSelectedItem().equals("경남")) workWantedList.region = "48000";

                    else if(check_area.getSelectedItem().equals("제주")) workWantedList.region = "50000";

                    else if(check_area.getSelectedItem().equals("선택안함")) workWantedList.region = "";

                    check_area_text=check_area.getSelectedItem().toString();


                    if(check_salary.getSelectedItem().equals("일급")){
                        workWantedList.salTpNm="D";
                    }
                    else if(check_salary.getSelectedItem().equals("시급")) {
                        workWantedList.salTpNm = "H";
                    }
                    else if(check_salary.getSelectedItem().equals("월급")) {
                        workWantedList.salTpNm = "M";
                    }
                    else if(check_salary.getSelectedItem().equals("연봉")) {
                        workWantedList.salTpNm = "Y";
                    }
                    else if(check_salary.getSelectedItem().equals("선택안함")) {
                        workWantedList.salTpNm = "";
                    }
                    check_salary_text=check_salary.getSelectedItem().toString();


                    // [목록 조회]
                    if(workparser.PulbicDataList_HttpURLConnection(workWantedList)) {
                        workDataArray = workparser.XMLParserDataList();
                        ShowWorkDataList();
                    }
                }
                catch (Exception e){

                }
            }
        }.start();
    }


    void WorkSearchDateDetail(String str){
        new Thread(){
            public  void run(){
                try {
                    // !? 상세정보클릭시 서비스아이디를 받고 링크만들기
                    WorkWantedDetail workWantedDetail = new WorkWantedDetail();
                    workWantedDetail.wantedAuthNo = str;
                    if(workparser.PulbicDataDetail_HttpURLConnection(workWantedDetail)){
                        workDetailArray = workparser.XMLParserDataDetail();

                        ShowWorkDetailData();
                    }
                }
                catch (Exception e){

                }

            }
        }.start();
    }

    // 리스트뷰초기화
    void WorkInitListView() {
//        ListView list = (ListView) findViewById(R.id.listView1);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, scrollItemList);
//        list.setAdapter(adapter);


        list = (ListView) findViewById(R.id.listView2);
        workDataList = new ArrayList<>();
        workParsingAdapter = new WorkParsingAdapter(this, workDataList, this, this);
        list.setAdapter(workParsingAdapter);


//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)//스크롤뷰해당하는아이템에 인덱스 번호가 포지션변수에 들어옴
//            {
//                serachServID = publicDataArray.get(position).wantedAuthNo;   //서비스아이디에 해당포지션에 해당하는 서비스아이디대입
//                Toast.makeText(getApplicationContext(), "servID : " + serachServID  + " / pos : " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    // 리스트 뷰에 목록 조회 데이터 출력
    void ShowWorkDataList()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                workDataList.clear(); //리스트 초기화
                work_scrollItemList.clear();
                for(int q = 0; q <workDataArray.size(); q++) {
                    if (title_search == null) {
                        title_search = "";
                    }
                    if (company_search == null) {
                        company_search = "";
                    }
                    if (check_area_text.equals("선택안함")) {
                        check_area_text = "";
                    }
                    if (check_salary_text.equals("선택안함")) {
                        check_salary_text = "";
                    }

                    if (title_search.equals("") || company_search.equals("")) {
                        if (workDataArray.get(q).title.contains(title_search) &&
                                workDataArray.get(q).company.contains(company_search)) {
                            if (workDataArray.get(q).region.contains(check_area_text) &&
                                    workDataArray.get(q).salTpNm.contains(check_salary_text)) {

                                //work_scrollServID.add((workDataArray.get(q).wantedAuthNo));
                                workDataList.add(workDataArray.get(q));

                            }
                        }
                    } else {
                        if (workDataArray.get(q).title.contains(title_search) ||
                                workDataArray.get(q).company.contains(company_search)) {
                            if (workDataArray.get(q).region.contains(check_area_text) &&
                                    workDataArray.get(q).salTpNm.contains(check_salary_text)) {

                                //work_scrollServID.add((workDataArray.get(q).wantedAuthNo));
                                workDataList.add(workDataArray.get(q));

                            }
                        }

                    }

                }
                workParsingAdapter.notifyDataSetChanged();
            }
        });
    }

    // 리스트 뷰에 상세 보기 데이터 출력
    void ShowWorkDetailData()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                work_scrollItemList.clear();   //아이템리스트초기화
                for(int t = 0; t <workDetailArray.size(); t++)
                {
                    if (workDetailArray.get(t).wantedTitle != null)
                        wantedTitle.setText(workDetailArray.get(t).wantedTitle);

                    if (workDetailArray.get(t).jobsNm != null)
                        jobsNm.setText(workDetailArray.get(t).jobsNm);

                    if (workDetailArray.get(t).relJobsNm != null)
                        relJobsNm.setText(workDetailArray.get(t).relJobsNm);

                    if (workDetailArray.get(t).jobCont != null)
                        jobCont.setText(workDetailArray.get(t).jobCont);

                    if (workDetailArray.get(t).salTpNm != null)
                        salTpNm.setText(workDetailArray.get(t).salTpNm);

                    if (workDetailArray.get(t).workRegion != null)
                        workRegion.setText(workDetailArray.get(t).workRegion);

                    if (workDetailArray.get(t).workdayWorkhrCont != null)
                        workdayWorkhrCont.setText(workDetailArray.get(t).workdayWorkhrCont);

                    if (workDetailArray.get(t).pfCond != null)
                        pfCond.setText(workDetailArray.get(t).pfCond);

                    if (workDetailArray.get(t).selMthd != null)
                        selMthd.setText(workDetailArray.get(t).selMthd);
                }

            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(WorkActivity.this,
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

                String userArea = item.getString("userArea");

                for(int q=0; q<check_area_items.length ; q++){
                    if(check_area_items[q].equals(userArea)) {
                        Log.d(TAG, "생애주기 값 : " + check_area_items[q]);
                        check_area.setSelection(q);
                    }
                }
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult_member : ", e);
        }

    }
}