package com.aqua.anroid.policynoticeapp.Favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataDetail;
import com.aqua.anroid.policynoticeapp.Public_Parser.PublicDataParser;
import com.aqua.anroid.policynoticeapp.Public_Parser.WantedDetail;
import com.aqua.anroid.policynoticeapp.R;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    public static Context detail_context;

    PublicDataParser parser = new PublicDataParser();

    ArrayList<PublicDataDetail> publicDetailArray;


    TextView servNm, jurMnofNm, tgtrDtlCn, slctCritCn, alwServCn, trgterIndvdlArray, lifeArray;


    String intent_servID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detail_context = this;


        servNm = findViewById(R.id.servNm);
        jurMnofNm = findViewById(R.id.jurMnofNm);
        tgtrDtlCn = findViewById(R.id.tgtrDtlCn);
        slctCritCn = findViewById(R.id.slctCritCn);
        alwServCn = findViewById(R.id.alwServCn);
        trgterIndvdlArray = findViewById(R.id.trgterIndvdlArray);
        lifeArray = findViewById(R.id.lifeArray);


        ImageView backbtn = findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        intent_servID = intent.getStringExtra("servID");
        SearchDateDetail(intent_servID);
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
}
