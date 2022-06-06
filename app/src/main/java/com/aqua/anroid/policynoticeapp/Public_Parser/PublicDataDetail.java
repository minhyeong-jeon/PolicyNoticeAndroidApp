package com.aqua.anroid.policynoticeapp.Public_Parser;

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
