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


        //메뉴 버튼 클릭 시 이동
        menubtn = findViewById(R.id.menubtn);
        menubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메뉴 화면으로 이동
                Intent intent = new Intent(Chatbot_Main.this, MenuActivity.class);
                startActivity(intent);

            }
        });

    }

    //자주묻는 질문 클릭 시 이동
    public void onClick_Faq(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Chatbot_Faq.class);
        startActivity(intent);
    }

    //1:1문의 클릭 시 이동
    public void onClick_Inquiry(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Chatbot_Inquiry.class);
        startActivity(intent);

    }

    //도움말 클릭 시 이동
    public void onClick_Help(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Chatbot_Help.class);
        startActivity(intent);
    }

}