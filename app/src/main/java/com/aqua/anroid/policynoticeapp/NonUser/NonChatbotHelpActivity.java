package com.aqua.anroid.policynoticeapp.NonUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aqua.anroid.policynoticeapp.R;

public class NonChatbotHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_chatbot_help);

        ImageView backBtn = findViewById(R.id.backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NonChatbotHelpActivity.this,NonChatbotMainActivity.class);
                startActivity(intent);

            }
        });

        Toast.makeText(this, "1대1문의", Toast.LENGTH_SHORT).show();

    }
}