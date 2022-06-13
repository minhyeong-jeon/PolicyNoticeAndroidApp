package com.aqua.anroid.policynoticeapp.Favorite;

public class FavoriteData {
    public String item_name;   //즐겨찾기 할 정보의 제목
    public String item_content;  //즐겨찾기 할 정보의 내용
    public String servID;        // 서비스 ID
    public String CloseDt;       // 즐겨찾기 할 정보의 접수 마감일


    public FavoriteData( String item_name, String item_content, String servID, String CloseDt) {
        this.item_name = item_name;
        this.item_content = item_content;
        this.servID = servID;
        this.CloseDt = CloseDt;

    }

    public String getItem_name() { return item_name; }

    public String getItem_content() {
        return item_content;
    }

    public String getServID() {
        return servID;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public void setServID(String servID) {
        this.servID = servID;
    }

    public String getCloseDt() {
        return CloseDt;
    }

    public void setCloseDt(String CloseDt) {
        this.CloseDt = CloseDt;
    }


}