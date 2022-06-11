package com.aqua.anroid.policynoticeapp.NonUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.R;

public class NonChatbotFaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_chatbot_faq);

        ImageView backBtn = findViewById(R.id.backbtn);

        //뒤로가기 버튼 클릭 시
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //비회원 문의 메인 화면으로 이동
                Intent intent = new Intent(NonChatbotFaqActivity.this, NonChatbotMainActivity.class);
                startActivity(intent);

            }
        });

    }
}