package com.aqua.anroid.policynoticeapp.NonUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.API_Data.WorkActivity;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataDetail;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataList;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataParser;
import com.aqua.anroid.policynoticeapp.Public_Parser.WantedDetail;
import com.aqua.anroid.policynoticeapp.Public_Parser.WantedList;
import com.aqua.anroid.policynoticeapp.R;

import java.util.ArrayList;

//즐겨찾기화면에서 상세설명을 보기위해 Activity 구현
public class NonPublicActivity extends AppCompatActivity implements NonParsingAdapter.OnItemClick {
    private static String IP_ADDRESS = "192.168.35.237";
    private static String TAG = "phptest";

    ImageView chatbot_non;

    public static Context context;

    PublicDataParser parser = new PublicDataParser();
    ArrayList<PublicDataList> publicDataArray = new ArrayList<>();;
    ArrayList<PublicDataList> publicDataList;

    ArrayList<PublicDataDetail> publicDetailArray;

    ArrayList<String> scrollServID = new ArrayList<String>();


    String searchServID; //서비스아이디값
    String lifeArrayText;         //생애주기입력값
    String trgterIndvdlArrayText; //가구유형입력값
    String title_search;  //검색어-제목 저장 변수
    String detail_search; //검색어-내용 저장 변수

    EditText input_searchWrd_non; //검색어 저장 변수

    String[] lifeArray_items = {"선택안함", "영유아", "아동", "청소년", "청년","중장년", "노년", "임신·출산" };
    String[] trgterIndvdlArray_items = {"선택안함", "다문화·탈북민", "다자녀", "보훈대상자", "장애인", "저소득", "한부모·조손"};
    String[] desireArray_items = { "선택안함", "일자리", "주거", "일상생활", "신체건강 및 보건의료", "정신건강 및 심리정서", "보호 및 돌봄·요양", "보육 및 교육", "문화 및 여가", "안전 및 권익보장",};
    String[] check_search_items = { "제목", "내용", "제목+내용"};

    Spinner check_life_non; //생애주기 스피너 값 저장변수
    Spinner check_trgterIndvdlArray_non; //가구유형 스피너 값 저장변수
    Spinner check_desireArray_non; //관심주제 스피너 값 저장변수
    Spinner check_search_non;   //검색유형 스피너 값 저장변수

    TextView servNm_non, jurMnofNm_non, tgtrDtlCn_non, slctCritCn_non, alwServCn_non, trgterIndvdlArray_non, lifeArray_non;

    NonParsingAdapter nonParsingAdapter;

    private View layout_1_non; //상세결과 레이아웃
    private View layout_2_non; //목록결과 레이아웃

    ListView list;

    //어뎁터에서 보낸 onClick 함수
    @Override
    public void onClick(String value) {
        searchServID = value;
        SearchDateDetail(searchServID); //어뎁터에서 받아온 서비스아이디를 인자로하여 SearchDateDetail함수 호출
        layout_1_non.setVisibility(View.VISIBLE); //상세결과를 보이게
        layout_2_non.setVisibility(View.INVISIBLE);  //목록결과 레이아웃 숨김
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nonmember);
        context = this;

        check_life_non = findViewById(R.id.check_life_non);
        check_trgterIndvdlArray_non = findViewById(R.id.check_trgterIndvdlArray_non);
        check_desireArray_non = findViewById(R.id.check_desireArray_non);
        check_search_non = findViewById(R.id.check_search_non);

        layout_1_non = (LinearLayout) findViewById(R.id.layout_non);
        layout_2_non = (LinearLayout) findViewById(R.id.main_layout_non);

        servNm_non = findViewById(R.id.servNm_non);
        jurMnofNm_non = findViewById(R.id.jurMnofNm_non);
        tgtrDtlCn_non = findViewById(R.id.tgtrDtlCn_non);
        slctCritCn_non = findViewById(R.id.slctCritCn_non);
        alwServCn_non = findViewById(R.id.alwServCn_non);
        trgterIndvdlArray_non = findViewById(R.id.trgterIndvdlArray_non);
        lifeArray_non = findViewById(R.id.lifeArray_non);


        layout_1_non.setVisibility(View.INVISIBLE);
        layout_2_non.setVisibility(View.VISIBLE);

        //목록조회 버튼 클릭 시
        Button buttonAPI_List_non = findViewById(R.id.buttonAPI_List_non);
        buttonAPI_List_non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDataList();
            }
        });

        //초기화 버튼 클릭 시
        Button resetBtn_non = findViewById(R.id.resetBtn_non);
        resetBtn_non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_searchWrd_non = findViewById(R.id.input_searchWrd);
                input_searchWrd_non.setText(null); //검색어 초기화
                input_searchWrd_non.clearFocus();
                check_life_non.setSelection(0); //생애주기 초기화
                check_trgterIndvdlArray_non.setSelection(0); //가구유형 초기화
                check_desireArray_non.setSelection(0); //관심주제 초기화
                publicDataList.clear();
                nonParsingAdapter.notifyDataSetChanged();
            }
        });

        //일자리정책 이동 버튼 클릭 시
        TextView worknet_non = findViewById(R.id.worknet_non);
        worknet_non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NonPublicActivity.this, NonWorkActivity.class);
                startActivity(intent);

            }
        });

        //상세설명 화면에서 뒤로가기 버튼 클릭 시
        ImageView backbtn_non = findViewById(R.id.backbtn_non);
        backbtn_non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_2_non.setVisibility(View.VISIBLE);
                layout_1_non.setVisibility(View.INVISIBLE);
            }
        });

        //생애주기 스피너 어뎁터
        ArrayAdapter<String> lifeArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,lifeArray_items);
        lifeArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_life_non.setAdapter(lifeArray_adapter);
        check_life_non.setSelection(0,false);

        check_life_non.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //가구유형 스피너 어뎁터
        ArrayAdapter<String> trgterIndvdlArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,trgterIndvdlArray_items);
        trgterIndvdlArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_trgterIndvdlArray_non.setAdapter(trgterIndvdlArray_adapter);
        check_trgterIndvdlArray_non.setSelection(0,false);

        check_trgterIndvdlArray_non.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //관심주제 스피너 어뎁터
        ArrayAdapter<String> desireArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,desireArray_items);
        desireArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_desireArray_non.setAdapter(desireArray_adapter);
        check_desireArray_non.setSelection(0,false);

        check_desireArray_non.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //검생유형 스피너 어뎁터
        ArrayAdapter<String> search_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,check_search_items);
        search_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_search_non.setAdapter(search_adapter);
        check_search_non.setSelection(0,false);

        check_search_non.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //문의 아이콘 클릭 시
        chatbot_non = findViewById(R.id.chatbot_non);
        chatbot_non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NonPublicActivity.this, NonChatbotMainActivity.class);
                startActivity(intent);

            }
        });

        // 리스트뷰 초기화
        InitListView();

    }

    void SearchDataList() //목록조회검색및필터링
    {
        new Thread(){
            public  void run(){
                try {

                    input_searchWrd_non = findViewById(R.id.input_searchWrd_non);

                    // 검색에 필요한 입력 데이터
                    WantedList wantedList = new WantedList();
                    wantedList.searchWrd = input_searchWrd_non.getText().toString();        // 키워드

                    //검색어 필터링
                    if(check_search_non.getSelectedItem().equals("제목")){
                        title_search = wantedList.searchWrd;
                        detail_search = null;
                    }
                    else if(check_search_non.getSelectedItem().equals("내용")){
                        detail_search = wantedList.searchWrd;
                        title_search=null;

                    }
                    else if(check_search_non.getSelectedItem().equals("제목+내용")){
                        title_search = wantedList.searchWrd;
                        detail_search = wantedList.searchWrd;
                    }


                    //001영유아 002아동 003청소년 004청년 005중장년 006노년 007임신·출산
                    if(check_life_non.getSelectedItem().equals("영유아")){
                        wantedList.lifeArray="001";
                    }
                    else if(check_life_non.getSelectedItem().equals("아동")) {
                        wantedList.lifeArray = "002";
                    }
                    else if(check_life_non.getSelectedItem().equals("청소년")) {
                        wantedList.lifeArray = "003";
                    }
                    else if(check_life_non.getSelectedItem().equals("청년")) {
                        wantedList.lifeArray = "004";
                    }
                    else if(check_life_non.getSelectedItem().equals("중장년")) {
                        wantedList.lifeArray = "005";
                    }
                    else if(check_life_non.getSelectedItem().equals("노년")) {
                        wantedList.lifeArray = "006";
                    }
                    else if(check_life_non.getSelectedItem().equals("임신·출산")) {
                        wantedList.lifeArray = "007";
                    }
                    else if(check_life_non.getSelectedItem().equals("선택안함")) {
                        wantedList.lifeArray = "";
                    }

                    lifeArrayText=check_life_non.getSelectedItem().toString();

                    //010다문화·탈북민 020다자녀 030보훈대상자 040장애인 050저소득 060한부모·조손
                    if(check_trgterIndvdlArray_non.getSelectedItem().equals("다문화·탈북민")){
                        wantedList.trgterIndvdlArray="010";
                    }
                    else if(check_trgterIndvdlArray_non.getSelectedItem().equals("다자녀")) {
                        wantedList.trgterIndvdlArray = "020";
                    }
                    else if(check_trgterIndvdlArray_non.getSelectedItem().equals("보훈대상자")) {
                        wantedList.trgterIndvdlArray = "030";
                    }
                    else if(check_trgterIndvdlArray_non.getSelectedItem().equals("장애인")) {
                        wantedList.trgterIndvdlArray = "040";
                    }
                    else if(check_trgterIndvdlArray_non.getSelectedItem().equals("저소득")) {
                        wantedList.trgterIndvdlArray = "050";
                    }
                    else if(check_trgterIndvdlArray_non.getSelectedItem().equals("한부모·조손")) {
                        wantedList.trgterIndvdlArray = "060";
                    }
                    else if(check_trgterIndvdlArray_non.getSelectedItem().equals("선택안함")) {
                        wantedList.trgterIndvdlArray = "";
                    }

                    trgterIndvdlArrayText=check_trgterIndvdlArray_non.getSelectedItem().toString();


                    if(check_desireArray_non.getSelectedItem().equals("일자리")){
                        wantedList.desireArray="100";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("주거")) {
                        wantedList.desireArray = "110";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("일상생활")) {
                        wantedList.desireArray = "120";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("신체건강 및 보건의료")) {
                        wantedList.desireArray = "130";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("정신건강 및 심리정서")) {
                        wantedList.desireArray = "140";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("보호 및 돌봄·요양")) {
                        wantedList.desireArray = "150";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("보육 및 교육")) {
                        wantedList.desireArray = "160";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("문화 및 여가")) {
                        wantedList.desireArray = "170";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("안전 및 권익보장")) {
                        wantedList.desireArray = "180";
                    }
                    else if(check_desireArray_non.getSelectedItem().equals("선택안함")) {
                        wantedList.desireArray = "";
                    }

                    //파싱 데이터 중복 제거
                    if( !wantedList.desireArray.isEmpty()) {
                        wantedList.lifeArray = "";
                        wantedList.trgterIndvdlArray = "";
                    }
                    else if(!wantedList.lifeArray.isEmpty()&&!wantedList.trgterIndvdlArray.isEmpty()){
                        wantedList.trgterIndvdlArray = "";
                    }

                    // [목록 조회]
                    if(parser.PulbicDataList_HttpURLConnection(wantedList)) {
                        publicDataArray = parser.XMLParserDataList();
                        ShowPublicDataList();
                    }
                }
                catch (Exception e){

                }
            }
        }.start();
    }



    void SearchDateDetail(String str){
        new Thread(){
            public  void run(){
                try {
                    // !? 상세정보클릭시 서비스아이디를 받고 링크만들기]
                    WantedDetail wantedDetail=new WantedDetail();
                    wantedDetail.servID = str;
                    if(parser.PulbicDataDetail_HttpURLConnection(wantedDetail)){
                        publicDetailArray = parser.XMLParserDataDetail();
                        ShowPublicDetailData();

                    }
                }
                catch (Exception e){
                }
            }
        }.start();
    }

    // 리스트뷰초기화
    void InitListView() {
        list = (ListView) findViewById(R.id.listView1);
        publicDataList = new ArrayList<>();
        nonParsingAdapter = new NonParsingAdapter(this, publicDataList, this, this);
        list.setAdapter(nonParsingAdapter);

    }


    //스크롤뷰해당하는아이템에 인덱스 번호가 포지션변수에 들어옴//서비스아이디에 해당포지션에 해당하는 서비스아이디대입
    // 리스트 뷰에 목록 조회 데이터 출력
    void ShowPublicDataList()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                publicDataList.clear(); //리스트 초기화
                for(int i = 0; i <publicDataArray.size(); i++) {
                    //null값을 주지 않기위해 공백으로 초기화
                    if (lifeArrayText.equals("선택안함")) {
                        lifeArrayText = "";
                    }
                    if (title_search == null) {
                        title_search = "";
                    }
                    if (detail_search == null) {
                        detail_search = "";
                    }
                    if (trgterIndvdlArrayText.equals("선택안함")) {
                        trgterIndvdlArrayText = "";
                    }

                    //검색어 미입력 or 제목 or 내용
                    //어레이에서 해당 String이 포함되는 아이템을 get하여 publicDataList에 add해줌
                    //publicDataList는 어뎁터 생성자에 인자로 들어감
                    if(title_search.equals("") || detail_search.equals("")) {
                        if (publicDataArray.get(i).servNm.contains(title_search) &&
                                publicDataArray.get(i).servDgst.contains(detail_search)) {
                            if (publicDataArray.get(i).lifeArray.contains(lifeArrayText) &&
                                    publicDataArray.get(i).trgterIndvdlArray.contains(trgterIndvdlArrayText)) {//설정한 생애주기와가구유형에 해당하는값만 출력

                                publicDataList.add(publicDataArray.get(i));
                            }
                        }
                    }

                    //제목+내용
                    //어레이에서 해당 String이 포함되는 아이템을 get하여 publicDataList에 add해줌
                    //publicDataList는 어뎁터 생성자에 인자로 들어감
                    else {
                        if (publicDataArray.get(i).servNm.contains(title_search) ||
                                publicDataArray.get(i).servDgst.contains(detail_search)) {
                            if (publicDataArray.get(i).lifeArray.contains(lifeArrayText) &&
                                    publicDataArray.get(i).trgterIndvdlArray.contains(trgterIndvdlArrayText)) {//설정한 생애주기와가구유형에 해당하는값만 출력

                                publicDataList.add(publicDataArray.get(i));
                            }
                        }
                    }
                }
                nonParsingAdapter.notifyDataSetChanged();

            }
        });
    }

    // 리스트 뷰에 상세 보기 데이터 출력
    void ShowPublicDetailData()
    {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //개행제거 된 문자 저장변수
                String tgtrDtlCn_test;
                String slctCritCn_test;
                String alwServCn_test;
                for (int k = 0; k < publicDetailArray.size(); k++) {
                    if (publicDetailArray.get(k).servNm != null)    //서비스명
                        servNm_non.setText(publicDetailArray.get(k).servNm);

                    if (publicDetailArray.get(k).jurMnofNm != null) {   //소관부처명
                        jurMnofNm_non.setText(publicDetailArray.get(k).jurMnofNm);
                    }

                    if (publicDetailArray.get(k).tgtrDtlCn != null) {   //대상자
                        //개행 (\r\n)을 공백으로 대체
                        tgtrDtlCn_test = publicDetailArray.get(k).tgtrDtlCn.replace("\r", "").replace("\n", "");
                        tgtrDtlCn_non.setText(tgtrDtlCn_test); //개행이 사라진 문자열을 set
                    }

                    if (publicDetailArray.get(k).slctCritCn != null) {  //선정기준
                        //개행 (\r\n)을 공백으로 대체
                        slctCritCn_test = publicDetailArray.get(k).slctCritCn.replace("\r", "").replace("\n", "");
                        slctCritCn_non.setText(slctCritCn_test); //개행이 사라진 문자열을 set

                    }
                    if (publicDetailArray.get(k).alwServCn != null) {   //급여서비스
                        //개행 (\r\n)을 공백으로 대체
                        alwServCn_test = publicDetailArray.get(k).alwServCn.replace("\r", "").replace("\n", "");
                        alwServCn_non.setText(alwServCn_test); //개행이 사라진 문자열을 set
                     }
                    if (publicDetailArray.get(k).trgterIndvdlArray != null) //가구유형
                        trgterIndvdlArray_non.setText(publicDetailArray.get(k).trgterIndvdlArray);

                    if (publicDetailArray.get(k).lifeArray != null) //생애주기
                        lifeArray_non.setText(publicDetailArray.get(k).lifeArray);
                }
            }
        });
    }
}