package com.aqua.anroid.policynoticeapp.Worknet_Parser;

public class WorkDataList {

    public String  wantedAuthNo;    //구인인증번호
    public String  company; //회사명
    public String  title;   //채용제목
    public String  salTpNm; //임금형태
    public String  region;  //근무지역
    public String  regDt;   //등록일자
    public String  closeDt; //마감일자
    public String  wantedInfoUrl;   //워크넷 채용정보 url

    public WorkDataList(){

    }
    public WorkDataList(WorkDataList data)
    {
        this.wantedInfoUrl =data.wantedInfoUrl;
        this.closeDt =data.closeDt;
        this.regDt =data.regDt;
        this.region =data.region;
        this.salTpNm =data.salTpNm;
        this.title =data.title;
        this.company =data.company;
        this.wantedAuthNo =data.wantedAuthNo;
    }

    public void SetEmpty(){

        wantedInfoUrl="";
        closeDt="";
        regDt="";
        region="";
        salTpNm="";
        title="";
        company= "";
        wantedAuthNo = "";

    }

    public String getWantedAuthNo() {
        return wantedAuthNo;
    }
    public void setWantedAuthNo(String wantedAuthNo) {
        this.wantedAuthNo = wantedAuthNo;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) { this.region = region; }

    public String getSalTpNm() {
        return salTpNm;
    }
    public void setSalTpNm(String region) { this.salTpNm = salTpNm; }

    public String getCloseDt() {
        return closeDt;
    }
    public void setCloseDt(String closeDt) { this.closeDt = closeDt; }

}
