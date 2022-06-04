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

public class NonChatbotMainActivity extends AppCompatActivity {
    ImageView menubtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_chatbot_main);

       // Toast.makeText(getApplicationContext(), "챗봇 화면 !!", Toast.LENGTH_SHORT).show();

    }

    public void onClick_Faq_non(View view)
    {
        Intent intent = new Intent(this, NonChatbotFaqActivity.class);
        startActivity(intent);
    }

    public void onClick_Inquiry_non(View view)
    {
        Intent intent = new Intent(this, NonChatbotInquiryActivity.class);
        startActivity(intent);

    }

    public void onClick_Help_non(View view)
    {
        Intent intent = new Intent(this, NonChatbotHelpActivity.class);
        startActivity(intent);
    }

}