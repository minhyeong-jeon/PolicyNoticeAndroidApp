package com.aqua.anroid.policynoticeapp;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class LocalIp extends Application {
    private static String IP_ADDRESS = "192.168.137.119";

    public String getIp(){
        return IP_ADDRESS;
    }
}

