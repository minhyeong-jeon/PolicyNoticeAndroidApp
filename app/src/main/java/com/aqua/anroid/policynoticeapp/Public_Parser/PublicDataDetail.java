package com.aqua.anroid.policynoticeapp.Public_Parser;

public class PublicDataDetail {
    public String servNm  ;         // 제목
    public String jurMnofNm;        // 단체
    public String tgtrDtlCn;        //대상자상세
    public String slctCritCn;       //선정기준
    public String alwServCn;        //급여서비스
    public String trgterIndvdlArray;// 대상
    public String lifeArray;        //생애주기

    public PublicDataDetail(){

    }
    public PublicDataDetail(PublicDataDetail data)
    {
        this.servNm = data.servNm;
        this.jurMnofNm =data.jurMnofNm;
        this.tgtrDtlCn = data.tgtrDtlCn;
        this.slctCritCn = data.slctCritCn;
        this.alwServCn = data.alwServCn;
        this.trgterIndvdlArray = data.trgterIndvdlArray;
        this.lifeArray = data.lifeArray;

    }

    public void SetEmpty(){
        servNm = "";
        jurMnofNm = "";
        tgtrDtlCn= "";
        slctCritCn = "";
        alwServCn = "";
        trgterIndvdlArray = "";
        lifeArray= "";

    }
}
