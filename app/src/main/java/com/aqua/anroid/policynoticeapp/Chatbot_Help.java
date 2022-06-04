package com.aqua.anroid.policynoticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Chatbot_Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_help);

        ImageView backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatbot_Help.this,Chatbot_Main.class);
                startActivity(intent);

            }
        });

        Toast.makeText(this, "1대1문의", Toast.LENGTH_SHORT).show();

    }
}