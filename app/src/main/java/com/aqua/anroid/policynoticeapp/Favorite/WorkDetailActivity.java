package com.aqua.anroid.policynoticeapp.Favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataDetail;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkDataParser;
import com.aqua.anroid.policynoticeapp.Worknet_Parser.WorkWantedDetail;

import java.util.ArrayList;

public class WorkDetailActivity extends AppCompatActivity {
    private static String TAG = "phptest";

    public static Context workdetail_context;

    WorkDataParser workparser = new WorkDataParser();

    ArrayList<WorkDataDetail> workDetailArray; //상세보기그릇

    TextView jobsNm, wantedTitle, relJobsNm, jobCont, salTpNm, workRegion, workdayWorkhrCont, pfCond, selMthd;


    String intent_servID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workdetail);
        workdetail_context = this;


        jobsNm = findViewById(R.id.jobsNm);
        wantedTitle = findViewById(R.id.wantedTitle);
        relJobsNm = findViewById(R.id.relJobsNm);
        jobCont = findViewById(R.id.jobCont);
        salTpNm = findViewById(R.id.salTpNm);
        workRegion = findViewById(R.id.workRegion);
        workdayWorkhrCont = findViewById(R.id.workdayWorkhrCont);
        pfCond = findViewById(R.id.pfCond);
        selMthd = findViewById(R.id.selMthd);


        ImageView backbtn = findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkDetailActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        intent_servID = intent.getStringExtra("servID");
        WorkSearchDateDetail(intent_servID);
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

    // 리스트 뷰에 상세 보기 데이터 출력
    void ShowWorkDetailData()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int t = 0; t <workDetailArray.size(); t++)
                {
                    if (workDetailArray.get(t).wantedTitle != null)
                        wantedTitle.setText(workDetailArray.get(t).wantedTitle);

                    if (workDetailArray.get(t).jobsNm != null)
                        jobsNm.setText(workDetailArray.get(t).jobsNm);

                    if (workDetailArray.get(t).relJobsNm != null)   //관련직종
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
}
