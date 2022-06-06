package com.aqua.anroid.policynoticeapp.Public_Parser;

public class PublicDataList {

    public String jurMnofNm;          // 단체
    public String lifeArray;          //생애주기
    public String servDgst;           // 내용
    public String servDtlLink;        // 링크
    public String servNm;             // 제목
    public String trgterIndvdlArray;  // 대상
    public String servID;             // 서비스 ID

    public PublicDataList(){

    }
    public PublicDataList(PublicDataList data)
    {
        this.jurMnofNm =data.jurMnofNm;
        this.lifeArray =data.lifeArray;
        this.servDgst = data.servDgst;
        this.servDtlLink = data.servDtlLink;
        this.servNm = data.servNm;
        this.trgterIndvdlArray = data.trgterIndvdlArray;
        this.servID = data.servID;
    }

    public void SetEmpty(){
        jurMnofNm = "";
        lifeArray = "";
        servDgst= "";
        servDtlLink = "";
        servNm = "";
        trgterIndvdlArray = "";
        servID = "";
    }

    public String getServNm() {
        return servNm;
    }

    public String getServID() {
        return servID;
    }

    public String getServDgst() {
        return servDgst;
    }

    public void setServID(String servID) {
        this.servID = servID;
    }

    public void setServNm(String servNm) {
        this.servNm = servNm;
    }

    public void setServDgst(String servDgst) {
        this.servDgst = servDgst;
    }


}
