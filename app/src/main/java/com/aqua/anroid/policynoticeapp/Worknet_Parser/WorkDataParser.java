package com.aqua.anroid.policynoticeapp.Worknet_Parser;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//URL연결해주는곳
public class WorkDataParser {

    public WorkDataListParser workDataListParser = new WorkDataListParser();
    public WorkDataDetailParser workDataDetailParser = new WorkDataDetailParser();

    private  URL dataListURL   = null;
    private  URL dataDetailURL = null;

    //목록조회url연결
    public boolean  PulbicDataList_HttpURLConnection(WorkWantedList workWantedList) throws UnsupportedEncodingException, MalformedURLException {

        String strURL = workDataListParser.CreatePublicDataListURL(workWantedList);
        dataListURL = new URL(strURL);

        if(HttpURLConnection(dataListURL)) {
            return true;
        }
        return false;
    }

    public ArrayList<WorkDataList> XMLParserDataList()//목록파서
    {
        return workDataListParser.XMLParser(dataListURL);
    }

    //상세보기url연결
    public boolean  PulbicDataDetail_HttpURLConnection(WorkWantedDetail workWantedDetail)throws UnsupportedEncodingException, MalformedURLException{
        String strURL= workDataDetailParser.CreatePublicDetailURL(workWantedDetail);
        dataDetailURL=new URL(strURL);

        if(HttpURLConnection(dataDetailURL)) {
            return true;
        }
        return false;
    }

    public ArrayList<WorkDataDetail> XMLParserDataDetail()//상세파서
    {
        return workDataDetailParser.XMLParser(dataDetailURL);
    }

    // 연결
    private boolean HttpURLConnection(@NonNull URL url)
    {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            return true;

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}


