package com.aqua.anroid.policynoticeapp.Parser;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

//상세 보기 URL 만들고 파싱해주는곳
public class PublicDataDetailParser {
    public ArrayList<PublicDataDetail> publicDataDetailArray = new ArrayList<PublicDataDetail>();

    // 상세 보기 URL 만들기
    public String CreatePublicDetailURL(@NonNull WantedDetail wantedDetail) throws UnsupportedEncodingException
    {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B554287/NationalWelfareInformations/NationalWelfaredetailed"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=%2BWnjcadNxjH3FFyaHjifaa6i%2Fi3l9YuKKNF1N1NHsyUESdHZm8EY1NYJv690quMUhZ7NQXKfyW4jQW%2FhuiF37A%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("callTp","UTF-8") + "=" + URLEncoder.encode(wantedDetail.callTp, "UTF-8")); /*호출할 페이지 타입을 반드시 설정합니다.(L: 목록, D:상세)*/
        urlBuilder.append("&" + URLEncoder.encode("servId","UTF-8") + "=" + URLEncoder.encode(wantedDetail.servID, "UTF-8")); /*검색어*/
        return urlBuilder.toString();
    }
    // XML 파서 [ 상세 보기 ]
    public  ArrayList<PublicDataDetail> XMLParser(URL url)
    {
        int eventType =0;
        String tag;
        PublicDataDetail data = new PublicDataDetail();

        publicDataDetailArray.clear();

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
                        if(tag.equals("wantedDtl")) {
                            data.SetEmpty();
                        }

                        else if(tag.equals("servNm")){
                            buffer.append("서비스명  : ");
                            xpp.next();
                            data.servNm =xpp.getText();
                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }

                        else if(tag.equals("jurMnofNm")){
                            buffer.append("소관부처명 : ");
                            xpp.next();
                            data.jurMnofNm =xpp.getText();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("tgtrDtlCn")){
                            buffer.append("대상자 :");
                            xpp.next();
                            data.tgtrDtlCn =xpp.getText();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("slctCritCn")){
                            buffer.append("선정기준 :");
                            xpp.next();
                            data.slctCritCn =xpp.getText();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("alwServCn")){
                            buffer.append("급여서비스:");
                            xpp.next();
                            data.alwServCn =xpp.getText();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("trgterIndvdlArray")){
                            buffer.append("가구유형 :");
                            xpp.next();
                            data.trgterIndvdlArray =xpp.getText();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("lifeArray")){
                            buffer.append("생애주기 :");
                            xpp.next();
                            data.lifeArray =xpp.getText();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기

                        if(tag.equals("wantedDtl")) {
                            buffer.append("\n"); // 첫번째 검색결과 끝 줄바꿈

                            publicDataDetailArray.add(new PublicDataDetail(data));

                        }
                        break;
                }
                eventType= xpp.next();
            }

        }
        catch (Exception e)
        {

        }

        return publicDataDetailArray;
    }
}
