package com.aqua.anroid.policynoticeapp.Parser;

public class PublicDataDetail {
    public String servNm  ;
    public String jurMnofNm;
    public String tgtrDtlCn;
    public String slctCritCn;
    public String alwServCn;
    public String trgterIndvdlArray;
    public String lifeArray;
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
    public PublicDataDetail(String servNm, String jurMnofNm, String tgtrDtlCn , String slctCritCn, String alwServCn, String trgterIndvdlArray, String lifeArray) {
        this.servNm = servNm;
        this.jurMnofNm =jurMnofNm;
        this.tgtrDtlCn = tgtrDtlCn;
        this.slctCritCn = slctCritCn;
        this.alwServCn = alwServCn;
        this.trgterIndvdlArray = trgterIndvdlArray;
        this.lifeArray = lifeArray;


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
