package com.aqua.anroid.policynoticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.User.MainActivity;

//초기화면 엑티비티
public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash_intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(splash_intent);
                finish();
            }
        },2000);


    }

}
