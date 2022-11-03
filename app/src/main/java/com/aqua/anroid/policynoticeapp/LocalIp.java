package com.aqua.anroid.policynoticeapp;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class LocalIp extends Application {
    private static String IP_ADDRESS = "172.20.10.4";

    public String getIp(){
        return IP_ADDRESS;
    }
}

