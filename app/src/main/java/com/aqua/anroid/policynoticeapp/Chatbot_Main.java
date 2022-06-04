package com.aqua.anroid.policynoticeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.Favorite.FavoriteActivity;

public class Chatbot_Main extends AppCompatActivity {
    ImageView menubtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_main);

       // Toast.makeText(getApplicationContext(), "챗봇 화면 !!", Toast.LENGTH_SHORT).show();

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
        //Toast.makeText(getApplicationContext(), "자주하는 질문!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Chatbot_Faq.class);
        startActivity(intent);
        //finish();
    }

    public void onClick_Inquiry(View view)
    {
       // Toast.makeText(getApplicationContext(),"1대1문의",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Chatbot_Inquiry.class);
        startActivity(intent);

    }

    public void onClick_Help(View view)
    {
       // Toast.makeText(getApplicationContext(),"도움말",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Chatbot_Help.class);
        startActivity(intent);
    }

}