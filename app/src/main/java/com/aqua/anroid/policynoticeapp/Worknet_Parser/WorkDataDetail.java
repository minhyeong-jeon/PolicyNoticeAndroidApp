package com.aqua.anroid.policynoticeapp.Worknet_Parser;

public class WorkDataDetail {
    public String jobsNm  ;          //모집점종
    public String wantedTitle;      //구인제목
    public String relJobsNm;        //관련직종
    public String jobCont;          //직무내용
    public String salTpNm;          //인금조건
    public String workRegion;       //근무예정지
    public String workdayWorkhrCont;//근무시간/형태
    public String pfCond;           //우대조건
    public String selMthd;          //전형방법

    public WorkDataDetail(){

    }
    public WorkDataDetail(WorkDataDetail data)
    {
        this.jobsNm = data.jobsNm;
        this.wantedTitle =data.wantedTitle;
        this.relJobsNm = data.relJobsNm;
        this.jobCont = data.jobCont;
        this.salTpNm = data.salTpNm;
        this.workRegion = data.workRegion;
        this.workdayWorkhrCont = data.workdayWorkhrCont;
        this.pfCond = data.pfCond;
        this.selMthd = data.selMthd;

    }

    public void SetEmpty(){
        jobsNm = "";
        wantedTitle = "";
        relJobsNm= "";
        jobCont = "";
        salTpNm = "";
        workRegion = "";
        workdayWorkhrCont= "";
        pfCond= "";
        selMthd= "";

    }
}
