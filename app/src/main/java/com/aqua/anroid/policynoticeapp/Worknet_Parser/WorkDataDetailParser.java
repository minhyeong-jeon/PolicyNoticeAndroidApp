package com.aqua.anroid.policynoticeapp.Worknet_Parser;

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
public class WorkDataDetailParser {
    public ArrayList<WorkDataDetail> workDataDetailArray = new ArrayList<WorkDataDetail>();

    // 상세 보기 URL 만들기
    public String CreatePublicDetailURL(@NonNull WorkWantedDetail workWantedDetail) throws UnsupportedEncodingException
    {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.work.go.kr/opi/opi/opia/wantedApi.do"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("authKey","UTF-8") +"="+ "WNL2XYNKFLGTOE5A06NKA2VR1HJ"); /*인증키*/
        urlBuilder.append("&" + URLEncoder.encode("callTp","UTF-8") + "=" + URLEncoder.encode(workWantedDetail.callTp, "UTF-8"));/*호출타입(L: 목록, D:상세)*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode(workWantedDetail.returnType, "UTF-8"));/*xml*/
        urlBuilder.append("&" + URLEncoder.encode("wantedAuthNo","UTF-8") + "=" + URLEncoder.encode(workWantedDetail.wantedAuthNo, "UTF-8"));/*구인인증번호*/
        urlBuilder.append("&" + URLEncoder.encode("infoSvc","UTF-8") + "=" + URLEncoder.encode(workWantedDetail.infoSvc, "UTF-8"));/*정보제공처*/

        return urlBuilder.toString();
    }
    // XML 파서 [ 상세 보기 ]
    public  ArrayList<WorkDataDetail> XMLParser(URL url)
    {
        int eventType =0;
        String tag;
        WorkDataDetail data = new WorkDataDetail();

        workDataDetailArray.clear();

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
                        if(tag.equals("wantedInfo")) {  //시작태그
                            data.SetEmpty();
                        }

                        else if(tag.equals("jobsNm")){ //모집집종

                            xpp.next();
                            data.jobsNm =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }

                        else if(tag.equals("wantedTitle")){ //구인제목
                            xpp.next();
                            data.wantedTitle =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("relJobsNm")){ //관련직종

                            xpp.next();
                            data.relJobsNm =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }

                        else if(tag.equals("workRegion")){  //근무예정지

                            xpp.next();
                            data.workRegion =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }

                        else if(tag.equals("jobCont")){  //직무내용

                            xpp.next();
                            data.jobCont =xpp.getText();
                            //servNm= xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("salTpNm")){  //임금조건

                            xpp.next();
                            data.salTpNm =xpp.getText();


                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }

                        else if(tag.equals("pfCond")){  //우대조건

                            xpp.next();
                            data.pfCond =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("selMthd")){  //전형방법

                            xpp.next();
                            data.selMthd =xpp.getText();

                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("workdayWorkhrCont")){  //근무시간/형태

                            xpp.next();
                            data.workdayWorkhrCont =xpp.getText();


                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기

                        if(tag.equals("wantedInfo")) {  //end태그
                            buffer.append("\n"); // 첫번째 검색결과 끝 줄바꿈

                            workDataDetailArray.add(new WorkDataDetail(data));

                        }
                        break;
                }
                eventType= xpp.next();
            }

        }
        catch (Exception e)
        {

        }

        return workDataDetailArray;
    }
}
