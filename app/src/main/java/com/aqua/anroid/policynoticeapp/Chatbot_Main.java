package com.aqua.anroid.policynoticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.User.MenuActivity;

public class Chatbot_Main extends AppCompatActivity {
    ImageView menubtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_main);


        menubtn = findViewById(R.id.menubtn);
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatbot_Main.this, MenuActivity.class);
                startActivity(intent);

            }
        });

    }

    public void onClick_Faq(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Chatbot_Faq.class);
        startActivity(intent);
    }

    public void onClick_Inquiry(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Chatbot_Inquiry.class);
        startActivity(intent);

    }

    public void onClick_Help(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Chatbot_Help.class);
        startActivity(intent);
    }

}