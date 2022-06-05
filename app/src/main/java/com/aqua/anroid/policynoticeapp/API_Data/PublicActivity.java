package com.aqua.anroid.policynoticeapp.API_Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.aqua.anroid.policynoticeapp.User.MenuActivity;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataDetail;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataList;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataParser;
import com.aqua.anroid.policynoticeapp.Public_Parser.WantedDetail;
import com.aqua.anroid.policynoticeapp.Public_Parser.WantedList;
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
import java.util.ArrayList;


public class PublicActivity extends AppCompatActivity implements ParsingAdapter.OnItemClick {
    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";
    private static final String TAG_JSON="root";
    String userID;
    ImageView btn_menu;
    String mJsonString;
    public static Context context;

    PublicDataParser parser = new PublicDataParser();
    ArrayList<PublicDataList> publicDataArray = new ArrayList<>();;
    ArrayList<PublicDataList> publicDataList;

    ArrayList<PublicDataDetail> publicDetailArray;

    ArrayList<String> scrollServID = new ArrayList<String>();

    // Scroll
    final ArrayList<String> scrollItemList = new ArrayList<String>();
    ArrayAdapter<String> adapter = null;

    String searchServID; //서비스아이디값
    String lifeArrayText;         //생애주기입력값
    String trgterIndvdlArrayText; //가구유형입력값
    String title_search;
    String detail_search;

    EditText input_searchWrd;

    String[] lifeArray_items = {"선택안함", "영유아", "아동", "청소년", "청년","중장년", "노년", "임신·출산" };
    String[] trgterIndvdlArray_items = {"선택안함", "다문화·탈북민", "다자녀", "보훈대상자", "장애인", "저소득", "한부모·조손"};
    String[] desireArray_items = { "선택안함", "일자리", "주거", "일상생활", "신체건강 및 보건의료", "정신건강 및 심리정서", "보호 및 돌봄·요양", "보육 및 교육", "문화 및 여가", "안전 및 권익보장",};
    String[] check_search_items = { "제목", "내용", "제목+내용"};

    Spinner check_life; //생애주기 스피너 값 저장변수
    Spinner check_trgterIndvdlArray; //가구유형 스피너 값 저장변수
    Spinner check_desireArray; //관심주제 스피너 값 저장변수
    Spinner check_search;   //검색유형 스피너 값 저장변수

    TextView servNm, jurMnofNm, tgtrDtlCn, slctCritCn, alwServCn, trgterIndvdlArray, lifeArray;

    ParsingAdapter parsingAdapter;

    private View layout_1;
    private View layout_2;

    ListView list;

    @Override
    public void onClick(String value) {
        searchServID = value;
        Log.d("searchServID",searchServID);
        SearchDateDetail(value);

        layout_1.setVisibility(View.VISIBLE);
        layout_2.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        context = this;

        check_life = findViewById(R.id.check_life);
        check_trgterIndvdlArray = findViewById(R.id.check_trgterIndvdlArray);
        check_desireArray = findViewById(R.id.check_desireArray);
        check_search = findViewById(R.id.check_search);

        layout_1 = (LinearLayout) findViewById(R.id.layout);
        layout_2 = (LinearLayout) findViewById(R.id.main_layout);

        servNm = findViewById(R.id.servNm);
        jurMnofNm = findViewById(R.id.jurMnofNm);
        tgtrDtlCn = findViewById(R.id.tgtrDtlCn);
        slctCritCn = findViewById(R.id.slctCritCn);
        alwServCn = findViewById(R.id.alwServCn);
        trgterIndvdlArray = findViewById(R.id.trgterIndvdlArray);
        lifeArray = findViewById(R.id.lifeArray);

        TextView worknet = findViewById(R.id.worknet);

        layout_1.setVisibility(View.INVISIBLE);
        layout_2.setVisibility(View.VISIBLE);

        Button buttonAPI_List = findViewById(R.id.buttonAPI_List);
        Button resetBtn = findViewById(R.id.resetBtn);
        ImageView backbtn = findViewById(R.id.backbtn);

        buttonAPI_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDataList();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_2.setVisibility(View.VISIBLE);
                layout_1.setVisibility(View.INVISIBLE);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_searchWrd = findViewById(R.id.input_searchWrd);
                input_searchWrd.setText(null);
                input_searchWrd.clearFocus();
                check_life.setSelection(0);
                check_trgterIndvdlArray.setSelection(0);
                check_desireArray.setSelection(0);
                publicDataList.clear();
                parsingAdapter.notifyDataSetChanged();
            }
        });

        worknet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicActivity.this, WorkActivity.class);
                startActivity(intent);

            }
        });
        //생애주기 스피너 어뎁터
        ArrayAdapter<String> lifeArray_adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,lifeArray_items);
        lifeArray_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        check_life.setAdapter(lifeArray_adapter);
        check_life.setSelection(0,false);

        check_life.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        check_trgterIndvdlArray.setAdapter(trgterIndvdlArray_adapter);
        check_trgterIndvdlArray.setSelection(0,false);

        check_trgterIndvdlArray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        check_desireArray.setAdapter(desireArray_adapter);
        check_desireArray.setSelection(0,false);

        check_desireArray.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        check_search.setAdapter(search_adapter);
        check_search.setSelection(0,false);

        check_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /*로그인 id값 받는 부분*/
        SharedPreferences sharedPreferences = getSharedPreferences("userID",MODE_PRIVATE);
        userID  = sharedPreferences.getString("userID","");


        //메뉴버튼 클릭 시 메뉴화면으로 이동
        btn_menu = findViewById(R.id.menubtn);
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });

        GetData task = new GetData();
        task.execute(userID);

        // 리스트뷰 초기화
        InitListView();

    }

    void SearchDataList() //목록조회검색및필터링
    {
        new Thread(){
            public  void run(){
                try {

                    input_searchWrd = findViewById(R.id.input_searchWrd);

                    // 검색에 필요한 입력 데이터
                    WantedList wantedList = new WantedList();
                    wantedList.searchWrd = input_searchWrd.getText().toString();        // 키워드

                    //title_search = wantedList.searchWrd;
                    if(check_search.getSelectedItem().equals("제목")){
                        title_search = wantedList.searchWrd;
                        detail_search = null;
                        Log.d(TAG, "검색어_제목 " + title_search);

                    }
                    else if(check_search.getSelectedItem().equals("내용")){
                        detail_search = wantedList.searchWrd;
                        title_search=null;
                        Log.d(TAG, "검색어_내용 " + detail_search);

                    }
                    else if(check_search.getSelectedItem().equals("제목+내용")){
                        title_search = wantedList.searchWrd;
                        detail_search = wantedList.searchWrd;
                        Log.d(TAG, "검색어_제목+내용 " + title_search + "," + detail_search);

                    }


                    //001영유아 002아동 003청소년 004청년 005중장년 006노년 007임신·출산
                    if(check_life.getSelectedItem().equals("영유아")){
                        wantedList.lifeArray="001";
                    }
                    else if(check_life.getSelectedItem().equals("아동")) {
                        wantedList.lifeArray = "002";
                    }
                    else if(check_life.getSelectedItem().equals("청소년")) {
                        wantedList.lifeArray = "003";
                    }
                    else if(check_life.getSelectedItem().equals("청년")) {
                        wantedList.lifeArray = "004";
                    }
                    else if(check_life.getSelectedItem().equals("중장년")) {
                        wantedList.lifeArray = "005";
                    }
                    else if(check_life.getSelectedItem().equals("노년")) {
                        wantedList.lifeArray = "006";
                    }
                    else if(check_life.getSelectedItem().equals("임신·출산")) {
                        wantedList.lifeArray = "007";
                    }
                    else if(check_life.getSelectedItem().equals("선택안함")) {
                        wantedList.lifeArray = "";
                    }

                    lifeArrayText=check_life.getSelectedItem().toString();

                    //010다문화·탈북민 020다자녀 030보훈대상자 040장애인 050저소득 060한부모·조손
                    if(check_trgterIndvdlArray.getSelectedItem().equals("다문화·탈북민")){
                        wantedList.trgterIndvdlArray="010";
                    }
                    else if(check_trgterIndvdlArray.getSelectedItem().equals("다자녀")) {
                        wantedList.trgterIndvdlArray = "020";
                    }
                    else if(check_trgterIndvdlArray.getSelectedItem().equals("보훈대상자")) {
                        wantedList.trgterIndvdlArray = "030";
                    }
                    else if(check_trgterIndvdlArray.getSelectedItem().equals("장애인")) {
                        wantedList.trgterIndvdlArray = "040";
                    }
                    else if(check_trgterIndvdlArray.getSelectedItem().equals("저소득")) {
                        wantedList.trgterIndvdlArray = "050";
                    }
                    else if(check_trgterIndvdlArray.getSelectedItem().equals("한부모·조손")) {
                        wantedList.trgterIndvdlArray = "060";
                    }
                    else if(check_trgterIndvdlArray.getSelectedItem().equals("선택안함")) {
                        wantedList.trgterIndvdlArray = "";
                    }

                    trgterIndvdlArrayText=check_trgterIndvdlArray.getSelectedItem().toString();


                    if(check_desireArray.getSelectedItem().equals("일자리")){
                        wantedList.desireArray="100";
                    }
                    else if(check_desireArray.getSelectedItem().equals("주거")) {
                        wantedList.desireArray = "110";
                    }
                    else if(check_desireArray.getSelectedItem().equals("일상생활")) {
                        wantedList.desireArray = "120";
                    }
                    else if(check_desireArray.getSelectedItem().equals("신체건강 및 보건의료")) {
                        wantedList.desireArray = "130";
                    }
                    else if(check_desireArray.getSelectedItem().equals("정신건강 및 심리정서")) {
                        wantedList.desireArray = "140";
                    }
                    else if(check_desireArray.getSelectedItem().equals("보호 및 돌봄·요양")) {
                        wantedList.desireArray = "150";
                    }
                    else if(check_desireArray.getSelectedItem().equals("보육 및 교육")) {
                        wantedList.desireArray = "160";
                    }
                    else if(check_desireArray.getSelectedItem().equals("문화 및 여가")) {
                        wantedList.desireArray = "170";
                    }
                    else if(check_desireArray.getSelectedItem().equals("안전 및 권익보장")) {
                        wantedList.desireArray = "180";
                    }
                    else if(check_desireArray.getSelectedItem().equals("선택안함")) {
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



    public void SearchDateDetail(String str){
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
        parsingAdapter = new ParsingAdapter(this, publicDataList, this, this);
        list.setAdapter(parsingAdapter);


        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }


    //스크롤뷰해당하는아이템에 인덱스 번호가 포지션변수에 들어옴//서비스아이디에 해당포지션에 해당하는 서비스아이디대입
    // 리스트 뷰에 목록 조회 데이터 출력
    void ShowPublicDataList()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                publicDataList.clear(); //리스트 초기화
                scrollServID.clear();
                for(int y = 0; y <publicDataArray.size(); y++) {
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
                    if(title_search.equals("") || detail_search.equals("")) {
                        if (publicDataArray.get(y).servNm.contains(title_search) &&
                            publicDataArray.get(y).servDgst.contains(detail_search)) {
                            if (publicDataArray.get(y).lifeArray.contains(lifeArrayText) &&
                                publicDataArray.get(y).trgterIndvdlArray.contains(trgterIndvdlArrayText)) {//설정한 생애주기와가구유형에 해당하는값만 출력

                                //scrollServID.add((publicDataArray.get(y).servID));
                                publicDataList.add(publicDataArray.get(y));
                            }
                        }
                    }

                    //제목+내용
                    else {
                        if (publicDataArray.get(y).servNm.contains(title_search) ||
                                publicDataArray.get(y).servDgst.contains(detail_search)) {
                            if (publicDataArray.get(y).lifeArray.contains(lifeArrayText) &&
                                    publicDataArray.get(y).trgterIndvdlArray.contains(trgterIndvdlArrayText)) {//설정한 생애주기와가구유형에 해당하는값만 출력

                                //scrollServID.add((publicDataArray.get(y).servID));
                                publicDataList.add(publicDataArray.get(y));
                            }
                        }
                    }
                }
                parsingAdapter.notifyDataSetChanged();

            }
        });
    }

    // 리스트 뷰에 상세 보기 데이터 출력
    void ShowPublicDetailData()
    {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String tgtrDtlCn_test;
                String slctCritCn_test;
                String alwServCn_test;
                for (int k = 0; k < publicDetailArray.size(); k++) {
                    if (publicDetailArray.get(k).servNm != null)    //서비스명
                        servNm.setText(publicDetailArray.get(k).servNm);

                    if (publicDetailArray.get(k).jurMnofNm != null) {   //소관부처명
                        jurMnofNm.setText(publicDetailArray.get(k).jurMnofNm);
                    }

                    if (publicDetailArray.get(k).tgtrDtlCn != null) {   //대상자
                        tgtrDtlCn_test = publicDetailArray.get(k).tgtrDtlCn.replace("\r", "").replace("\n", "");
                        tgtrDtlCn.setText(tgtrDtlCn_test);
                    }

                    if (publicDetailArray.get(k).slctCritCn != null) {  //선정기준
                        slctCritCn_test = publicDetailArray.get(k).slctCritCn.replace("\r", "").replace("\n", "");
                        slctCritCn.setText(slctCritCn_test);

                    }
                    if (publicDetailArray.get(k).alwServCn != null) {   //급여서비스
                        alwServCn_test = publicDetailArray.get(k).alwServCn.replace("\r", "").replace("\n", "");
                        alwServCn.setText(alwServCn_test);

                    }
                    if (publicDetailArray.get(k).trgterIndvdlArray != null) //가구유형
                        trgterIndvdlArray.setText(publicDetailArray.get(k).trgterIndvdlArray);

                    if (publicDetailArray.get(k).lifeArray != null) //생애주기
                        lifeArray.setText(publicDetailArray.get(k).lifeArray);
                }
            }
        });
    }



    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(PublicActivity.this,
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

                String userLifearray = item.getString("userLifearray");
                String userTrgterIndvdl = item.getString("userTrgterIndvdl");

                for(int q=0; q<lifeArray_items.length ; q++){
                    if(lifeArray_items[q].equals(userLifearray)) {
                        Log.d(TAG, "생애주기 값 : " + lifeArray_items[q]);
                        check_life.setSelection(q);
                    }
                }
                for(int t=0; t<trgterIndvdlArray_items.length ; t++){
                    if(trgterIndvdlArray_items[t].equals(userTrgterIndvdl)) {
                        Log.d(TAG, "생애주기 값 : " + trgterIndvdlArray_items[t]);
                        check_trgterIndvdlArray.setSelection(t);
                    }
                }
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult_member : ", e);
        }

    }
}