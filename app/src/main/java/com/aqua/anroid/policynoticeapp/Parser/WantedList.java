package com.aqua.anroid.policynoticeapp.Parser;
////목록조회 기본값
public class WantedList {
    public String serviceKey;
    public String callTp            = "L";      //페이지타입 L:목록 D상세
    public String pageNo            = "1";      //기본값 1,최대 1000
    public String numOfRows         = "100";    //출력건수
    public String srchKeyCode       = "003";    //001제목 002내용 003제목+내용
    public String searchWrd         = "";      //★ 검색어
    public String lifeArray         = "";       //★ 생애주기
    public String charTrgterArray   = "";       //대상특성
    public String obstKiArray       = "";       //장애유형
    public String obstAbtArray      = "";       //장애정도
    public String trgterIndvdlArray = "";       //★ 가구유형
    public String desireArray       = "";       //★ 사업목적
    //public String servID       = "";       //★ 서비스아이디

}
