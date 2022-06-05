package com.aqua.anroid.policynoticeapp.worknet_Parser;

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
public class WorkDataListParser {

    public ArrayList<WorkDataList> workDataLists = new ArrayList<WorkDataList>();


    // 목록 조회 URL 만들기
    public String CreatePublicDataListURL(@NonNull WorkWantedList workWantedList) throws UnsupportedEncodingException
    {

        StringBuilder urlBuilder = new StringBuilder("http://openapi.work.go.kr/opi/opi/opia/wantedApi.do"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("authKey","UTF-8") +"="+ "WNL2XYNKFLGTOE5A06NKA2VR1HJ");
        urlBuilder.append("&" + URLEncoder.encode("callTp","UTF-8") + "=" + URLEncoder.encode(workWantedList.callTp, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode(workWantedList.returnType, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("startPage","UTF-8") + "=" + URLEncoder.encode(workWantedList.startPage, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("display","UTF-8") + "=" + URLEncoder.encode(workWantedList.display, "UTF-8"));

        if(!workWantedList.region.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("region","UTF-8") + "=" + URLEncoder.encode(workWantedList.region, "UTF-8")); /*검색어*/
        if(!workWantedList.salTpNm.isEmpty())
            urlBuilder.append("&" + URLEncoder.encode("salTpNm","UTF-8") + "=" + URLEncoder.encode(workWantedList.salTpNm, "UTF-8")); /*코드표참조*/

        return urlBuilder.toString();
    }

    // XML 파서 [ 목록 조회 ]
    public  ArrayList<WorkDataList> XMLParser(URL url)
    {
        int eventType =0;
        String tag;
        WorkDataList data = new WorkDataList();

        workDataLists.clear();

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
                                        tag= xpp.getName();

                                        tag= xpp.getName(); // 태그 이름 얻어오기
                                        if(tag.equals("wanted")) {
                                            data.SetEmpty();
                                        }

                                        else if(tag.equals("wantedAuthNo")){
                                            xpp.next();
                                            data.wantedAuthNo =xpp.getText();

                                            buffer.append(xpp.getText());
                                            buffer.append("\n"); //줄바꿈 문자 추가
                                        }
                                        else if (tag.equals("company")) {
                                            xpp.next();
                                            data.company = xpp.getText();

                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                        else if (tag.equals("title")) {
                                            xpp.next();
                                            data.title = xpp.getText();

                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                        else if(tag.equals("salTpNm")){
                                            xpp.next();
                                            data.salTpNm =xpp.getText();


                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                        else if(tag.equals("region")){

                                            xpp.next();
                                            data.region =xpp.getText();

                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                        else if(tag.equals("regDt")){

                                            xpp.next();
                                            data.regDt =xpp.getText();

                                            buffer.append(xpp.getText());
                                            buffer.append("\n"); //줄바꿈 문자 추가
                                        }
                                        else if(tag.equals("closeDt")){

                                            xpp.next();
                                            data.closeDt =xpp.getText();

                                            buffer.append(xpp.getText());
                                            buffer.append("\n"); //줄바꿈 문자 추가
                                        }
                                        else if(tag.equals("wantedInfoUrl")){

                                            xpp.next();
                                            data.wantedInfoUrl =xpp.getText();

                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                        break;

//

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기

                        if(tag.equals("wanted")) {
                            buffer.append("\n"); // 첫번째 검색결과 끝 줄바꿈

                            workDataLists.add(new WorkDataList(data));

                        }
                        break;
                }
                eventType= xpp.next();
            }

        }
        catch (Exception e)
        {

        }

        return workDataLists;
    }


}


