package com.aqua.anroid.policynoticeapp.NonUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.Chatbot_Faq;
import com.aqua.anroid.policynoticeapp.Chatbot_Help;
import com.aqua.anroid.policynoticeapp.Chatbot_Inquiry;
import com.aqua.anroid.policynoticeapp.R;
import com.aqua.anroid.policynoticeapp.User.MenuActivity;

public class NonChatbotMainActivity extends AppCompatActivity {
    ImageView homebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_chatbot_main);

        //메뉴버튼 클릭 시 메뉴화면으로 이동
        homebtn = findViewById(R.id.homebtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NonChatbotMainActivity.this, NonPublicActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onClick_Faq_non(View view)  //자주묻는질문 클릭 시
    {
        Intent intent = new Intent(this, NonChatbotFaqActivity.class);
        startActivity(intent);
    }

    public void onClick_Inquiry_non(View view)  //1:1문의 클릭 시
    {
        Intent intent = new Intent(this, NonChatbotInquiryActivity.class);
        startActivity(intent);

    }

    public void onClick_Help_non(View view)  //도움말 클릭 시
    {
        Intent intent = new Intent(this, NonChatbotHelpActivity.class);
        startActivity(intent);
    }

}