package com.aqua.anroid.policynoticeapp.NonUser;

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

import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.User.MenuActivity;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataDetail;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataList;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataParser;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkWantedDetail;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkWantedList;

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
public class NonWorkActivity extends AppCompatActivity implements NonWorkParsingAdapter.OnItemClick{
    private static String TAG = "phptest";

    WorkDataParser workparser = new WorkDataParser();

    ArrayList<WorkDataList>   workDataArray;   //목록조회그릇
    ArrayList<WorkDataDetail> workDetailArray; //상세보기그릇

    ArrayList<WorkDataList> workDataList;
    NonWorkParsingAdapter nonWorkParsingAdapter;


    String userID; //서비스아이디값

    String[] check_title_items = { "제목", "회사명", "제목+회사"};
    String[] check_area_items = { "선택안함", "지역무관", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주"};
    String[] check_salary_items = {"선택안함", "일급", "시급", "월급", "연봉"};

    Spinner check_area, check_salary, check_title;     //스피너 값 저장변수
    String check_area_text, check_salary_text;         //입력값
    String title_search, company_search;

    EditText input_search;   //검색어 변수

    ListView list;
    ImageView chatbot_non;

    public static  Context work_context;
    TextView jobsNm, wantedTitle, relJobsNm, jobCont, salTpNm, workRegion, workdayWorkhrCont, pfCond, selMthd;
    private View layout_1, layout_2;

    //어뎁터에서 보낸 onClick 함수
    @Override
    public void onClick(String value) {
        WorkSearchDateDetail(value); //어뎁터에서 받아온 서비스아이디를 인자로하여 WorkSearchDateDetail함수 호출
        layout_1.setVisibility(View.VISIBLE); //상세결과를 보이게
        layout_2.setVisibility(View.INVISIBLE); //목록결과 레이아웃 숨김
    }

    public void onClick_work_List(View view)  //목록조회버튼
    {
        WorkSearchDataList();
    }

    public void  onClick_work_reset(View view) //초기화 버튼
    {
        input_search = findViewById(R.id.input_search);
        input_search.setText(null); //검색어 초기화
        input_search.clearFocus();
        check_area.setSelection(0); //지역 초기화
        check_salary.setSelection(0); //임금유형 초기화
        workDataList.clear(); //리스트 초기화
        nonWorkParsingAdapter.notifyDataSetChanged();
    }

    //상세결과 화면에서 뒤로가기 버튼 클릭 시
    public void back_searchlist(View view){
        layout_2.setVisibility(View.VISIBLE);
        layout_1.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonwork);
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


        //문의 아이콘 클릭 시
        chatbot_non = findViewById(R.id.chatbot_non);
        chatbot_non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //비회원 문의 메인화면으로 이동
                Intent intent = new Intent(NonWorkActivity.this, NonChatbotMainActivity.class);
                startActivity(intent);

            }
        });

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
        //복지정책 이동 버튼 클릭 시
        TextView publicclick_non = findViewById(R.id.publicclick_non);
        publicclick_non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NonWorkActivity.this, NonPublicActivity.class);
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

                    //검색어 필터링
                    if(check_title.getSelectedItem().equals("제목")){
                        title_search = input_search.getText().toString();
                        company_search = null;

                    }
                    else if(check_title.getSelectedItem().equals("회사명")){
                        company_search = input_search.getText().toString();
                        title_search=null;
                    }

                    else if(check_title.getSelectedItem().equals("제목+회사")){
                        company_search = input_search.getText().toString();
                        title_search=input_search.getText().toString();
                    }

                    //근무지역 필터링 : 워크넷 API에서 제공하는 코드를 지역명과 맵핑
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


                    //임금유형 필터링 : 워크넷 API에서 제공하는 코드를 임금유형과 맵핑
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
                    // 상세정보클릭시 서비스아이디를 받고 링크만들기
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

        list = (ListView) findViewById(R.id.listView2);
        workDataList = new ArrayList<>();
        nonWorkParsingAdapter = new NonWorkParsingAdapter(this, workDataList, this, this);
        list.setAdapter(nonWorkParsingAdapter);

    }

    // 리스트 뷰에 목록 조회 데이터 출력
    void ShowWorkDataList()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                workDataList.clear(); //리스트 초기화
                for(int q = 0; q <workDataArray.size(); q++) {
                    //null값을 주지 않기위해 공백으로 초기화
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

                    //제목 or 회사명 or 검색어 미입력 시
                    //어레이에서 해당 String이 포함되는 아이템을 get하여 workDataList에 add해줌
                    //workDataList는 어뎁터 생성자에 인자로 들어감
                    if (title_search.equals("") || company_search.equals("")) {
                        if (workDataArray.get(q).title.contains(title_search) &&
                                workDataArray.get(q).company.contains(company_search)) {
                            if (workDataArray.get(q).region.contains(check_area_text) &&
                                    workDataArray.get(q).salTpNm.contains(check_salary_text)) {

                                workDataList.add(workDataArray.get(q));
                                Log.d("id", workDataArray.get(q).wantedAuthNo);

                            }
                        }
                    }
                    //제목+회사명으로 입력 시
                    //어레이에서 해당 String이 포함되는 아이템을 get하여 workDataList에 add해줌
                    //workDataList는 어뎁터 생성자에 인자로 들어감
                    else {
                        if (workDataArray.get(q).title.contains(title_search) ||
                                workDataArray.get(q).company.contains(company_search)) {
                            if (workDataArray.get(q).region.contains(check_area_text) &&
                                    workDataArray.get(q).salTpNm.contains(check_salary_text)) {

                                workDataList.add(workDataArray.get(q));

                            }
                        }

                    }

                }
                nonWorkParsingAdapter.notifyDataSetChanged();
            }
        });
    }

    // 리스트 뷰에 상세 보기 데이터 출력
    void ShowWorkDetailData()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int t = 0; t <workDetailArray.size(); t++)
                {
                    if (workDetailArray.get(t).wantedTitle != null) //채용제목
                        wantedTitle.setText(workDetailArray.get(t).wantedTitle);

                    if (workDetailArray.get(t).jobsNm != null)  //모집직종
                        jobsNm.setText(workDetailArray.get(t).jobsNm);

                    if (workDetailArray.get(t).relJobsNm != null)   //관련직종
                        relJobsNm.setText(workDetailArray.get(t).relJobsNm);
                    
                    if (workDetailArray.get(t).jobCont != null) //직무내용
                        jobCont.setText(workDetailArray.get(t).jobCont);

                    if (workDetailArray.get(t).salTpNm != null) //임금조건
                        salTpNm.setText(workDetailArray.get(t).salTpNm);

                    if (workDetailArray.get(t).workRegion != null)  //근무예정지
                        workRegion.setText(workDetailArray.get(t).workRegion);

                    if (workDetailArray.get(t).workdayWorkhrCont != null)   //근무시간/형태
                        workdayWorkhrCont.setText(workDetailArray.get(t).workdayWorkhrCont);

                    if (workDetailArray.get(t).pfCond != null)  //우대조건
                        pfCond.setText(workDetailArray.get(t).pfCond);

                    if (workDetailArray.get(t).selMthd != null) //전형방법
                        selMthd.setText(workDetailArray.get(t).selMthd);
                }

            }
        });
    }

}