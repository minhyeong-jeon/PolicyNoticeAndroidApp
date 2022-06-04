package com.aqua.anroid.policynoticeapp.Parser;

//import com.mobile.PolicyApp.PublicData;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

//목록 조회 URL 만들고 파싱해주는곳
public class PublicDataListParser {

    public ArrayList<PublicDataList>   publicDataLists  = new ArrayList<PublicDataList>();

    // 목록 조회 URL 만들기
    public String CreatePublicDataListURL(@NonNull WantedList wantedList) throws UnsupportedEncodingException
    {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B554287/NationalWelfareInformations/NationalWelfarelist"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=%2BWnjcadNxjH3FFyaHjifaa6i%2Fi3l9YuKKNF1N1NHsyUESdHZm8EY1NYJv690quMUhZ7NQXKfyW4jQW%2FhuiF37A%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("callTp","UTF-8") + "=" + URLEncoder.encode(wantedList.callTp, "UTF-8")); /*호출할 페이지 타입을 반드시 설정합니다.(L: 목록, D:상세)*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(wantedList.pageNo, "UTF-8")); /*기본값 1, 최대 1000 검색 시작위치를 지정할 수 있습니다*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(wantedList.numOfRows, "UTF-8")); /*출력건수, 기본값 10, 최대 500 까지 가능합니다.*/
        urlBuilder.append("&" + URLEncoder.encode("srchKeyCode","UTF-8") + "=" + URLEncoder.encode(wantedList.srchKeyCode, "UTF-8")); /*001 제목 002 내용 003 제목+내용*/

        if(!wantedList.searchWrd.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("searchWrd","UTF-8") + "=" + URLEncoder.encode(wantedList.searchWrd, "UTF-8")); /*검색어*/
        if(!wantedList.lifeArray.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("lifeArray","UTF-8") + "=" + URLEncoder.encode(wantedList.lifeArray, "UTF-8")); /*코드표참조*/
        if(!wantedList.charTrgterArray.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("charTrgterArray","UTF-8") + "=" + URLEncoder.encode(wantedList.charTrgterArray, "UTF-8")); /*코드표참조*/
        if(!wantedList.obstKiArray.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("obstKiArray","UTF-8") + "=" + URLEncoder.encode(wantedList.obstKiArray, "UTF-8")); /*코드표참조*/
        if(!wantedList.obstAbtArray.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("obstAbtArray","UTF-8") + "=" + URLEncoder.encode(wantedList.obstAbtArray, "UTF-8")); /*코드표참조*/
        if(!wantedList.trgterIndvdlArray.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("trgterIndvdlArray","UTF-8") + "=" + URLEncoder.encode(wantedList.trgterIndvdlArray, "UTF-8")); /*코드표참조*/
        if(!wantedList.desireArray.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("desireArray","UTF-8") + "=" + URLEncoder.encode(wantedList.desireArray, "UTF-8")); /*코드표참조*/

        return urlBuilder.toString();
    }

    // XML 파서 [ 목록 조회 ]
    public  ArrayList<PublicDataList> XMLParser(URL url)
    {
        int eventType =0;
        String tag;
        PublicDataList data = new PublicDataList();

        publicDataLists.clear();

        try {
            // XML 파서
            StringBuilder buffer = new StringBuilder();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            InputStream is =url.openStream();
            xpp.setInput( new InputStreamReader(is,"UTF-8") );

            xpp.next();


            while( eventType != XmlPullParser.END_DOCUMENT ) {

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작 \n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("servList")) {
                            data.SetEmpty();
                        }

                        else if(tag.equals("jurMnofNm")){
                            buffer.append("단체  : ");
                            xpp.next();
                            data.jurMnofNm =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("lifeArray")){
                            buffer.append("생애주기  : ");
                            xpp.next();
                            data.lifeArray =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }

                        else if(tag.equals("servDgst")){
                            buffer.append("설명 : ");
                            xpp.next();
                            data.servDgst =xpp.getText();
                            //servDgst = getTextpp.();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("servDtlLink")){
                            buffer.append("url링크 :");
                            xpp.next();
                            data.servDtlLink =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("servNm")){
                            buffer.append("제목 :");
                            xpp.next();
                            data.servNm =xpp.getText();
                            //servNm= xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("servId")){
                            buffer.append("서비스 ID :");
                            xpp.next();
                            data.servID =xpp.getText();


                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("trgterIndvdlArray")){
                            buffer.append("대상자 :");
                            xpp.next();
                            data.trgterIndvdlArray =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기

                        if(tag.equals("servList")) {
                            buffer.append("\n"); // 첫번째 검색결과 끝 줄바꿈

                            publicDataLists.add(new PublicDataList(data));

                        }
                        break;
                }
                eventType= xpp.next();
            }

        }
        catch (Exception e)
        {

        }

        return publicDataLists;
    }


}


