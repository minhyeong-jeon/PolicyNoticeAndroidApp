package com.aqua.anroid.policynoticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.User.MemberUpdateActivity;

public class Chatbot_Faq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_faq);

        ImageView backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatbot_Faq.this, Chatbot_Main.class);
                startActivity(intent);

            }
        });

       // Toast.makeText(this, "자주하는 질문 화면 !!", Toast.LENGTH_SHORT).show();

    }
}